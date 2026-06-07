package com.tao.android.ai_ad_recommendation.ui;

import android.os.Bundle;

import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;

import com.tao.android.ai_ad_recommendation.R;
import com.tao.android.ai_ad_recommendation.data.remote.MockDataSource;
import com.tao.android.ai_ad_recommendation.data.repository.AdRepository;
import com.tao.android.ai_ad_recommendation.model.AdItem;

import java.util.List;

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

//        MockDataSource ds = new MockDataSource(this);
//        ds.loadMockData();
//        Log.d("MyAPP", ds.getTotalCount()+"条数据");
//        List<AdItem> page0 = ds.getPage(0);
//
//        Log.d("MYAPP", "第0页: " + page0.size() + "条, 第1条标题=" +
//                page0.get(0).getTitle());
//
//        List<AdItem> page1 = ds.getPage(1);
//        Log.d("MYAPP", "第1页: " + page1.size() + "条");
//
//        List<AdItem> page2 = ds.getPage(2);
//        Log.d("MYAPP", "第2页: " + page2.size() + "条 (应为0)");


        AdRepository repo = new AdRepository(this);
        List<AdItem> list = repo.loadInitialAds();
        Log.d("MYAPP", "Repository加载: " + list.size() + "条, 第1条: "
                + list.get(0).getTitle());

        List<AdItem> page2 = repo.loadNextPage(1);
        Log.d("MYAPP", "下一页: " + page2.size() + "条");

        List<AdItem> hot = repo.loadAdsByCategory("热门", 0);
        Log.d("MYAPP", "热门: " + hot.size() + "条");
        // 💡 知识点：Fragment通过XML的 <fragment> 标签自动加载，
        //         不需要手动 new Fragment + FragmentTransaction
    }
}
