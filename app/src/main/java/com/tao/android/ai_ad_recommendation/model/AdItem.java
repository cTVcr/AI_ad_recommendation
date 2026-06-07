package com.tao.android.ai_ad_recommendation.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.Serializable;

/**
 * 广告数据实体 (Room数据库表 ad_items)
 *
 * 💡 知识点：Room @Entity 注解把这个类映射为SQLite表
 *         @PrimaryKey 标记主键, @ColumnInfo 自定义列名
 *
 * 🔗 被使用：AdDao(CURD操作) / MockDataSource(填充数据) / AdCardView(展示)
 */
@Entity(tableName = "ad_items")
public class AdItem implements Serializable {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "id")
    private String id;

    @ColumnInfo(name = "title")
    private String title;

    @ColumnInfo(name = "description")
    private String description;

    @ColumnInfo(name = "image_url")
    private String imageUrl;

    @ColumnInfo(name = "video_url")
    private String videoUrl;

    // 💡 知识点：type 决定卡片样式 → large_image(大图) / small_image(小图) / video(视频)
    @ColumnInfo(name = "type")
    private String type;

    @ColumnInfo(name = "advertiser")
    private String advertiser;

    @ColumnInfo(name = "category")
    private String category;  // 广告分类：推荐/关注/热门

    @ColumnInfo(name = "summary")
    private String summary;   // AI生成的摘要

    @ColumnInfo(name = "tags")
    private String tags;      // 💡 知识点：Room不直接存List，用逗号分隔字符串，"科技,性价比,新品"

    @ColumnInfo(name = "impressions")
    private int impressions;  // 曝光次数

    @ColumnInfo(name = "clicks")
    private int clicks;       // 点击次数

    @ColumnInfo(name = "timestamp")
    private long timestamp;

    // ====== 构造方法 (Room需要空构造+全参构造) ======
    public AdItem() {}

    @Ignore
    public AdItem(@NonNull String id, String title, String description, String imageUrl,
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

    // ====== Getter/Setter (Room通过反射调用，必须有) ======
    @NonNull
    public String getId() { return id; }

    public void setId(@NonNull String id) { this.id = id; }

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
