package com.tao.android.ai_ad_recommendation.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * 用户行为实体 (Room数据库表 user_behaviors)
 * 记录：点赞、收藏、评论、曝光、点击
 *
 * 💡 知识点：行为类型用 behaviorType 字段区分：
 *         "like"=点赞, "favorite"=收藏, "comment"=评论
 *         "impression"=曝光, "click"=点击
 *
 * 🔗 被使用：BehaviorDao / BehaviorRepository / InteractionBar
 */
@Entity(tableName = "user_behaviors")
public class UserBehavior {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private long id;

    @ColumnInfo(name = "ad_id")
    private String adId;          // 关联的广告ID

    @ColumnInfo(name = "behavior_type")
    private String behaviorType;   // like / favorite / comment / impression / click

    @ColumnInfo(name = "comment_text")
    private String commentText;    // 评论内容（仅 behaviorType=comment 时有值）

    @ColumnInfo(name = "timestamp")
    private long timestamp;

    // ====== 构造方法 ======
    public UserBehavior() {}

    public UserBehavior(String adId, String behaviorType, String commentText, long timestamp) {
        this.adId = adId;
        this.behaviorType = behaviorType;
        this.commentText = commentText;
        this.timestamp = timestamp;
    }

    // ====== Getter/Setter ======
    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public String getAdId() { return adId; }
    public void setAdId(String adId) { this.adId = adId; }

    public String getBehaviorType() { return behaviorType; }
    public void setBehaviorType(String behaviorType) { this.behaviorType = behaviorType; }

    public String getCommentText() { return commentText; }
    public void setCommentText(String commentText) { this.commentText = commentText; }

    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
}
