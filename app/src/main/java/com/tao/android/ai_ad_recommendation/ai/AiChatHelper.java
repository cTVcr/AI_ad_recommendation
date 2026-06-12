package com.tao.android.ai_ad_recommendation.ai;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * AI 对话辅助类 — 发送用户消息给大模型，返回标签
 *
 * 流程:
 *   用户说 "想买拍照手机"
 *   → 构造 Prompt: "你是广告推荐助手。用户说: 想买拍照手机。请从以下标签中选最匹配的..."
 *   → 大模型返回: "科技,数码"
 *   → 用这些标签匹配本地广告
 *
 * 持续对话: 每次调用带上对话历史，AI记住上下文
 */
public class AiChatHelper {

    private final AiApiService apiService;
    private final MockAiService fallback;  // API不可用时兜底

    public AiChatHelper() {
        // baseUrl 随便填(会被@Url覆盖)，但要符合URL格式
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.example.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        this.apiService = retrofit.create(AiApiService.class);
        this.fallback = new MockAiService();
    }

    /**
     * AI直接分析广告库 → 返回匹配的广告ID + 对话回复
     */
    public void analyzeAndMatch(String userInput, List<String> history,
                                 List<com.tao.android.ai_ad_recommendation.model.AdItem> allAds,
                                 MatchCallback callback) {
        // 没有API Key → Mock: 标签匹配 → 找出对应广告ID
        if (AiApiService.API_KEY.equals("sk-your-api-key-here")) {
            android.util.Log.d("AI_API", "没有API Key，Mock标签匹配");
            String tags = fallback.semanticSearch(userInput.toLowerCase().trim());
            // 把标签转成广告ID
            StringBuilder ids = new StringBuilder();
            int cnt = 0;
            for (com.tao.android.ai_ad_recommendation.model.AdItem ad : allAds) {
                if (cnt >= 3) break;
                for (String t : tags.split(",")) {
                    if (ad.getTags() != null && ad.getTags().contains(t.trim())) {
                        if (ids.length() > 0) ids.append(",");
                        ids.append(ad.getId());
                        cnt++;
                        break;
                    }
                }
            }
            callback.onResult(ids.toString(), "为你找到以下匹配广告:");
            return;
        }

        android.util.Log.d("AI_API", "AI直接分析广告库: " + allAds.size() + "条");

        // 构建广告列表文本发给AI
        StringBuilder adListText = new StringBuilder();
        for (int i = 0; i < allAds.size(); i++) {
            com.tao.android.ai_ad_recommendation.model.AdItem ad = allAds.get(i);
            adListText.append("[").append(ad.getId()).append("] ")
                .append(ad.getTitle()).append(" | 标签:").append(ad.getTags())
                .append(" | 广告主:").append(ad.getAdvertiser())
                .append(" | ").append(ad.getDescription().substring(0, Math.min(40, ad.getDescription().length())))
                .append("\n");
        }

        List<Map<String, String>> messages = new ArrayList<>();
        messages.add(makeMessage("system",
            "你是广告推荐助手。根据用户需求，从以下广告库中选出最匹配的1-3条广告。\n\n"
            + "广告库:\n" + adListText.toString() + "\n"
            + "返回格式: 严格按JSON格式返回，不要加任何其他文字。\n"
            + "{\"reply\":\"你的对话回复(一句话)\",\"ids\":[\"ad_001\",\"ad_002\"]}\n\n"
            + "注意: reply要自然有人情味，像朋友推荐一样。ids按匹配度从高到低排序，最多3个。"));

        // 历史
        int hs = Math.max(0, history.size() - 6);
        for (int i = hs; i < history.size(); i++) {
            messages.add(makeMessage((i % 2 == 0) ? "user" : "assistant", history.get(i)));
        }
        messages.add(makeMessage("user", userInput));

        Map<String, Object> body = new HashMap<>();
        body.put("model", AiApiService.MODEL);
        body.put("messages", messages);
        body.put("max_tokens", 200);
        body.put("temperature", 0.3);
        body.put("response_format", java.util.Collections.singletonMap("type", "json_object"));

        apiService.chat(AiApiService.API_URL, "Bearer " + AiApiService.API_KEY, body)
            .enqueue(new Callback<Map<String, Object>>() {
                @Override
                public void onResponse(Call<Map<String, Object>> c, Response<Map<String, Object>> r) {
                    if (r.isSuccessful() && r.body() != null) {
                        try {
                            List<Map<String, Object>> choices = (List<Map<String, Object>>) r.body().get("choices");
                            String content = (String)((Map<String, Object>)choices.get(0).get("message")).get("content");
                            android.util.Log.d("AI_API", "AI返回: " + content);
                            com.google.gson.Gson gson = new com.google.gson.Gson();
                            AiMatchResult result = gson.fromJson(content, AiMatchResult.class);
                            if (result != null && result.ids != null) {
                                callback.onResult(joinIds(result.ids), result.reply != null ? result.reply : "为你找到匹配的广告:");
                                return;
                            }
                        } catch (Exception e) { e.printStackTrace(); }
                    }
                    // 降级Mock
                    String tags = fallback.semanticSearch(userInput.toLowerCase().trim());
                    StringBuilder ids = new StringBuilder();
                    for (com.tao.android.ai_ad_recommendation.model.AdItem ad : allAds) {
                        for (String t : tags.split(",")) {
                            if (ad.getTags() != null && ad.getTags().contains(t.trim())) {
                                if (ids.length() > 0) ids.append(",");
                                ids.append(ad.getId());
                                break;
                            }
                        }
                    }
                    callback.onResult(ids.toString(), "为你找到以下广告:");
                }

                @Override
                public void onFailure(Call<Map<String, Object>> c, Throwable t) {
                    String tags = fallback.semanticSearch(userInput.toLowerCase().trim());
                    StringBuilder ids = new StringBuilder();
                    for (com.tao.android.ai_ad_recommendation.model.AdItem ad : allAds) {
                        for (String tag : tags.split(",")) {
                            if (ad.getTags() != null && ad.getTags().contains(tag.trim())) {
                                if (ids.length() > 0) ids.append(",");
                                ids.append(ad.getId());
                                break;
                            }
                        }
                    }
                    callback.onResult(ids.toString(), "为你找到以下广告:");
                }
            });
    }

