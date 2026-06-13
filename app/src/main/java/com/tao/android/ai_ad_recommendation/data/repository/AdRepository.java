package com.tao.android.ai_ad_recommendation.data.repository;

import android.content.Context;

import com.tao.android.ai_ad_recommendation.data.remote.MockDataSource;
import com.tao.android.ai_ad_recommendation.model.AdItem;

import java.util.List;

/**
 * 广告数据仓库 - 协调数据来源（当前用Mock，未来换API）
 *
 * 💡 知识点：Repository 模式 = 把"数据从哪来"的细节藏起来
 *         ViewModel 不关心数据是Mock还是API，只管调用 repository.getAds()
 *         以后换API只需要改Repository内部，ViewModel不用动 ← 解耦！
 *
 * 🔗 被使用：MainFeedViewModel（唯一调用方）
 *
 * ====== 框架说明 ======
 * 框架已搭好，核心方法需要你来填充逻辑。
 */
public class AdRepository {

    private final MockDataSource mockDataSource;

    public AdRepository(Context context) {
        this.mockDataSource = new MockDataSource(context);
    }

    /**
     * 加载第一页数据（初始化时调用）
     *
     * TODO: 【你来写-简单】loadInitialAds()
     * 1. 调用 mockDataSource.loadMockData() 加载JSON数据
     * 2. 调用 mockDataSource.getPage(0) 获取第一页
     * 3. 返回结果
     */
    // TODO: 【你来写-简单】
    public List<AdItem> loadInitialAds() {
        // ====== 你的代码 ======
        mockDataSource.loadMockData();
        List<AdItem> page0 = mockDataSource.getPage(0);
        return page0;
    }

    /**
     * 加载下一页（上滑加载更多）
     *
     * TODO: 【你来写-简单】loadNextPage()
     * 直接用 mockDataSource.getPage(page) 返回
     */
    // TODO: 【你来写-简单】
    public List<AdItem> loadNextPage(int page) {
        // ====== 你的代码 ======
       return mockDataSource.getPage(page);
    }

    /**
     * 按Tab加载数据
     *
     * TODO: 【你来写-简单】loadAdsByCategory()
     * 用 mockDataSource.getPageByCategory(tab, page)
     */
    // TODO: 【你来写-简单】
    public List<AdItem> loadAdsByCategory(String tab, int page) {
        // ====== 你的代码 ======
        return mockDataSource.getPageByCategory(tab, page);
    }

    /**
     * 刷新数据（下拉刷新）
     *
     * TODO: 【你来写-简单】refreshAds()
     * 用 mockDataSource.getRandomAds(10)
     */
    // TODO: 【你来写-简单】
    public List<AdItem> refreshAds() {
        // ====== 你的代码 ======
        return mockDataSource.getRandomAds(10);

    }

}
