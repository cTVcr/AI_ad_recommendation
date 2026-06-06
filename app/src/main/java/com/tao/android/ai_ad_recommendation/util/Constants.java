package com.tao.android.ai_ad_recommendation.util;

/**
 * 全局常量集中管理
 *
 * 💡 知识点：把所有常量放在一个地方的好处：
 *         1. 修改时只需改一处
 *         2. 避免魔法字符串散布代码各处
 *         3. IDE可以自动补全
 *
 * ====== 框架说明 ======
 * 这个文件【已实现】，不需要修改。
 * 在写代码时引用 Constants.PAGE_SIZE 即可。
 */
public class Constants {

    // 分页：每次加载多少条
    public static final int PAGE_SIZE = 10;

    // 三个Tab标签
    public static final String TAB_RECOMMEND = "推荐";
    public static final String TAB_FOLLOWING = "关注";
    public static final String TAB_HOT = "热门";

    // 广告卡片三种类型
    public static final String TYPE_LARGE_IMAGE = "large_image";
    public static final String TYPE_SMALL_IMAGE = "small_image";
    public static final String TYPE_VIDEO = "video";

    // 用户行为类型
    public static final String BEHAVIOR_LIKE = "like";
    public static final String BEHAVIOR_FAVORITE = "favorite";
    public static final String BEHAVIOR_COMMENT = "comment";
    public static final String BEHAVIOR_IMPRESSION = "impression";
    public static final String BEHAVIOR_CLICK = "click";

    // AI服务
    public static final String AI_PROVIDER_MOCK = "mock";
    public static final String AI_PROVIDER_QWEN = "qwen";

    // Qwen API (未来上线)
    public static final String QWEN_API_BASE_URL = "https://dashscope.aliyuncs.com/api/v1/";
    public static final String QWEN_MODEL_NAME = "qwen-turbo";

    // Mock数据文件名
    public static final String MOCK_DATA_FILE = "mock/ad_feed.json";
}
