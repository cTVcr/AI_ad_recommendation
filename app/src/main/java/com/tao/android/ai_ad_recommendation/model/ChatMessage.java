package com.tao.android.ai_ad_recommendation.model;

import java.io.Serializable;
import java.util.List;

/** 一条聊天消息 */
public class ChatMessage implements Serializable {
    public static final String ROLE_USER = "user";
    public static final String ROLE_AI = "ai";

    private String role;              // user | ai
    private String text;              // 对话文字
    private List<AdItem> adResults;   // AI回复里带的广告(null=纯文字)
    private long timestamp;

    public ChatMessage(String role, String text, List<AdItem> adResults) {
        this.role = role;
        this.text = text;
        this.adResults = adResults;
        this.timestamp = System.currentTimeMillis();
    }

    public String getRole() { return role; }
    public String getText() { return text; }
    public List<AdItem> getAdResults() { return adResults; }
    public long getTimestamp() { return timestamp; }
}
