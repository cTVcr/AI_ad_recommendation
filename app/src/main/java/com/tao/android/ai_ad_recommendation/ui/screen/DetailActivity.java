package com.tao.android.ai_ad_recommendation.ui.screen;

import android.os.Bundle;

import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.tao.android.ai_ad_recommendation.AiAdApplication;
import com.tao.android.ai_ad_recommendation.R;
import com.tao.android.ai_ad_recommendation.data.repository.BehaviorRepository;
import com.tao.android.ai_ad_recommendation.model.AdItem;
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

        // ====== 第3步：绑定控件 + 设置数据 ======
        // TODO: 【你来写-中等】setupUI()
        //
        // 思路：
        // 1. findViewById 获取各种控件
        // 2. 根据 adItem.getType() 决定显示 ImageView 还是 VideoPlayerView
        // 3. 如果是图片：用 Glide 加载大图
        // 4. 如果是视频：初始化 ExoPlayer，设置视频URL
        // 5. 设置标题、描述、广告主等信息

        // 顶部色块 + 叠加标题
        TextView bannerText = findViewById(R.id.detail_banner_text);
        ImageView imageView = findViewById(R.id.detail_image);
        int color = 0xFFA8D8EA;
        switch (adItem.getCategory()) {
            case "推荐": color = 0xFFA8D8EA; break;
            case "关注": color = 0xFFFFC8C3; break;
            case "热门": color = 0xFFFFE0C8; break;
        }
        imageView.setBackgroundColor(color);
        imageView.setImageDrawable(null);
        bannerText.setText(adItem.getTitle());

        // 信息卡片
        TextView titleView = findViewById(R.id.detail_title);
        titleView.setText(adItem.getTitle());

        TextView descView = findViewById(R.id.detail_description);
        descView.setText(adItem.getDescription());

        TextView advertiserView = findViewById(R.id.detail_advertiser);
        advertiserView.setText(adItem.getAdvertiser());

        // AI 深度分析（根据分类和标签生成不同内容）
        TextView aiAnalysis = findViewById(R.id.detail_ai_analysis);
        aiAnalysis.setText("根据深度分析，" + adItem.getTitle() + " 当前在"
                + adItem.getCategory() + "频道表现优异。\n\n"
                + "• 目标受众：对" + adItem.getTags().replace(",", "、") + "感兴趣的用户群体\n"
                + "• 推荐理由：" + adItem.getSummary() + "\n"
                + "• 广告热度：已有 " + adItem.getImpressions() + " 次曝光，"
                + adItem.getClicks() + " 次点击\n"
                + "• 建议：该广告内容质量较高，建议增加投放预算以获得更多曝光");

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

        // 数据统计
        TextView impressionsView = findViewById(R.id.detail_stat_impressions);
        impressionsView.setText(formatNumber(adItem.getImpressions()));

        TextView clicksView = findViewById(R.id.detail_stat_clicks);
        clicksView.setText(formatNumber(adItem.getClicks()));

        TextView rateView = findViewById(R.id.detail_stat_rate);
        if (adItem.getImpressions() > 0) {
            float rate = (float) adItem.getClicks() / adItem.getImpressions() * 100;
            rateView.setText(String.format("%.1f%%", rate));
        }

        // ====== 第4步：设置互动按钮 ======
        // TODO: 【你来写-中等】setupInteraction()
        //
        // 思路：
        // 1. 点赞按钮点击 → viewModel.toggleLike()
        // 2. 收藏按钮点击 → viewModel.toggleFavorite()
        // 3. 评论按钮点击 → 弹出评论DialogFragment
        // 4. 观察 viewModel.isLiked / isFavorited 更新按钮状态
        // 5. 观察 viewModel.likeCount / favoriteCount 更新数字

        // ====== 第5步：观察Toast提示 ======
        // viewModel.toastMessage.observe(this, msg ->
        //     Toast.makeText(this, msg, Toast.LENGTH_SHORT).show());


    }

    /** 返回时保存浏览位置信息 */
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
}