    private static class AiMatchResult {
        String reply;
        java.util.List<String> ids;
    }

    private String joinIds(java.util.List<String> ids) {
        StringBuilder sb = new StringBuilder();
        for (String id : ids) sb.append(id).append(",");
        return sb.toString();
    }

    /**
     * 为详情页生成 AI 分析报告
     *
     * @param adTitle      广告标题
     * @param adDesc       广告描述
     * @param adTags       广告标签
     * @param callback     回调: 分析文本(失败/null则降级Mock)
     */
    public void generateDetailAnalysis(String adTitle, String adDesc, String adTags,
                                       DetailAnalysisCallback callback) {
        String prompt = "你是广告分析专家。请为以下广告写一段100-200字的深度分析，包括:\n"
            + "1. 目标人群分析\n2. 卖点亮点\n3. 购买建议\n\n"
            + "广告标题: " + adTitle + "\n"
            + "广告描述: " + adDesc + "\n"
            + "标签: " + adTags + "\n\n"
            + "请用自然语气，每点一行，格式: 👥 目标人群: ...\n💡 卖点亮点: ...\n🎯 购买建议: ...";

        // 没有API Key → Mock
        if (AiApiService.API_KEY.equals("sk-your-api-key-here")) {
            callback.onResult(mockDetailAnalysis(adTitle, adDesc, adTags));
            return;
        }

        List<Map<String, String>> messages = new ArrayList<>();
        messages.add(makeMessage("system", prompt));
        messages.add(makeMessage("user", "请为这个广告写分析"));

        Map<String, Object> body = new HashMap<>();
        body.put("model", AiApiService.MODEL);
        body.put("messages", messages);
        body.put("max_tokens", 300);
        body.put("temperature", 0.7);

        apiService.chat(AiApiService.API_URL, "Bearer " + AiApiService.API_KEY, body)
            .enqueue(new Callback<Map<String, Object>>() {
                @Override
                public void onResponse(Call<Map<String, Object>> c, Response<Map<String, Object>> r) {
                    if (r.isSuccessful() && r.body() != null) {
                        try {
                            List<Map<String, Object>> choices =
                                (List<Map<String, Object>>) r.body().get("choices");
                            String content = (String) ((Map<String, Object>)
                                choices.get(0).get("message")).get("content");
                            android.util.Log.d("AI_API", "详情分析API返回: " +
                                (content != null ? content.substring(0, Math.min(50, content.length())) : "null"));
                            callback.onResult(content);
                            return;
                        } catch (Exception e) { e.printStackTrace(); }
                    }
                    callback.onResult(mockDetailAnalysis(adTitle, adDesc, adTags));
                }
                @Override
                public void onFailure(Call<Map<String, Object>> c, Throwable t) {
                    callback.onResult(mockDetailAnalysis(adTitle, adDesc, adTags));
                }
            });
    }

    /** 详情分析 Mock（没有API时的兜底） */
    private String mockDetailAnalysis(String title, String desc, String tags) {
        StringBuilder sb = new StringBuilder();
        sb.append("👥 目标人群: 对" + tags.replace(",", "、") + "感兴趣的用户\n\n");
        sb.append("💡 卖点亮点: 该广告主打差异化和用户需求痛点，配合吸引眼球的创意文案，能有效提升点击率\n\n");
        sb.append("🎯 购买建议: 内容质量较高，建议加大投放力度，特别在" +
            (tags.contains("科技") ? "科技数码" : tags.contains("美妆") ? "美妆护肤" :
             tags.contains("食品") ? "食品饮料" : tags.contains("游戏") ? "游戏娱乐" : "热门") +
            "垂直领域");
        return sb.toString();
    }

    private Map<String, String> makeMessage(String role, String content) {
        Map<String, String> msg = new HashMap<>();
        msg.put("role", role);
        msg.put("content", content);
        return msg;
    }

    public interface MatchCallback {
        void onResult(String adIds, String reply);  // adIds: "ad_001,ad_006", reply: AI的对话回复
    }

    public interface DetailAnalysisCallback {
        void onResult(String analysis);
    }
}
