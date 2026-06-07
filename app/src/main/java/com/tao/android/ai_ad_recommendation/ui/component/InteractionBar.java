package com.tao.android.ai_ad_recommendation.ui.component;

import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.tao.android.ai_ad_recommendation.R;

/**
 * 互动按钮栏 - 点赞、收藏、评论 三合一
 *
 * 💡 知识点：这个组件的设计思想是"数据驱动UI"
 *         调用 setLiked(true) → 图标变红心 + 数字+1
 *         调用 setLiked(false) → 图标变空心 + 数字-1
 *         组件自己不存业务数据！数据都在ViewModel和数据库里
 *
 * 🔗 复用点：
 *    - AdCardView（每个广告卡片上一个互动栏）
 *    - DetailActivity（详情页的互动栏）
 *
 * ====== 框架说明 ======
 * 【已实现】基本框架。动画效果可以自己调参优化。
 */
public class InteractionBar extends LinearLayout {

    // 点赞
    private ImageView likeIcon;
    private TextView likeCountText;
    private boolean isLiked = false;
    private int likeCount = 0;

    // 收藏
    private ImageView favoriteIcon;
    private TextView favoriteCountText;
    private boolean isFavorited = false;
    private int favoriteCount = 0;

    // 评论
    private ImageView commentIcon;
    private TextView commentCountText;
    private int commentCount = 0;

    // 回调接口
    private OnInteractionListener listener;

    public InteractionBar(Context context) {
        this(context, null);
    }

    public InteractionBar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public InteractionBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.view_interaction_bar, this, true);

        // 绑定控件
        likeIcon = findViewById(R.id.like_icon);
        likeCountText = findViewById(R.id.like_count);
        favoriteIcon = findViewById(R.id.favorite_icon);
        favoriteCountText = findViewById(R.id.favorite_count);
        commentIcon = findViewById(R.id.comment_icon);
        commentCountText = findViewById(R.id.comment_count);

        // 设置点击事件
        likeIcon.setOnClickListener(v -> {
            // TODO: 【你来写-中等】点赞点击
            // 1. 调用 animateHeart() 播放动画
            // 2. 切换 isLiked 状态
            // 3. 更新图标（红心↔空心）
            // 4. 回调 listener.onLikeClick()
        });

        favoriteIcon.setOnClickListener(v -> {
            // TODO: 【你来写-中等】收藏点击
            // 同上逻辑
        });

        commentIcon.setOnClickListener(v -> {
            // TODO: 【你来写-简单】评论点击
            // 回调 listener.onCommentClick()
        });
    }

    /**
     * 设置点赞状态
     *
     * @param liked 是否已点赞
     * @param count 点赞总数
     */
    public void setLiked(boolean liked, int count) {
        this.isLiked = liked;
        this.likeCount = count;
        likeIcon.setImageResource(liked ? R.drawable.ic_heart_filled : R.drawable.ic_heart_outline);
        likeCountText.setText(formatCount(count));
    }

    /** 设置收藏状态 */
    public void setFavorited(boolean favorited, int count) {
        this.isFavorited = favorited;
        this.favoriteCount = count;
        favoriteIcon.setImageResource(favorited ? R.drawable.ic_star_filled : R.drawable.ic_star_outline);
        favoriteCountText.setText(formatCount(count));
    }

    /** 设置评论数 */
    public void setCommentCount(int count) {
        this.commentCount = count;
        commentCountText.setText(formatCount(count));
    }

    /**
     * 点赞动画 - 心形放大再弹回
     *
     * 💡 知识点：属性动画(ValueAnimator)改变View的scaleX/scaleY
     *         OvershootInterpolator = 超过目标值再弹回 ← 弹性效果
     *         这是抖音/小红书同款动画效果！
     */
    private void animateHeart() {
        AnimatorSet set = new AnimatorSet();
        ValueAnimator scaleX = ValueAnimator.ofFloat(1.0f, 1.3f, 1.0f);
        scaleX.addUpdateListener(animation ->
            likeIcon.setScaleX((float) animation.getAnimatedValue()));
        ValueAnimator scaleY = ValueAnimator.ofFloat(1.0f, 1.3f, 1.0f);
        scaleY.addUpdateListener(animation ->
            likeIcon.setScaleY((float) animation.getAnimatedValue()));
        set.playTogether(scaleX, scaleY);
        set.setDuration(300);
        set.setInterpolator(new OvershootInterpolator());
        set.start();
    }

    /**
     * 格式化数字显示（超过1000显示为"1.2k"）
     *
     * 💡 知识点：这是一个常见的UI细节！
     */
    private String formatCount(int count) {
        if (count >= 10000) {
            return String.format("%.1fw", count / 10000.0f);
        } else if (count >= 1000) {
            return String.format("%.1fk", count / 1000.0f);
        }
        return String.valueOf(count);
    }

    // ====== 回调设置 ======
    /** 设置互动事件回调 */
    public void setOnInteractionListener(OnInteractionListener listener) {
        this.listener = listener;
    }

    /**
     * 互动事件回调接口
     */
    public interface OnInteractionListener {
        void onLikeClick();
        void onFavoriteClick();
        void onCommentClick();
    }
}
