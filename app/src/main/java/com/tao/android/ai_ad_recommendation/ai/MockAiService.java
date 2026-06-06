package com.tao.android.ai_ad_recommendation.ai;

import com.tao.android.ai_ad_recommendation.model.AdItem;
import com.tao.android.ai_ad_recommendation.model.AiResult;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Mock AI服务实现 - 返回预设的摘要和标签
 *
 * 💡 知识点：Mock的意义 = 在没有真实AI接口时，先跑通整个流程
 *         等Qwen API申请好之后，写一个 QwenAiService implements AiService
 *         替换掉这里即可，其他代码全部不需要改
 *
 * 🔗 被使用：MainFeedViewModel（调用生成摘要/标签）
 *
 * ====== 框架说明 ======
 * 框架已搭好，需要你来写摘要和标签的匹配逻辑。
 */
public class MockAiService implements AiService {

    // 预设的摘要模板
    private static final List<String> SUMMARY_TEMPLATES = Arrays.asList(
        "🔥 限时优惠！这款产品正在热卖中，点击了解详情",
        "⭐ 好评如潮！用户真实评价超过1000条",
        "🆕 新品首发！抢先体验最新科技",
        "💎 品质之选！做工精良，性价比超高",
        "🎯 精准匹配！根据你的喜好为你推荐",
        "🏆 销量冠军！本月已售10000+件",
        "📦 限时包邮！现在下单立享优惠",
        "✅ 官方正品！品质保证，假一赔十"
    );

    // 预设标签库
    private static final List<String> TAG_LIBRARY = Arrays.asList(
        "科技", "美妆", "食品", "游戏", "服饰", "数码", "家电",
        "新品", "热卖", "性价比", "限时优惠", "品牌正品", "学生党", "办公利器"
    );

    @Override
    public AiResult generateSummary(String adId, String adDescription) {
        // TODO: 【你来写-中等】generateSummary()
        //
        // 思路：
        // 1. 用 adId.hashCode() % SUMMARY_TEMPLATES.size() 选一个模板摘要
        //    这样同一个adId每次返回相同摘要（有"缓存"效果）
        // 2. 创建 AiResult 对象返回
        //
        // ⚠️ 注意：Math.abs(hashCode % size) 防止负数索引
        //
        // ====== 你的代码 ======
        return null;
    }

    @Override
    public AiResult generateTags(String adId, String adTitle, String adDescription) {
        // TODO: 【你来写-中等】generateTags()
        //
        // 思路：
        // 1. 用 adId.hashCode() 作为随机种子，确保同一广告每次返回相同标签
        // 2. 从 TAG_LIBRARY 中随机选2-3个标签
        // 3. 可以玩点花样：adTitle中包含"科技"就加"科技"标签
        //    adDescription中包含"优惠"就加"限时优惠"标签
        //    ← 这就是最简单的"语义理解"！
        //
        // 💡 知识点：模拟AI打标签的核心 = 关键词匹配
        //         真AI会用NLP模型理解语义，但基本思路一样
        //
        // ====== 你的代码 ======
        return null;
    }

    @Override
    public String semanticSearch(String query) {
        // TODO: 【你来写-挑战】semanticSearch()
        //
        // 这是"对话式搜索"的核心：
        // 用户输入自然语言 → 返回匹配的广告ID或关键词
        //
        // 思路（Mock版）：
        // 1. 把query中的常见词提取出来（分詞）
        //    比如"我想看性价比高的科技产品" → ["性价比","科技"]
        // 2. 在 TAG_LIBRARY 和 SUMMARY_TEMPLATES 中匹配
        // 3. 返回匹配到的关键词，由ViewModel用这些关键词过滤广告
        //
        // 进阶玩法：预设一些意图模板
        // "便宜" "实惠" "学生" → 返回"性价比"
        // "最新" "新出" "首发" → 返回"新品"
        // "质量好" "正品" "正版" → 返回"品牌正品"
        //
        // 💡 这就是最原始的"意图识别"！真AI用的也是类似思路，只是更复杂
        //
        // ====== 你的代码 ======
        return "";
    }
}
