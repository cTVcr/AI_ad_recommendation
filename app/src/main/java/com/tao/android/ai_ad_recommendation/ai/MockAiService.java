package com.tao.android.ai_ad_recommendation.ai;

import com.tao.android.ai_ad_recommendation.model.AiResult;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Mock AI服务 — 基于关键词匹配生成标签和摘要，模拟AI语义理解
 *
 * 核心思路：分析广告标题和描述中的关键词 → 匹配标签 → 生成摘要
 */
public class MockAiService implements AiService {

    // ─── 关键词 → 标签映射表（模拟AI语义理解） ───
    private static final java.util.Map<String, String> KEYWORD_TAG_MAP = new java.util.HashMap<>();
    static {
        KEYWORD_TAG_MAP.put("手机", "科技");     KEYWORD_TAG_MAP.put("芯片", "科技");
        KEYWORD_TAG_MAP.put("卫星", "科技");     KEYWORD_TAG_MAP.put("镜头", "科技");
        KEYWORD_TAG_MAP.put("徕卡", "科技");     KEYWORD_TAG_MAP.put("大底", "科技");
        KEYWORD_TAG_MAP.put("图传", "科技");     KEYWORD_TAG_MAP.put("无人机", "科技");
        KEYWORD_TAG_MAP.put("酷睿", "科技");     KEYWORD_TAG_MAP.put("笔记本", "科技");
        KEYWORD_TAG_MAP.put("引擎", "科技");     KEYWORD_TAG_MAP.put("光线追踪", "科技");

        KEYWORD_TAG_MAP.put("唇釉", "美妆");     KEYWORD_TAG_MAP.put("彩妆", "美妆");
        KEYWORD_TAG_MAP.put("持色", "美妆");     KEYWORD_TAG_MAP.put("色号", "美妆");

        KEYWORD_TAG_MAP.put("游戏", "游戏");     KEYWORD_TAG_MAP.put("Boss", "游戏");
        KEYWORD_TAG_MAP.put("技能", "游戏");     KEYWORD_TAG_MAP.put("史诗", "游戏");

        KEYWORD_TAG_MAP.put("气垫", "服饰");     KEYWORD_TAG_MAP.put("配色", "服饰");
        KEYWORD_TAG_MAP.put("穿搭", "服饰");     KEYWORD_TAG_MAP.put("鞋", "服饰");

        KEYWORD_TAG_MAP.put("家电", "家电");     KEYWORD_TAG_MAP.put("空调", "家电");
        KEYWORD_TAG_MAP.put("冰箱", "家电");     KEYWORD_TAG_MAP.put("吸尘器", "家电");
        KEYWORD_TAG_MAP.put("洗衣机", "家电");

        KEYWORD_TAG_MAP.put("坚果", "食品");     KEYWORD_TAG_MAP.put("咖啡", "食品");
        KEYWORD_TAG_MAP.put("零食", "食品");     KEYWORD_TAG_MAP.put("拿铁", "食品");

        KEYWORD_TAG_MAP.put("学生", "学生党");   KEYWORD_TAG_MAP.put("限时", "限时优惠");
        KEYWORD_TAG_MAP.put("折扣", "限时优惠"); KEYWORD_TAG_MAP.put("五折", "限时优惠");
        KEYWORD_TAG_MAP.put("分期", "限时优惠"); KEYWORD_TAG_MAP.put("免息", "限时优惠");

        KEYWORD_TAG_MAP.put("首发", "新品");     KEYWORD_TAG_MAP.put("新机", "新品");
        KEYWORD_TAG_MAP.put("限定", "新品");     KEYWORD_TAG_MAP.put("上新", "新品");
        KEYWORD_TAG_MAP.put("预售", "新品");     KEYWORD_TAG_MAP.put("新款", "新品");

        KEYWORD_TAG_MAP.put("正品", "品牌正品"); KEYWORD_TAG_MAP.put("官方", "品牌正品");
        KEYWORD_TAG_MAP.put("授权", "品牌正品"); KEYWORD_TAG_MAP.put("旗舰", "品牌正品");

        KEYWORD_TAG_MAP.put("销量", "热卖");     KEYWORD_TAG_MAP.put("热卖", "热卖");
        KEYWORD_TAG_MAP.put("爆款", "热卖");     KEYWORD_TAG_MAP.put("热销", "热卖");

        KEYWORD_TAG_MAP.put("性价比", "性价比"); KEYWORD_TAG_MAP.put("实惠", "性价比");
        KEYWORD_TAG_MAP.put("低价", "性价比");   KEYWORD_TAG_MAP.put("便宜", "性价比");

        KEYWORD_TAG_MAP.put("办公", "办公利器"); KEYWORD_TAG_MAP.put("商务", "办公利器");
        KEYWORD_TAG_MAP.put("工作", "办公利器"); KEYWORD_TAG_MAP.put("键盘", "办公利器");

        KEYWORD_TAG_MAP.put("数码", "数码");     KEYWORD_TAG_MAP.put("拍照", "数码");
        KEYWORD_TAG_MAP.put("音乐", "数码");     KEYWORD_TAG_MAP.put("音质", "数码");
        KEYWORD_TAG_MAP.put("曲库", "数码");
    }

    @Override
    public AiResult generateSummary(String adId, String adDescription) {
        // 基于描述内容选摘要模板
        int hash = Math.abs(adId.hashCode());
        String[] templates = {
            "🔥 限时优惠！点击了解详情",
            "⭐ 好评如潮！用户真实评价超千条",
            "🆕 新品首发！抢先体验",
            "💎 品质之选！做工精良",
            "🏆 销量领先！本月已售万件",
            "📦 品质保障！官方正品",
            "✅ 强烈推荐！值得入手",
            "🎯 精准匹配！为您推荐"
        };
        String summary = templates[hash % templates.length];

        AiResult result = new AiResult();
        result.setAdId(adId);
        result.setSummary(summary);
        result.setGeneratedAt(System.currentTimeMillis());
        return result;
    }

