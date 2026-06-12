package com.tao.android.ai_ad_recommendation.ui.screen;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.tabs.TabLayout;
import com.tao.android.ai_ad_recommendation.AiAdApplication;
import com.tao.android.ai_ad_recommendation.R;
import com.tao.android.ai_ad_recommendation.data.repository.AdRepository;
import com.tao.android.ai_ad_recommendation.data.repository.BehaviorRepository;
import com.tao.android.ai_ad_recommendation.model.AdItem;
import com.tao.android.ai_ad_recommendation.ui.component.InteractionBar;
import com.tao.android.ai_ad_recommendation.ui.component.VideoPlayerView;
import com.tao.android.ai_ad_recommendation.viewmodel.MainFeedViewModel;

import java.util.List;

/**
 * 主信息流 Fragment - 全屏滑动广告列表
 *
 * 💡 知识点：Fragment 是"轻量级Activity"，可以嵌入到Activity中
 *         生命周期：onCreateView (加载布局) → onViewCreated (初始化控件)
 *
 * 🔗 包含：TabLayout(分类切换) + SwipeRefreshLayout(下拉刷新) + RecyclerView(广告列表)
 *
 * ====== 框架说明 ======
 * 框架已搭建。onViewCreated 中的初始化逻辑需要你来完善。
 * 包括：RecyclerView设置、Adapter绑定、Tab监听、下拉刷新、上滑加载更多。
 */
public class MainFeedFragment extends Fragment {

    MainFeedViewModel viewModel;
    private AdFeedAdapter adapter;

    private TabLayout tabLayout;
    private SwipeRefreshLayout swipeRefresh;
    private RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main_feed, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // ====== 第一步：绑定控件 ======
        tabLayout = view.findViewById(R.id.tab_layout);
        swipeRefresh = view.findViewById(R.id.swipe_refresh);
        recyclerView = view.findViewById(R.id.recycler_view);

        // 搜索框：点击弹出搜索弹窗
        EditText searchBox = view.findViewById(R.id.search_box);
        searchBox.setOnClickListener(v -> {
            SearchDialogFragment dialog = new SearchDialogFragment();
            // 直接传数据，不走parentFragment（因为用的是Activity的FragmentManager）
            if (viewModel.adList.getValue() != null) {
                dialog.setAllAds(viewModel.adList.getValue());
            }
            dialog.show(getParentFragmentManager(), "SearchDialog");
        });

        // ====== 第二步：创建ViewModel ======
        // 💡 知识点：ViewModelProvider 创建ViewModel，自动管理生命周期
        AdRepository repository = new AdRepository(requireContext());
        viewModel = new ViewModelProvider(this,
                new MainFeedViewModelFactory(repository))
                .get(MainFeedViewModel.class);

        // ====== 第三步：设置RecyclerView ======
        // TODO: 【你来写-中等】setupRecyclerView()
        //
        // 思路：
        // 1. 创建 LinearLayoutManager (VERTICAL)
        // 2. recyclerView.setLayoutManager(...)
        // 3. 创建 AdFeedAdapter
        // 4. recyclerView.setAdapter(adapter)


        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        BehaviorRepository behaviorRopo=new BehaviorRepository(
                ((AiAdApplication)requireActivity().getApplication()).getDatabase().behaviorDao());
        adapter=new AdFeedAdapter(behaviorRopo);
        recyclerView.setAdapter(adapter);

        // ====== 第四步：设置Tab切换 ======
        // TODO: 【你来写-中等】setupTabLayout()
        //
        // 思路：
        // 1. tabLayout.addTab(tabLayout.newTab().setText("推荐"))
        // 2. tabLayout.addTab(tabLayout.newTab().setText("关注"))
        // 3. tabLayout.addTab(tabLayout.newTab().setText("热门"))
        // 4. tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener(){...})
        //    在 onTabSelected 中调用 viewModel.switchTab(tab.getText().toString())

