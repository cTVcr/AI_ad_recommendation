package com.tao.android.ai_ad_recommendation.ui.component;

import android.content.Context;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.media3.common.MediaItem;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.media3.ui.PlayerView;

import com.tao.android.ai_ad_recommendation.R;

/**
 * 视频播放器组件 - 对 ExoPlayer 的封装
 *
 * 💡 知识点：封装第三方库 = 把复杂API藏起来，暴露简单方法
 *         外部只需要调用 setupVideo(url) 和 play()/pause()
 *         不需要知道内部用的是 ExoPlayer
 *
 * 🔗 复用点：
 *    - AdCardView（列表中的视频卡片）
 *    - DetailActivity（详情页全屏播放）
 *
 * ====== 框架说明 ======
 * 框架已搭建。ExoPlayer的初始化和播放控制需要你来写。
 *
 * ⚠️ 视频播放注意事项：
 *    1. 列表中默认静音、不自动播放（避免打扰用户）
 *    2. 同一时间只有一个播放器在播放
 *    3. 离开屏幕时释放资源（onDetachedFromWindow）
 */
public class VideoPlayerView extends FrameLayout {

    private PlayerView playerView;
    private ImageView playButton;  // 中央播放按钮（点击后开始播放）
    private ExoPlayer player;
    private String videoUrl;
    private boolean isPlaying = false;

    public VideoPlayerView(Context context) {
        this(context, null);
    }

    public VideoPlayerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VideoPlayerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.view_video_player, this, true);

        playerView = findViewById(R.id.player_view);
        playButton = findViewById(R.id.play_button);

        // 点击后开始/暂停播放
        playButton.setOnClickListener(v -> {
            if (isPlaying) {
                pause();
            } else {
                play();
            }
        });
    }

    /**
     * 初始化视频
     *
     * TODO: 【你来写-挑战】setupVideo()
     *
     * 思路：
     * 1. 创建 ExoPlayer: player = new ExoPlayer.Builder(getContext()).build()
     * 2. playerView.setPlayer(player)  // 绑定到PlayerView
     * 3. 创建 MediaItem: MediaItem.fromUri(Uri.parse(url))
     * 4. player.setMediaItem(mediaItem)
     * 5. player.prepare()  // 准备播放
     * 6. player.setPlayWhenReady(false)  // 默认不自动播放
     *
     * 💡 知识点：ExoPlayer是Google官方推荐的视频播放器
     *         比旧版MediaPlayer更稳定、功能更强
     *         学习资源：https://developer.android.com/media/media3/exoplayer
     *
     * @param url 视频地址
     */
    // TODO: 【你来写-挑战】
    public void setupVideo(String url) {
        this.videoUrl = url;
        // ====== 你的代码从这里开始 ======

        // ====== 你的代码到这里结束 ======
    }

    /** 开始播放 */
    public void play() {
        if (player != null) {
            player.play();
            isPlaying = true;
            playButton.setVisibility(GONE);  // 播放时隐藏播放按钮
        }
    }

    /** 暂停播放 */
    public void pause() {
        if (player != null) {
            player.pause();
            isPlaying = false;
            playButton.setVisibility(VISIBLE);  // 暂停时显示播放按钮
        }
    }

    /** 切换播放/暂停 */
    public void togglePlay() {
        if (isPlaying) pause(); else play();
    }

    /**
     * 释放播放器资源
     *
     * ⚠️ 非常重要！不释放会导致内存泄漏和电池消耗
     * 在 Fragment/Activity 的 onPause/onStop 中调用
     */
    public void releasePlayer() {
        if (player != null) {
            player.release();
            player = null;
        }
    }

    /**
     * 当View从窗口分离时自动释放资源
     *
     * 💡 知识点：onDetachedFromWindow 是一个生命周期回调
     *         View被移除时自动调用，适合做清理工作
     */
    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        releasePlayer();
    }
}