    @Override
    public AiResult generateTags(String adId, String adTitle, String adDescription) {
        // 合并标题和描述中的关键词，匹配标签
        String text = (adTitle + " " + adDescription).toLowerCase();
        java.util.Set<String> matchedTags = new java.util.LinkedHashSet<>();

        for (java.util.Map.Entry<String, String> entry : KEYWORD_TAG_MAP.entrySet()) {
            if (text.contains(entry.getKey())) {
                matchedTags.add(entry.getValue());
            }
        }

        // 如果匹配太少，补一个默认标签
        if (matchedTags.size() < 2) {
            int hash = Math.abs(adId.hashCode());
            String[] defaults = {"热卖", "新品", "性价比", "品牌正品"};
            matchedTags.add(defaults[hash % defaults.length]);
        }

        AiResult result = new AiResult();
        result.setAdId(adId);
        result.setTags(new ArrayList<>(matchedTags));
        result.setGeneratedAt(System.currentTimeMillis());
        return result;
    }

    @Override
    public String semanticSearch(String query) {
        // 意图识别：从用户输入提取关键词 → 返回匹配的标签列表（逗号分隔）
        if (query == null || query.trim().isEmpty()) return "";

        String q = query.toLowerCase();
        java.util.Set<String> tags = new java.util.LinkedHashSet<>();

        // 意图映射表：口语 → 标签。第一列是标签名，后面是触发词
        String[][] intentMap = {
            // 价格/性价比相关
            {"性价比", "便宜", "实惠", "低价", "不贵", "省钱", "划算", "白菜", "学生", "预算", "平价"},
            // 新品相关
            {"新品", "最新", "新出", "首发", "新款", "新上市", "刚出", "刚发布", "新买"},
            // 品牌正品
            {"品牌正品", "质量好", "正品", "正版", "官方", "旗舰", "靠谱", "放心", "信得过"},
            // 热卖
            {"热卖", "火", "热门", "流行", "多人买", "畅销", "爆款", "大家都在", "推荐"},
            // 限时优惠
            {"限时优惠", "优惠", "打折", "折扣", "便宜点", "降价", "促销", "活动", "特价", "骨折"},
            // 科技
            {"科技", "科技", "数码", "电子", "智能", "手机", "电脑", "芯片", "高科技", "黑科技"},
            // 游戏
            {"游戏", "游戏", "玩", "剧情", "打怪", "Boss", "通关", "手柄", "电竞", "娱乐"},
            // 美妆
            {"美妆", "美妆", "化妆", "口红", "唇釉", "彩妆", "护肤", "漂亮", "好看", "美丽", "化妆品"},
            // 食品
            {"食品", "吃", "零食", "坚果", "咖啡", "奶茶", "喝", "美食", "好吃", "饮料", "食物", "早餐"},
            // 家电
            {"家电", "家电", "电器", "空调", "冰箱", "吸尘", "洗衣机", "电视", "家居", "打扫"},
            // 服饰
            {"服饰", "鞋", "穿搭", "运动", "潮流", "穿", "衣服", "裤子", "潮牌", "搭配"},
            // 数码
            {"数码", "数码", "拍照", "摄像", "相机", "音乐", "耳机", "音质", "听歌", "视频", "录制", "像素"},
            // 办公利器
            {"办公利器", "办公", "工作", "商务", "笔记本", "出差", "键盘", "生产力", "上班"},
            // 学生党
            {"学生党", "学生", "大学", "校园", "考试", "宿舍", "年轻人", "上学", "学习"},
        };

        for (String[] intent : intentMap) {
            for (int i = 1; i < intent.length; i++) {
                if (q.contains(intent[i])) {
                    tags.add(intent[0]);
                    break;
                }
            }
        }

        // 也检查原始关键词
        for (String key : KEYWORD_TAG_MAP.keySet()) {
            if (q.contains(key)) tags.add(KEYWORD_TAG_MAP.get(key));
        }

        // 没匹配到任何标签 → 返回原始输入作为关键词
        if (tags.isEmpty()) return query.trim();

        StringBuilder sb = new StringBuilder();
        for (String t : tags) { sb.append(t).append(","); }
        return sb.toString();
    }

    /** 生成对话式回复 — 根据用户需求和匹配结果，用自然语言回复 */
    public String buildConversationalReply(String query, java.util.List<com.tao.android.ai_ad_recommendation.model.AdItem> results) {
        if (results == null || results.isEmpty()) return "没找到匹配的~";

        // 提取匹配的标签来生成个性化开头
        String tags = semanticSearch(query.toLowerCase());
        StringBuilder sb = new StringBuilder();

        if (query.contains("便宜") || query.contains("学生") || query.contains("性价比")) {
            sb.append("考虑到你的预算，我帮你挑了 ");
        } else if (query.contains("新") || query.contains("最新")) {
            sb.append("最新上架的这几款应该适合你，");
        } else if (query.contains("好") || query.contains("推荐")) {
            sb.append("根据大家的好评，我推荐 ");
        } else {
            sb.append("我帮你找到了 ");
        }

        sb.append(results.size()).append(" 条匹配的广告:\n");
        for (int i = 0; i < results.size(); i++) {
            com.tao.android.ai_ad_recommendation.model.AdItem ad = results.get(i);
            sb.append("\n").append(i + 1).append(". ").append(ad.getTitle());
            sb.append("\n   ").append(ad.getSummary());
            sb.append("\n");
        }
        sb.append("\n点击卡片可以查看详情哦~");
        return sb.toString();
    }
}