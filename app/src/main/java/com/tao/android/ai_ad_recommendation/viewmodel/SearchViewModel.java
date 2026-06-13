package com.tao.android.ai_ad_recommendation.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.tao.android.ai_ad_recommendation.ai.AiChatHelper;
import com.tao.android.ai_ad_recommendation.model.AdItem;
import com.tao.android.ai_ad_recommendation.model.ChatMessage;

import java.util.ArrayList;
import java.util.List;

/**
 * 聊天对话 ViewModel — AI直接分析广告库，返回匹配的广告ID
 */
public class SearchViewModel extends ViewModel {

    private List<AdItem> allAds;
    private final AiChatHelper aiHelper = new AiChatHelper();
    private final List<String> history = new ArrayList<>();

    public MutableLiveData<List<ChatMessage>> chatHistory = new MutableLiveData<>(new ArrayList<>());
    public MutableLiveData<Boolean> thinking = new MutableLiveData<>(false);

    public void init(List<AdItem> ads) {
        this.allAds = ads;
        List<ChatMessage> msgs = new ArrayList<>();
        msgs.add(new ChatMessage(ChatMessage.ROLE_AI,
            "你好！我是AI广告助手，帮你从"
                    + (ads != null ? ads.size() : 0)
                    + "条广告里找到最合适的。\n\n直接告诉我想看什么吧，比如:\n• 有没有拍照好的手机？\n• 推荐适合学生的好物\n• 最近有什么新出的游戏？", null));
        chatHistory.setValue(msgs);
    }

    /** 用户发消息 → AI直接从广告库选出匹配的 → 展示 */
    public void sendMessage(String userText) {
        if (allAds == null || userText == null || userText.trim().isEmpty()) return;

        thinking.setValue(true);
        String query = userText.trim();

        // AI直接分析全量广告 → 返回广告ID + 回复文字
        aiHelper.analyzeAndMatch(query, history, allAds, (adIds, reply) -> {
            List<AdItem> matched = matchByIds(adIds);

            String finalReply = reply;
            if (matched.isEmpty()) {
                finalReply = "抱歉呀，没找到特别匹配的广告~试试问: 推荐手机 / 笔记本 / 学生党的好物?";
            }

            history.add(query);
            history.add(finalReply);

            List<ChatMessage> msgs = new ArrayList<>(chatHistory.getValue());
            msgs.add(new ChatMessage(ChatMessage.ROLE_USER, query, null));
            msgs.add(new ChatMessage(ChatMessage.ROLE_AI, finalReply,
                matched.isEmpty() ? null : matched));

            chatHistory.setValue(msgs);
            thinking.setValue(false);
        });
    }

    /** AI直接返回了广告ID，按ID从allAds里取，最多3条 */
    private List<AdItem> matchByIds(String adIds) {
        List<AdItem> result = new ArrayList<>();
        if (adIds == null || adIds.isEmpty() || allAds == null) return result;
        for (String id : adIds.split(",")) {
            String sid = id.trim();
            for (AdItem ad : allAds) {
                if (ad.getId().equals(sid) && !result.contains(ad)) {
                    result.add(ad);
                    if (result.size() >= 3) break;
                }
            }
            if (result.size() >= 3) break;
        }
        return result;
    }
}
