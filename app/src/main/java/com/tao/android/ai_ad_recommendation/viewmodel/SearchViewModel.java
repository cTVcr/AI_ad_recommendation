package com.tao.android.ai_ad_recommendation.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.tao.android.ai_ad_recommendation.ai.AiService;
import com.tao.android.ai_ad_recommendation.model.AdItem;

import java.util.ArrayList;
import java.util.List;

/**
 * 搜索 ViewModel - 管理对话式搜索的状态
 *
 * 💡 知识点：用户输入自然语言 → AI解析意图 → 匹配广告 → 返回结果
 *         这就是"对话式搜索"的基本流程
 *
 * 🔗 被使用：SearchDialogFragment
 *
 * ====== 框架说明 ======
 * 搜索逻辑需要你来写，包含关键词提取和广告匹配。
 */
public class SearchViewModel extends ViewModel {

    private final AiService aiService;
    private List<AdItem> allAds; // 需要从外部设置（所有可搜索的广告）

    /** 搜索结果列表 */
    public MutableLiveData<List<AdItem>> searchResults = new MutableLiveData<>(new ArrayList<>());

    /** 搜索提示 */
    public MutableLiveData<String> searchHint = new MutableLiveData<>("试试说：想看性价比高的科技产品");

    /** 是否正在搜索 */
    public MutableLiveData<Boolean> isSearching = new MutableLiveData<>(false);

    public SearchViewModel(AiService aiService) {
        this.aiService = aiService;
    }

    /** 设置搜索范围 */
    public void setAllAds(List<AdItem> ads) {
        this.allAds = ads;
    }

    /**
     * 执行对话式搜索
     *
     * TODO: 【你来写-挑战】doSearch()
     *
     * 这是整个项目的亮点功能！思路分3步：
     *
     * 第1步：理解意图（调用AI服务）
     *   String keyword = aiService.semanticSearch(query)
     *   返回的是从用户语句中提取的关键词
     *
     * 第2步：匹配广告
     *   遍历 allAds，在 title/description/tags 中搜索 keyword
     *   匹配到的加入结果列表
     *
     * 第3步：更新UI
     *   searchResults.setValue(匹配结果)
     *
     * 进阶玩法（加分项）：
     *   对匹配结果排序：tags匹配 > title匹配 > description匹配
     *   无结果时给出提示："没有找到相关广告，试试换个说法？"
     *
     * @param query 用户输入的自然语言，如"我想买一台适合打游戏的笔记本"
     */
    // TODO: 【你来写-挑战】
    public void doSearch(String query) {
        // ====== 你的代码从这里开始 ======

        // 1. 用AI服务理解意图

        // 2. 在allAds中匹配

        // 3. 更新结果

        // ====== 你的代码到这里结束 ======
    }

    /**
     * 按标签筛选
     *
     * TODO: 【你来写-中等】filterByTag()
     *
     * 思路：
     * 1. 遍历 allAds
     * 2. 检查 ad.getTags().contains(tag) ← 看广告的标签中是否包含目标标签
     * 3. 匹配的加入结果，更新 searchResults
     *
     * @param tag 标签名，如"科技"、"性价比"
     */
    // TODO: 【你来写-中等】
    public void filterByTag(String tag) {
        // ====== 你的代码 ======
    }
}
