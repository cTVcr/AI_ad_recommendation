package com.tao.android.ai_ad_recommendation.ui.component;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.tao.android.ai_ad_recommendation.R;
import com.tao.android.ai_ad_recommendation.model.AdItem;
import com.tao.android.ai_ad_recommendation.ui.screen.DetailActivity;
import com.tao.android.ai_ad_recommendation.util.Constants;

/**
 * 广告卡片 View - 支持三种样式切换（大图/小图/视频）
 *
 * 💡 知识点：这是一个"三合一"的复用组件
 *         根据传入的AdItem.type自动切换布局：
 *         - large_image → 大图卡片（图片占60%+文字40%）
 *         - small_image → 小图卡片（图片40%左+文字60%右）
 *         - video → 视频卡片（ExoPlayer+文字叠加层）
 *
 * 🔗 复用点：
 *    - MainFeedFragment (RecyclerView每个item)
 *    - SearchDialogFragment (搜索结果，用小图模式)
 *
 * ====== 框架说明 ======
 * 框架已搭建。bindData/switchStyle/setupClick需要你来写。
 */
public class AdCardView extends FrameLayout {

    private AdItem adItem;

    // 各种UI控件（三种样式共享）
    private ImageView mainImageView;
    private TextView titleView;
    private TextView descriptionView;
    private TextView advertiserView;
    private VideoPlayerView videoPlayerView;  // 复用！

    // 标签容器和互动栏
    private android.widget.LinearLayout tagsContainer;
    private InteractionBar interactionBar;  // 复用！

    public AdCardView(Context context) {
        this(context, null);
    }

    public AdCardView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AdCardView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        // 默认加载大图样式布局
        LayoutInflater.from(context).inflate(R.layout.view_ad_card, this, true);

        // findViewById 绑定控件
        mainImageView = findViewById(R.id.card_image);
        titleView = findViewById(R.id.card_title);
        descriptionView = findViewById(R.id.card_description);
        advertiserView = findViewById(R.id.card_advertiser);
        videoPlayerView = findViewById(R.id.card_video_player);
        tagsContainer = findViewById(R.id.card_tags_container);
        interactionBar = findViewById(R.id.card_interaction_bar);
    }

    /**
     * 绑定广告数据到卡片
     *
     * TODO: 【你来写-中等】bindData()
     *
     * 思路：
     * 1. 保存 adItem 引用
     * 2. 设置标题、描述、广告主名称
     * 3. 根据 adItem.getType() 切换卡片样式
     * 4. 使用 Glide 加载图片到 mainImageView
     *    💡 知识点：Glide.with(getContext()).load(item.getImageUrl()).into(mainImageView)
     * 5. 给标签容器添加 TagChipView
     *    String[] tags = item.getTags().split(",")
     *    for each tag: TagChipView chip = new TagChipView(getContext()); chip.setTagText(tag);
     *    tagsContainer.addView(chip)
     * 6. 设置互动栏数据
     *
     * @param item 广告数据
     */
    // TODO: 【你来写-中等】
    public void bindData(AdItem item) {
        this.adItem = item;
        // ====== 你的代码从这里开始 ======

        // 1. 设置基础文本

        // 2. 切换样式

        // 3. 加载图片(Glide)

        // 4. 动态添加标签chips

        // 5. 设置互动栏

        // ====== 你的代码到这里结束 ======
    }

    /**
     * 根据广告类型切换卡片样式
     *
     * TODO: 【你来写-中等】switchStyle()
     *
     * 思路：
     * - TYPE_LARGE_IMAGE: 显示大图+文字，隐藏视频播放器
     *   mainImageView.setVisibility(VISIBLE)
     *   videoPlayerView.setVisibility(GONE)
     *   调整 mainImageView 的高度为屏幕60%
     *
     * - TYPE_SMALL_IMAGE: 图片和文字左右排列
     *   调整 mainImageView 宽度为40%
     *   文字区域宽度为60%
     *
     * - TYPE_VIDEO: 显示视频播放器+控制按钮
     *   mainImageView.setVisibility(GONE)
     *   videoPlayerView.setVisibility(VISIBLE)
     *   点击后进入全屏详情页播放
     *
     * 💡 知识点：用 setVisibility + LayoutParams 控制布局，
     *         不需要三个独立的布局文件！
     *
     * @param type 广告类型
     */
    // TODO: 【你来写-中等】
    private void switchStyle(String type) {
        // ====== 你的代码从这里开始 ======

        // ====== 你的代码到这里结束 ======
    }

    /**
     * 设置点击事件（跳转详情页）
     *
     * TODO: 【你来写-简单】setupClick()
     *
     * 思路：
     * setOnClickListener(v -> {
     *     Intent intent = new Intent(getContext(), DetailActivity.class);
     *     intent.putExtra("ad_item", adItem);  // AdItem 实现了Serializable
     *     getContext().startActivity(intent);
     * })
     */
    // TODO: 【你来写-简单】
    private void setupClick() {
        // ====== 你的代码 ======

    }

    /** 获取当前广告数据 */
    public AdItem getAdItem() {
        return adItem;
    }
}
