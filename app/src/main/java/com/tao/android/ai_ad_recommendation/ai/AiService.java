package com.tao.android.ai_ad_recommendation.ai;

import com.tao.android.ai_ad_recommendation.model.AiResult;

/**
 * AI服务接口 - 定义AI功能的抽象契约
 *
 * 💡 知识点：面向接口编程
 *         当前用 MockAiService（假数据），未来换成 QwenAiService（真API）
 *         换实现时只需要 new QwenAiService() 替代 new MockAiService()
 *         调用方代码完全不用改！
 *
 * 🔗 实现类：MockAiService (当前), QwenAiService (未来)
 *
 * ====== 框架说明 ======
 * 【已实现】接口只定义方法签名，具体实现在 MockAiService 中。
 */
public interface AiService {

    /**
     * 为广告生成AI摘要
     * @param adId 广告ID
     * @param adDescription 广告原始描述
     * @return AI生成的摘要结果
     */
    AiResult generateSummary(String adId, String adDescription);

    /**
     * 为广告生成标签
     * @param adId 广告ID
     * @param adTitle 广告标题
     * @param adDescription 广告描述
     * @return 带标签的AI结果
     */
    AiResult generateTags(String adId, String adTitle, String adDescription);

    /**
     * 对话式搜索
     * @param query 用户自然语言输入，如"我想看性价比高的科技产品"
     * @return 语义匹配结果
     */
    String semanticSearch(String query);
}