        tabLayout.addTab(tabLayout.newTab().setText("推荐"));
        tabLayout.addTab(tabLayout.newTab().setText("关注"));
        tabLayout.addTab(tabLayout.newTab().setText("热门"));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewModel.switchTab(tab.getText().toString());
            }
            @Override public void onTabUnselected(TabLayout.Tab tab) {}
            @Override public void onTabReselected(TabLayout.Tab tab) {}
        });
        // ====== 第五步：设置下拉刷新 ======
        // TODO: 【你来写-简单】setupSwipeRefresh()
        //
        // 思路：
        // swipeRefresh.setOnRefreshListener(() -> viewModel.refresh())
        // 观察 viewModel.isRefreshing，更新 swipeRefresh.setRefreshing()

        swipeRefresh.setOnRefreshListener(() -> viewModel.refresh());
        viewModel.isRefreshing.observe(getViewLifecycleOwner(), refreshing
                ->
                swipeRefresh.setRefreshing(refreshing != null && refreshing));

        // ====== 第六步：设置无限滚动 ======
        // TODO: 【你来写-中等】setupInfiniteScroll()
        //
        // 思路：给RecyclerView添加滚动监听
        // recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
        //     @Override
        //     public void onScrolled(RecyclerView rv, int dx, int dy) {
        //         判断是否滑到底部：
        //         LinearLayoutManager lm = (LinearLayoutManager) rv.getLayoutManager();
        //         int lastVisible = lm.findLastVisibleItemPosition();
        //         int totalItems = adapter.getItemCount();
        //         if (lastVisible >= totalItems - 2) {
        //             viewModel.loadNextPage();
        //         }
        //     }
        // })


        // 埋点：记录已曝光过的广告ID（避免重复计数）
        java.util.Set<String> impressedIds = new java.util.HashSet<>();

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView rv, int dx, int dy) {
                LinearLayoutManager lm = (LinearLayoutManager) rv.getLayoutManager();
                if (lm == null) return;

                // 无限滚动
                if (lm.findLastVisibleItemPosition() >= adapter.getItemCount() - 2) {
                    viewModel.loadNextPage();
                }

                // 曝光埋点：当前可见的卡片记录曝光
                int firstVisible = lm.findFirstVisibleItemPosition();
                int lastVisible = lm.findLastVisibleItemPosition();
                if (adapter.behaviorRepo != null && adapter.adList != null) {
                    for (int i = firstVisible; i <= lastVisible && i < adapter.adList.size(); i++) {
                        String id = adapter.adList.get(i).getId();
                        if (!impressedIds.contains(id)) {
                            impressedIds.add(id);
                            adapter.behaviorRepo.recordImpression(id);
                        }
                    }
                }
            }
        });

        // ====== 第七步：观察ViewModel数据变化 ======
        // TODO: 【你来写-中等】observeData()
        //
        // 思路：
         viewModel.adList.observe(getViewLifecycleOwner(), ads -> {
             adapter.setData(ads);
             adapter.notifyDataSetChanged();
         });
        //
        // viewModel.isLoading.observe(...)  // 控制loading提示
        // viewModel.hasMore.observe(...)    // 控制"没有更多了"


        // ====== 第八步：加载首页数据 ======
        viewModel.loadFirstPage();
    }

    /**
     * RecyclerView Adapter - 广告卡片适配器
     *
     * TODO: 【你来写-中等】AdFeedAdapter
     *
     * 这个Adapter是你之前在课件里学过的 RecyclerView.Adapter 实战！
     * 回顾课件二的Adapter三步：onCreateViewHolder / onBindViewHolder / getItemCount
     *
     * 需要实现的功能：
     * 1. 接收 List<AdItem> 数据
     * 2. 根据 AdItem.type 返回不同布局
     *    - large_image → 大图卡片布局
     *    - small_image → 小图卡片布局
     *    - video → 视频卡片布局
     * 3. 点击事件 → 跳转到 DetailActivity
     * 4. Glide 加载图片到 ImageView
     */
    private  class AdFeedAdapter extends RecyclerView.Adapter<AdFeedAdapter.ViewHolder> {

        List<AdItem> adList;
        final BehaviorRepository behaviorRepo;

        private AdFeedAdapter(BehaviorRepository behaviorRepo) {
            this.behaviorRepo = behaviorRepo;
        }

        public void setData(List<AdItem> ads) {
            this.adList = ads;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            // TODO: 【你来写-中等】根据viewType加载不同item布局
            // 用 LayoutInflater.from(parent.getContext()).inflate(...)

            int layoutId;
            if (viewType==1){
                layoutId=R.layout.item_ad_small_image;
            }else {
                layoutId=R.layout.view_ad_card;
            }
            View view=LayoutInflater.from(parent.getContext()).inflate(layoutId,parent,false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            // TODO: 【你来写-中等】绑定数据到ViewHolder
            // AdItem item = adList.get(position)

            AdItem item=adList.get(position);

            holder.titleView.setText(item.getTitle());
            holder.descriptionView.setText(item.getDescription());
            holder.advertiserView.setText(item.getAdvertiser());

            // 色块颜色
            int bgColor = getCategoryColor(item.getCategory());
            holder.bannerContainer.getBackground().setTint(bgColor);
            holder.categoryBannerText.setText(item.getTitle());

            // ─── 三种样式：大图/小图/视频 ───
            String adType = item.getType();
            if ("video".equals(adType)) {
                // 视频卡片：不创建ExoPlayer（防闪），显示色块+▶按钮，点击跳详情页全屏有声播放
                if (holder.videoPlayerView != null) holder.videoPlayerView.setVisibility(View.GONE);
                if (holder.videoPlayButton != null) {
                    holder.videoPlayButton.setVisibility(View.VISIBLE);
                    holder.videoPlayButton.setOnClickListener(v -> {
                        Intent intent = new Intent(v.getContext(), DetailActivity.class);
                        intent.putExtra("ad_item", item);
                        intent.putExtra("auto_play", true);
                        v.getContext().startActivity(intent);
                    });
                }
                holder.categoryBannerText.setText("▶ " + item.getTitle());
                // 色块用深色背景表示视频卡片
                holder.bannerContainer.getBackground().setTint(0xFF4A4A5A);
            } else {
                if (holder.videoPlayerView != null) holder.videoPlayerView.setVisibility(View.GONE);
                if (holder.videoPlayButton != null) holder.videoPlayButton.setVisibility(View.GONE);
            }

            // AI摘要
            if (item.getSummary() != null && !item.getSummary().isEmpty()) {
                holder.cardSummaryView.setText(item.getSummary());
                holder.cardSummaryView.setVisibility(View.VISIBLE);
            } else {
                holder.cardSummaryView.setVisibility(View.GONE);
            }

            // 标签
            holder.tagsContainer.removeAllViews();
            for (String tag : item.getTags().split(",")) {
                String t = tag.trim();
                if (!t.isEmpty()) {
                    TextView chip = new TextView(holder.itemView.getContext());
                    chip.setText(t);
                    chip.setTextColor(0xFF999999);
                    chip.setTextSize(11);
                    chip.setBackgroundResource(R.drawable.bg_tag_rounded);
                    chip.setPadding(16, 4, 16, 4);
                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    lp.setMarginEnd(8);
                    lp.bottomMargin = 4;
                    chip.setLayoutParams(lp);
                    holder.tagsContainer.addView(chip);
                }
            }

            String adId = item.getId();
            behaviorRepo.getLikeCount(adId).observe(getViewLifecycleOwner(), likeCount -> {
                if (likeCount !=null) {
                    holder.interactionBar.setLiked(likeCount>0,likeCount);
                }
            });

            behaviorRepo.getFavoriteCount(adId).observe(getViewLifecycleOwner(), favoriteCount -> {
                if (favoriteCount !=null) {
                    holder.interactionBar.setFavorited(favoriteCount>0,favoriteCount);
                }
            });

            behaviorRepo.getComments(adId).observe(getViewLifecycleOwner(), comments -> {
                if (comments != null) {
                    holder.interactionBar.setCommentCount(comments.size());
                }
            });

            holder.interactionBar.setOnInteractionListener(new InteractionBar.OnInteractionListener() {

                @Override
                public void onLikeClick() {
                    behaviorRepo.toggleLike(adId,isActive -> {});
                }

                @Override
                public void onFavoriteClick() {
                    behaviorRepo.toggleFavorite(adId,isActive -> {});
                }

                @Override
                public void onCommentClick() {
                    Intent intent = new Intent(holder.itemView.getContext(), DetailActivity.class);
                    intent.putExtra("ad_item", item);
                    holder.itemView.getContext().startActivity(intent);
                }
            });


            holder.itemView.setOnClickListener(v -> {
                Intent intent=new Intent(v.getContext(), DetailActivity.class);
                intent.putExtra("ad_item", item);
                v.getContext().startActivity(intent);
            });
        }

        /** 根据广告分类返回对应的颜色（每个分类视觉上能区分） */
        private int getCategoryColor(String category) {
            switch (category) {
                case "推荐": return 0xFFA8D8EA;  // 天空蓝
                case "关注": return 0xFFFFC8C3;  // 樱花粉
                case "热门": return 0xFFFFE0C8;  // 蜜桃橙
                default:     return 0xFFC1E8D5;  // 薄荷绿
            }
        }

        @Override
        public int getItemCount() {
            return adList != null ? adList.size() : 0;
        }

        /**
         * 💡 知识点：通过 getItemViewType 返回不同布局类型
         *         RecyclerView 会根据 viewType 自动调用对应的 onCreateViewHolder
         */
        @Override
        public int getItemViewType(int position) {
            // TODO: 【你来写-中等】根据 adList.get(position).getType() 返回不同type值
//            只用手机端
            AdItem item = adList.get(position);
            switch (item.getType()) {          // ← 是 getType() 不是 getCategory()！
                case "large_image": return 0;
                case "small_image": return 1;
                case "video":       return 2;
                default:            return 0;
            }
        }

         class ViewHolder extends RecyclerView.ViewHolder {
            // TODO: 【你来写-简单】在构造方法中 findViewById 绑定子控件
            // 标题、描述、图片、视频播放器、标签、互动按钮等
            ImageView mainImageView;
            FrameLayout bannerContainer;
            TextView categoryBannerText, cardSummaryView;
            TextView titleView, descriptionView, advertiserView;
            LinearLayout tagsContainer;
            InteractionBar interactionBar;
            ImageView videoPlayButton;
            VideoPlayerView videoPlayerView;
            String lastSetupUrl; // 防重复初始化导致闪烁

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                mainImageView = itemView.findViewById(R.id.card_image);
                bannerContainer = itemView.findViewById(R.id.card_media_container);
                categoryBannerText = itemView.findViewById(R.id.card_banner_text);
                titleView = itemView.findViewById(R.id.card_title);
                descriptionView = itemView.findViewById(R.id.card_description);
                advertiserView = itemView.findViewById(R.id.card_advertiser);
                cardSummaryView = descriptionView;
                tagsContainer = itemView.findViewById(R.id.card_tags_container);
                interactionBar = itemView.findViewById(R.id.card_interaction_bar);
                videoPlayButton = itemView.findViewById(R.id.card_video_play_btn);
                videoPlayerView = itemView.findViewById(R.id.card_video_player);
            }
        }
    }

    /**
     * ViewModel 工厂类 - 因为 ViewModel 构造需要参数，需要自定义 Factory
     *
     * ====== 框架说明 ======
     * 【已实现】不需要修改。
     * 如果你的ViewModel构造参数变了，在这里相应修改即可。
     */
    public static class MainFeedViewModelFactory implements ViewModelProvider.Factory {
        private final AdRepository repository;

        public MainFeedViewModelFactory(AdRepository repository) {
            this.repository = repository;
        }

        @NonNull
        @Override
        public <T extends androidx.lifecycle.ViewModel> T create(@NonNull Class<T> modelClass) {
            return (T) new MainFeedViewModel(repository);
        }
    }
}
