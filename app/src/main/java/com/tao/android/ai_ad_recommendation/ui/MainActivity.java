package com.tao.android.ai_ad_recommendation.ui;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.tao.android.ai_ad_recommendation.R;

/**
 * 主Activity - 单Activity架构的唯一入口
 *
 * 💡 知识点：现代Android推荐"单Activity多Fragment"架构
 *         一个Activity + 多个Fragment切换，比多个Activity跳转更流畅
 *
 * 🔗 包含：MainFeedFragment（信息流页面）
 *
 * ====== 框架说明 ======
 * 【已实现】只需要加载布局，Fragment通过XML中的<fragment>标签自动挂载。
 * 如果后续加入导航，这里改为NavHostFragment。
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 💡 知识点：Fragment通过XML的 <fragment> 标签自动加载，
        //         不需要手动 new Fragment + FragmentTransaction
    }
}
