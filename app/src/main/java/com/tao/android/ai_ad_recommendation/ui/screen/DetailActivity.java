package com.tao.android.ai_ad_recommendation.ui.screen;

import android.os.Bundle;

import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.tao.android.ai_ad_recommendation.AiAdApplication;
import com.tao.android.ai_ad_recommendation.R;
import com.tao.android.ai_ad_recommendation.data.repository.BehaviorRepository;
import com.tao.android.ai_ad_recommendation.model.AdItem;
import com.tao.android.ai_ad_recommendation.model.UserBehavior;
import com.tao.android.ai_ad_recommendation.ui.component.InteractionBar;
import com.tao.android.ai_ad_recommendation.ai.AiChatHelper;
import com.tao.android.ai_ad_recommendation.ui.component.VideoPlayerView;
import com.tao.android.ai_ad_recommendation.viewmodel.DetailViewModel;

/**
 * 广告详情页 - 全屏图片/视频预览 + 互动操作
 *
 * 💡 知识点：通过 Intent 接收 MainFeedFragment 传来的广告数据
 *         getIntent().getSerializableExtra("ad_item") 获取 AdItem 对象
 *
 * 🔗 被打开：MainFeedFragment (点击广告卡片时)
 * 🔗 复用组件：VideoPlayerView / InteractionBar / TagChipView
 *
 * ====== 框架说明 ======
 * 框架已搭建。初始化控件、绑定数据、设置互动逻辑需要你来写。
 */
public class DetailActivity extends AppCompatActivity {

    private DetailViewModel viewModel;
    private AdItem adItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        // ====== 第1步：接收Intent传来的广告数据 ======
        // 💡 知识点：AdItem 实现了 Serializable，可以通过Intent传递

        adItem = (AdItem) getIntent().getSerializableExtra("ad_item");
        if (adItem == null) {
            finish();  // 数据异常，直接关闭
            return;
        }

        // ====== 第2步：创建ViewModel ======
//        BehaviorRepository behaviorRepository = new BehaviorRepository(
//                ((AiAdApplication) getApplication()).getDatabase().behaviorDao());
//        viewModel = new ViewModelProvider(this).get(DetailViewModel.class);
//        // TODO: 如果ViewModel需要构造参数，创建Factory（参考MainFeedFragment的做法）
//        viewModel.setAd(adItem);
        // 💡 知识点：ViewModel构造有参数时，必须用Factory

        BehaviorRepository behaviorRepository = new BehaviorRepository(((AiAdApplication) getApplication()).getDatabase().behaviorDao());
        viewModel = new ViewModelProvider(this, new DetailViewModelFactory(behaviorRepository))
                .get(DetailViewModel.class);
        viewModel.setAd(adItem);  // ← 这行丢了!


        // ====== 第3步：绑定控件 + 设置数据 ======
        // TODO: 【你来写-中等】setupUI()
        //
        // 思路：
        // 1. findViewById 获取各种控件
        // 2. 根据 adItem.getType() 决定显示 ImageView 还是 VideoPlayerView
        // 3. 如果是图片：用 Glide 加载大图
        // 4. 如果是视频：初始化 ExoPlayer，设置视频URL
        // 5. 设置标题、描述、广告主等信息

        // 顶部图片
        ImageView imageView = findViewById(R.id.detail_image);
        TextView bannerText = findViewById(R.id.detail_banner_text);
        bannerText.setVisibility(View.GONE);  // 有真实图片后不再需要色块文字
        if (adItem.getImageUrl() != null && !adItem.getImageUrl().isEmpty()) {
            com.bumptech.glide.Glide.with(this)
                .load(adItem.getImageUrl())
                .centerCrop()
                .into(imageView);
        }


        //AI
        // ─── 视频模式：全屏播放器 ───
        VideoPlayerView detailVideoPlayer = findViewById(R.id.detail_video_player);
        FrameLayout mediaContainer = findViewById(R.id.detail_media_container);

