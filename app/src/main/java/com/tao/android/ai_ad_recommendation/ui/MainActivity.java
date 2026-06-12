package com.tao.android.ai_ad_recommendation.ui;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import com.tao.android.ai_ad_recommendation.R;

/**
 * 主Activity — 单Activity架构入口，Fragment通过XML自动挂载
 */
public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
