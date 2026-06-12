package com.tao.android.ai_ad_recommendation.ui.screen;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.tao.android.ai_ad_recommendation.R;
import com.tao.android.ai_ad_recommendation.ui.MainActivity;

/**
 * 启动页 — "让广告更懂你" 六个字逐字弹跳入场
 *
 * 💡 知识点：ObjectAnimator + OvershootInterpolator + AnimatorSet
 *         每个字依次跳起落下，弹跳感来自 OvershootInterpolator
 *         Handler.postDelayed 实现逐字延迟触发
 */
public class SplashActivity extends AppCompatActivity {

    // 六个字的 id 数组
    private static final int[] LETTER_IDS = {
        R.id.tv_letter_0, R.id.tv_letter_1, R.id.tv_letter_2,
        R.id.tv_letter_3, R.id.tv_letter_4, R.id.tv_letter_5
    };

    private static final int INITIAL_PAUSE = 400;    // 初始停顿
    private static final int STAGGER_DELAY = 160;    // 字与字间隔
    private static final int BOUNCE_DURATION = 600;  // 单字跳动时长

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        // 依次触发每个字的跳动
        for (int i = 0; i < LETTER_IDS.length; i++) {
            final TextView letterView = findViewById(LETTER_IDS[i]);
            int delay = INITIAL_PAUSE + i * STAGGER_DELAY;
            new Handler().postDelayed(() -> bounceLetter(letterView), delay);
        }

        // 动画结束后跳转到主界面
        int totalDelay = INITIAL_PAUSE + (LETTER_IDS.length - 1) * STAGGER_DELAY + BOUNCE_DURATION + 500;
        new Handler().postDelayed(() -> {
            startActivity(new Intent(SplashActivity.this, MainActivity.class));
            finish();
        }, totalDelay);
    }

    /**
     * 单字弹跳动画：上跳 + 放大 + 落回
     * 💡 OvershootInterpolator(1.5f) — 超过目标再弹回，像弹簧
     */
    private void bounceLetter(View view) {
        ObjectAnimator upDown = ObjectAnimator.ofFloat(view, "translationY", 0f, -45f, 0f);
        upDown.setDuration(BOUNCE_DURATION);
        upDown.setInterpolator(new OvershootInterpolator(1.5f));

        ObjectAnimator scaleX = ObjectAnimator.ofFloat(view, "scaleX", 1f, 1.3f, 1f);
        scaleX.setDuration(BOUNCE_DURATION);

        ObjectAnimator scaleY = ObjectAnimator.ofFloat(view, "scaleY", 1f, 1.3f, 1f);
        scaleY.setDuration(BOUNCE_DURATION);

        AnimatorSet set = new AnimatorSet();
        set.playTogether(upDown, scaleX, scaleY);
        set.start();
    }
}