        if ("video".equals(adItem.getType()) && adItem.getVideoUrl() != null
                && !adItem.getVideoUrl().isEmpty()) {
            // 隐藏非视频内容
            imageView.setVisibility(View.GONE);
            bannerText.setVisibility(View.GONE);
            // 16:9 视频卡片（父容器是LinearLayout，必须用LinearLayout.LayoutParams）
            float density = getResources().getDisplayMetrics().density;
            int margin = (int) (16 * density);
            int screenW = getResources().getDisplayMetrics().widthPixels;
            int cardH = (screenW - margin * 2) * 9 / 16;
            LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, cardH);
            llp.setMargins(margin, (int)(12*density), margin, 0);
            mediaContainer.setLayoutParams(llp);
            mediaContainer.setBackgroundResource(R.drawable.bg_card_rounded);
            mediaContainer.setClipToOutline(true);
            // 启动视频
            detailVideoPlayer.setVisibility(View.VISIBLE);
            detailVideoPlayer.setMuted(false);
            detailVideoPlayer.showController();
            detailVideoPlayer.setupVideo(adItem.getVideoUrl());
            // 静音切换按钮
            ImageView muteBtn = findViewById(R.id.detail_mute_btn);
            muteBtn.setVisibility(View.VISIBLE);
            final boolean[] isMuted = {false};
            muteBtn.setOnClickListener(v2 -> {
                isMuted[0] = !isMuted[0];
                detailVideoPlayer.setMuted(isMuted[0]);
                muteBtn.setImageResource(isMuted[0] ? R.drawable.ic_volume_off : R.drawable.ic_volume_on);
            });
            if (getIntent().getBooleanExtra("auto_play", false)) {
                detailVideoPlayer.play();
            }
        } else {
            detailVideoPlayer.setVisibility(View.GONE);
            findViewById(R.id.detail_mute_btn).setVisibility(View.GONE);
        }

        // 信息卡片
        TextView titleView = findViewById(R.id.detail_title);
        titleView.setText(adItem.getTitle());

        TextView descView = findViewById(R.id.detail_description);
        descView.setText(adItem.getDescription());

        TextView advertiserView = findViewById(R.id.detail_advertiser);
        advertiserView.setText(adItem.getAdvertiser());

        // AI 深度分析（调用大模型，失败则用Mock兜底）
        TextView aiAnalysis = findViewById(R.id.detail_ai_analysis);
        TextView aiRefreshBtn = findViewById(R.id.detail_ai_refresh);
        ProgressBar aiProgress = findViewById(R.id.detail_ai_progress);
        AiChatHelper aiHelper = new AiChatHelper();

        // 加载AI分析
        loadAiAnalysis(aiHelper, adItem, aiAnalysis, aiProgress);

        // "换一个"按钮
        aiRefreshBtn.setOnClickListener(v ->
            loadAiAnalysis(aiHelper, adItem, aiAnalysis, aiProgress));

        // 亮点速览（根据tags拆分）
        LinearLayout highlightsContainer = findViewById(R.id.detail_highlights_container);
        String[] tags = adItem.getTags().split(",");
        for (String tag : tags) {
            TextView tagItem = new TextView(this);
            tagItem.setText("✅ " + getHighlightText(tag.trim()));
            tagItem.setTextColor(0xFF666666);
            tagItem.setTextSize(13);
            tagItem.setPadding(0, 6, 0, 0);
            highlightsContainer.addView(tagItem);
        }

        // ─── 埋点统计：进详情页记录1次曝光+1次点击，Room异步更新UI ───
        TextView impressionsView = findViewById(R.id.detail_stat_impressions);
        TextView clicksView = findViewById(R.id.detail_stat_clicks);
        TextView rateView = findViewById(R.id.detail_stat_rate);

        // 先记录（异步），再观察（等Room返回真实数据）
        behaviorRepository.recordImpression(adItem.getId());
        behaviorRepository.recordClick(adItem.getId());

        // 两个变量保存最新数据，都到齐后算点击率
        final int[] latestImp = {0};
        final int[] latestClick = {0};

        Runnable updateRate = () -> {
            if (latestImp[0] > 0) {
                rateView.setText(String.format("%.1f%%", latestClick[0] * 100f / latestImp[0]));
            } else {
                rateView.setText("-");
            }
        };

        behaviorRepository.getImpressionCount(adItem.getId()).observe(this, count -> {
            latestImp[0] = count != null ? count : 0;
            impressionsView.setText(formatNumber(latestImp[0]));
            updateRate.run();
        });

        behaviorRepository.getClickCount(adItem.getId()).observe(this, count -> {
            latestClick[0] = count != null ? count : 0;
            clicksView.setText(formatNumber(latestClick[0]));
            updateRate.run();
        });

        // ====== 第4步：设置互动按钮 ======
        // TODO: 【你来写-中等】setupInteraction()
        //
        // 思路：
        // 1. 点赞按钮点击 → viewModel.toggleLike()
        // 2. 收藏按钮点击 → viewModel.toggleFavorite()
        // 3. 评论按钮点击 → 弹出评论DialogFragment
        // 4. 观察 viewModel.isLiked / isFavorited 更新按钮状态
        // 5. 观察 viewModel.likeCount / favoriteCount 更新数字

        InteractionBar interactionBar = findViewById(R.id.detail_interaction_bar);
        interactionBar.setOnInteractionListener(new InteractionBar.OnInteractionListener() {

            @Override
            public void onLikeClick() {
                viewModel.toggleLike();
            }

            @Override
            public void onFavoriteClick() {
                viewModel.toggleFavorite();
            }

            @Override
            public void onCommentClick() {
                AlertDialog.Builder builder = new AlertDialog.Builder(DetailActivity.this);
                builder.setTitle("发表评论");
                EditText input = new EditText(DetailActivity.this);

                builder.setView(input);
                builder.setPositiveButton("发送",(dialog,which)->{
                    String text = input.getText().toString().trim();
                    if (!text.isEmpty()) {
                        viewModel.addComment(text);
                    }
                });
                builder.setNegativeButton("取消",null);
                builder.show();
            }
        });


        // ====== 第5步：观察Toast提示 ======
        // viewModel.toastMessage.observe(this, msg ->
        //     Toast.makeText(this, msg, Toast.LENGTH_SHORT).show());
        viewModel.isLiked.observe(this, liked -> {
            interactionBar.setLiked(liked!=null&&liked,viewModel.likeCount.getValue()!=null ?viewModel.likeCount.getValue():0);
        });

        viewModel.isFavorited.observe(this, fav -> {
            interactionBar.setFavorited(fav!=null&&fav,viewModel.favoriteCount.getValue()!=null ?viewModel.favoriteCount.getValue():0);
        });

        viewModel.likeCount.observe(this, count -> {
            interactionBar.setLiked(viewModel.isLiked.getValue()!=null&&viewModel.isLiked.getValue(),count!=null?count:0);
        });

        viewModel.favoriteCount.observe(this, count -> {
            interactionBar.setFavorited(viewModel.isFavorited.getValue()!=null&&viewModel.isFavorited.getValue(),count!=null?count:0);
        });

        viewModel.commentCount.observe(this, count -> {
            interactionBar.setCommentCount(count != null ? count : 0);
        });

        // 评论列表展示
        LinearLayout commentsList = findViewById(R.id.detail_comments_list);
        TextView commentsEmpty = findViewById(R.id.detail_comments_empty);
        viewModel.comments.observe(this, list -> {
            commentsList.removeAllViews();
            if (list == null || list.isEmpty()) {
                commentsEmpty.setVisibility(View.VISIBLE);
            } else {
                commentsEmpty.setVisibility(View.GONE);
                for (UserBehavior comment : list) {
                    if (comment.getCommentText() != null && !comment.getCommentText().isEmpty()) {
                        TextView tv = new TextView(DetailActivity.this);
                        tv.setText("💬 " + comment.getCommentText());
                        tv.setTextColor(0xFF555555);
                        tv.setTextSize(13);
                        tv.setPadding(0, 6, 0, 6);
                        commentsList.addView(tv);
                    }
                }
            }
        });

        viewModel.toastMessage.observe(this, toast -> {
            Toast.makeText(DetailActivity.this,toast,Toast.LENGTH_SHORT).show();
        });


    }

    /** 调用AI生成详情分析 */
    private void loadAiAnalysis(AiChatHelper helper, AdItem ad,
                                TextView analysisView, ProgressBar progress) {
        analysisView.setText("AI思考中...");
        progress.setVisibility(View.VISIBLE);

        helper.generateDetailAnalysis(
            ad.getTitle(), ad.getDescription(), ad.getTags(),
            result -> runOnUiThread(() -> {
                progress.setVisibility(View.GONE);
                analysisView.setText(result != null ? result : "AI分析生成失败,请重试");
            }));
    }

    private String getHighlightText(String tag) {
        switch (tag) {
            case "科技": return "搭载行业领先技术，创新科技体验";
            case "新品": return "最新上市，抢先体验前沿产品";
            case "热卖": return "人气爆款，销量持续领先";
            case "性价比": return "同价位段性能标杆，超值之选";
            case "限时优惠": return "限时特价，错过不再有";
            case "品牌正品": return "官方授权，品质有保障，售后无忧";
            case "学生党": return "学生专属福利，价格友好轻松入手";
            case "办公利器": return "提升工作效率，职场达人必备";
            case "数码": return "数码好物，智能生活新体验";
            case "家电": return "品质家电，让生活更舒适便捷";
            case "美妆": return "精致美妆，焕发自然光彩";
            case "食品": return "严选好物，安心品质健康之选";
            case "服饰": return "潮流穿搭，彰显个性风格";
            case "游戏": return "沉浸式体验，畅享游戏乐趣";
            default: return "精选推荐，值得信赖";
        }
    }

    private String formatNumber(int num) {
        if (num >= 10000) return String.format("%.1fw", num / 10000f);
        if (num >= 1000) return String.format("%.1fk", num / 1000f);
        return String.valueOf(num);
    }
    static  class DetailViewModelFactory implements ViewModelProvider.Factory {
        private  final  BehaviorRepository repo;

        DetailViewModelFactory(BehaviorRepository repo) {
            this.repo = repo;
        }
        public <T extends androidx.lifecycle.ViewModel> T create(Class<T> modelClass) {
            return (T) new DetailViewModel(repo);
        }
    }
}
