package com.tao.android.ai_ad_recommendation.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.tao.android.ai_ad_recommendation.data.repository.AdRepository;
import com.tao.android.ai_ad_recommendation.model.AdItem;

import java.util.ArrayList;
import java.util.List;

/**
 * 主信息流 ViewModel - 管理信息流的所有状态
 *
 * 💡 知识点：ViewModel 的生命周期比 Activity/Fragment 长
 *         屏幕旋转不会销毁ViewModel，数据不会丢失！
 *         ← 这解决了课件二中 onSaveInstanceState 的痛点
 *
 * 🔗 被使用：MainFeedFragment（观察LiveData，驱动UI刷新）
 *
 * ====== 框架说明 ======
 * 核心方法 loadFirstPage/loadNextPage/switchTab/refresh 需要你来写。
 * 状态变量已定义好，直接使用即可。
 */
public class MainFeedViewModel extends ViewModel {

    private final AdRepository repository;

    // ====== 状态变量（UI观察这些LiveData） ======

    /** 当前显示的广告列表 */
    public MutableLiveData<List<AdItem>> adList = new MutableLiveData<>(new ArrayList<>());

    /** 当前页码 */
    public MutableLiveData<Integer> currentPage = new MutableLiveData<>(0);

    /** 当前选中的Tab */
    public MutableLiveData<String> currentTab = new MutableLiveData<>("推荐");

    /** 是否正在加载（控制loading动画的显示） */
    public MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);

    /** 是否还有更多数据（控制"加载更多"的显示） */
    public MutableLiveData<Boolean> hasMore = new MutableLiveData<>(true);

    /** 是否正在刷新（下拉刷新的动画） */
    public MutableLiveData<Boolean> isRefreshing = new MutableLiveData<>(false);

    public MainFeedViewModel(AdRepository repository) {
        this.repository = repository;
    }

    /**
     * 加载第一页数据
     *
     * TODO: 【你来写-中等】loadFirstPage()
     *
     * 思路：
     * 1. isLoading.setValue(true)  // 开始加载
     * 2. List<AdItem> data = repository.loadInitialAds()
     * 3. adList.setValue(data)     // 更新列表
     * 4. currentPage.setValue(0)   // 重置页码
     * 5. hasMore.setValue(判断是否还有更多)
     * 6. isLoading.setValue(false) // 加载完成
     *
     * ⚠️ 注意：这里暂时在主线程调用（因为Mock数据是本地读取），
     *         之后接真实API时需要在后台线程执行
     */
    // TODO: 【你来写-中等】
    public void loadFirstPage() {
        // ====== 你的代码从这里开始 ======

        // ====== 你的代码到这里结束 ======
    }

    /**
     * 加载下一页（上滑加载更多）
     *
     * TODO: 【你来写-中等】loadNextPage()
     *
     * 思路：
     * 1. 检查 isLoading 和 hasMore，如果正在加载或没更多数据，直接return
     * 2. int nextPage = currentPage.getValue() + 1
     * 3. 根据当前Tab选择调用 repository.loadNextPage(nextPage) 或 loadAdsByCategory()
     * 4. 把新数据和旧数据合并：创建新的List，先addAll(旧数据)，再addAll(新数据)
     * 5. adList.setValue(合并后的列表)
     * 6. currentPage.setValue(nextPage)
     */
    // TODO: 【你来写-中等】
    public void loadNextPage() {
        // ====== 你的代码从这里开始 ======

        // ====== 你的代码到这里结束 ======
    }

    /**
     * 切换Tab
     *
     * TODO: 【你来写-中等】switchTab()
     *
     * 思路：
     * 1. currentTab.setValue(tab)
     * 2. 如果 tab == "推荐" → 调用 repository.loadInitialAds()（加载全部推荐）
     *    否则 → 调用 repository.loadAdsByCategory(tab, 0)（加载分类数据）
     * 3. 更新 adList, currentPage=0, hasMore
     *
     * @param tab Tab名称（"推荐"/"关注"/"热门"）
     */
    // TODO: 【你来写-中等】
    public void switchTab(String tab) {
        // ====== 你的代码从这里开始 ======

        // ====== 你的代码到这里结束 ======
    }

    /**
     * 下拉刷新
     *
     * TODO: 【你来写-中等】refresh()
     *
     * 思路：
     * 1. isRefreshing.setValue(true)
     * 2. List<AdItem> newData = repository.refreshAds()
     * 3. 如果newData不为空，adList.setValue(newData)，currentPage=0
     * 4. isRefreshing.setValue(false)
     */
    // TODO: 【你来写-中等】
    public void refresh() {
        // ====== 你的代码从这里开始 ======

        // ====== 你的代码到这里结束 ======
    }
}
