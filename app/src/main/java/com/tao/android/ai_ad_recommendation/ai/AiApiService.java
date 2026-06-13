package com.tao.android.ai_ad_recommendation.ai;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Url;

/**
 * 大模型 API — 通用 OpenAI 兼容格式
 *
 * 用法: 填 API_KEY + API_URL + MODEL 即可
 */
public interface AiApiService {

    // ─── 在这里填你的 API 信息 ───
    String API_KEY  = "sk-521a15508dfe4d5c8ec30ba35e71202c";
    String API_URL  = "https://api.deepseek.com/v1/chat/completions";  // DeepSeek
    String MODEL    = "deepseek-chat";

    /** @Url 参数会替换 baseUrl，解决不同厂商路径不同的问题 */
    @POST
    Call<Map<String, Object>> chat(
        @Url String url,
        @Header("Authorization") String auth,
        @Body Map<String, Object> body
    );
}
