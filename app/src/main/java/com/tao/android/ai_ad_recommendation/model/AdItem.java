package com.tao.android.ai_ad_recommendation.model;

import java.io.Serializable;

/**
 * 广告数据实体
 *
 * 💡 知识点：实现了 Serializable，可以通过Intent在不同Activity间传递
 *
 * 🔗 被使用：MockDataSource(填充数据) / AdFeedAdapter(展示) / DetailActivity(详情)
 */
public class AdItem implements Serializable {

    private String id;

    private String title;

    private String description;

    private String imageUrl;

    private String videoUrl;

    // 💡 知识点：type 决定卡片样式 → large_image(大图) / small_image(小图) / video(视频)
    private String type;

    private String advertiser;

    private String category;  // 广告分类：推荐/关注/热门

    private String summary;   // AI生成的摘要

    private String tags;      // 💡 知识点：用逗号分隔字符串，"科技,性价比,新品"

    private int impressions;  // 曝光次数

    private int clicks;       // 点击次数

    private long timestamp;

    // ====== 构造方法 ======
    public AdItem() {}

    public AdItem(String id, String title, String description, String imageUrl,
                  String videoUrl, String type, String advertiser, String category,
                  String summary, String tags, int impressions, int clicks, long timestamp) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.imageUrl = imageUrl;
        this.videoUrl = videoUrl;
        this.type = type;
        this.advertiser = advertiser;
        this.category = category;
        this.summary = summary;
        this.tags = tags;
        this.impressions = impressions;
        this.clicks = clicks;
        this.timestamp = timestamp;
    }

    // ====== Getter/Setter ======
    public String getId() { return id; }

    public void setId(String id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public String getVideoUrl() { return videoUrl; }
    public void setVideoUrl(String videoUrl) { this.videoUrl = videoUrl; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getAdvertiser() { return advertiser; }
    public void setAdvertiser(String advertiser) { this.advertiser = advertiser; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getSummary() { return summary; }
    public void setSummary(String summary) { this.summary = summary; }

    public String getTags() { return tags; }
    public void setTags(String tags) { this.tags = tags; }

    public int getImpressions() { return impressions; }
    public void setImpressions(int impressions) { this.impressions = impressions; }

    public int getClicks() { return clicks; }
    public void setClicks(int clicks) { this.clicks = clicks; }

    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
}
