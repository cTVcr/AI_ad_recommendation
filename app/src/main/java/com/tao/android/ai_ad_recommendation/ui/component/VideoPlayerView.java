package com.tao.android.ai_ad_recommendation.ui.component;

import android.content.Context;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.media3.common.MediaItem;
import androidx.media3.common.Player;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.media3.ui.PlayerView;

import com.tao.android.ai_ad_recommendation.R;

/**
 * 视频播放器组件
 *
 * setupVideo(url)   → 创建Player + 加载 + prepare
 * play()/pause()    → 播放控制
 * playMuted()       → 主页静音自动播放
 * setMuted(bool)    → 静音切换
 * showController()  → 详情页控制栏(进度条+声音+全屏)
 */
public class VideoPlayerView extends FrameLayout {

    private PlayerView playerView;
    private ImageView playButton;
    private ExoPlayer player;
    private boolean isMuted = true;

    public VideoPlayerView(Context context) { this(context, null); }
    public VideoPlayerView(Context context, @Nullable AttributeSet attrs) { this(context, attrs, 0); }

    public VideoPlayerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.view_video_player, this, true);
        playerView = findViewById(R.id.player_view);
        playButton = findViewById(R.id.play_button);
        playButton.setOnClickListener(v -> togglePlay());
    }

    public void setupVideo(String url) {
        releasePlayer();
        player = new ExoPlayer.Builder(getContext()).build();
        playerView.setPlayer(player);
        player.setMediaItem(MediaItem.fromUri(Uri.parse(url)));
        player.setVolume(isMuted ? 0f : 1f);
        player.setPlayWhenReady(false);
        player.setRepeatMode(Player.REPEAT_MODE_ALL);
        player.prepare();
    }

    public void play() {
        if (player != null) {
            if (player.getPlaybackState() == Player.STATE_READY) {
                player.play();
                playButton.setVisibility(GONE);
            } else {
                // 还没准备好，等ready再播
                player.addListener(new Player.Listener() {
                    @Override
                    public void onPlaybackStateChanged(int state) {
                        if (state == Player.STATE_READY) {
                            player.play();
                            playButton.setVisibility(GONE);
                            player.removeListener(this);
                        }
                    }
                });
            }
        }
    }

    /** 主页静音自动播放 */
    public void playMuted() {
        if (player != null) player.setVolume(0f);
        isMuted = true;
        play();
    }

    public void pause() {
        if (player != null) {
            player.pause();
            playButton.setVisibility(VISIBLE);
        }
    }

    public void togglePlay() {
        if (player != null && player.isPlaying()) pause(); else play();
    }

    public void setMuted(boolean muted) {
        this.isMuted = muted;
        if (player != null) player.setVolume(muted ? 0f : 1f);
    }

    /** 显示控制栏 */
    public void showController() {
        playerView.setUseController(true);
    }

    /** 是否正在播放 */
    public boolean isPlaying() { return player != null && player.isPlaying(); }

    public void releasePlayer() {
        if (player != null) {
            player.release();
            player = null;
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        releasePlayer();
    }
}