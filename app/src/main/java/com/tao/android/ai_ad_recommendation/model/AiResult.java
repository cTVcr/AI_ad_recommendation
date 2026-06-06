package com.tao.android.ai_ad_recommendation.model;

import java.util.List;

/**
 * AI生成结果 (普通POJO，不存数据库)
 *
 * 💡 知识点：这个是临时数据，不需要 @Entity 注解
 *         只在AI服务返回结果时使用
 *
 * 🔗 被使用：AiService / MockAiService / AdCardView (展示摘要和标签)
 */
public class AiResult {

    private String adId;
    private String summary;        // AI生成的广告摘要（一句吸引人的话）
    private List<String> tags;     // AI生成的标签列表 如 ["科技", "新品", "性价比"]
    private long generatedAt;

    public AiResult() {}

    public AiResult(String adId, String summary, List<String> tags, long generatedAt) {
        this.adId = adId;
        this.summary = summary;
        this.tags = tags;
        this.generatedAt = generatedAt;
    }

    public String getAdId() { return adId; }
    public void setAdId(String adId) { this.adId = adId; }

    public String getSummary() { return summary; }
    public void setSummary(String summary) { this.summary = summary; }

    public List<String> getTags() { return tags; }
    public void setTags(List<String> tags) { this.tags = tags; }

    public long getGeneratedAt() { return generatedAt; }
    public void setGeneratedAt(long generatedAt) { this.generatedAt = generatedAt; }
}
