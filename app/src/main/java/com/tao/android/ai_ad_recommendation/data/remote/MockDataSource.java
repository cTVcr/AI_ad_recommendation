package com.tao.android.ai_ad_recommendation.data.remote;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.google.gson.Gson;
import com.tao.android.ai_ad_recommendation.model.AdItem;
import com.tao.android.ai_ad_recommendation.util.Constants;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Mock数据源：从 assets/mock/ad_feed.json 读取模拟广告数据
 *
 * 💡 知识点：Mock = 在没有后端接口时，用本地假数据模拟真实数据
 *         真正的App上线后，这里换成 Retrofit 调后端API
 *
 * 🔗 被使用：AdRepository（唯一调用方）
 *
 * ====== 框架说明 ======
 * 我已经写好了框架逻辑，核心方法需要你来填充。
 */
public class MockDataSource {

    private final Context context;
    private final Gson gson;
    private List<AdItem> allAds;  // 内存中缓存全部广告数据

    public MockDataSource(Context context) {
        this.context = context;
        this.gson = new Gson();
    }

    /**
     * 从assets读取JSON文件，解析为广告列表
     *
     * TODO: 【你来写-中等】parseAdFeedJson()
     *
     * 思路：
     * 1. 用 context.getAssets().open(Constants.MOCK_DATA_FILE) 打开文件
     * 2. 用 InputStreamReader + BufferedReader 读取全部文本
     * 3. 用 Gson 解析为 List<AdItem>
     * 4. 赋值到 this.allAds
     *
     * 💡 知识点：assets目录的文件用 AssetManager 读取
     * ⚠️ 注意：不要在主线程读取assets（这个方法最终会在后台线程调用）
     *
     * 参考代码框架：
     * <pre>{@code
     * try {
     *     InputStream is = context.getAssets().open(Constants.MOCK_DATA_FILE);
     *     BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
     *     StringBuilder sb = new StringBuilder();
     *     String line;
     *     while ((line = reader.readLine()) != null) {
     *         sb.append(line);
     *     }
     *     reader.close();
     *
     *     Type listType = new TypeToken<List<AdItem>>(){}.getType();
     *     allAds = gson.fromJson(sb.toString(), listType);
     * } catch (Exception e) {
     *     e.printStackTrace();
     *     allAds = new ArrayList<>();
     * }
     * }</pre>
     */
//    public String parseAdFeedJson(){
//
//
//    }


    // TODO: 【你来写-中等】实现这个方法
    public void loadMockData() {
        // ====== 你的代码从这里开始 ======
        try {
            InputStream is = context.getAssets().open(Constants.MOCK_DATA_FILE);

            BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            reader.close();
//            Gson 把JSON字符串-->Java对象列表
            Type listType = new TypeToken<List<AdItem>>() {}.getType();
            allAds=gson.fromJson(sb.toString(),listType);
        } catch (IOException e) {
            e.printStackTrace();
            allAds=new ArrayList<>();
        }
        // ====== 你的代码到这里结束 ======
    }

    /**
     * 分页获取广告数据（模拟真实API的分页行为）
     *
     * TODO: 【你来写-简单】getPage()
     *
     * 思路：
     * 1. 如果 allAds 为空，先调用 loadMockData()
     * 2. 计算起始位置：page * Constants.PAGE_SIZE
     * 3. 计算结束位置：Math.min(start + PAGE_SIZE, allAds.size())
     * 4. 返回 subList(start, end)
     * 5. 如果 start >= allAds.size()，返回空列表（表示没有更多数据了）
     *
     * ⚠️ 注意：要用 new ArrayList<>(subList) 包装，避免 ConcurrentModificationException
     *
     * @param page 页码，从0开始（0=第一页）
     * @return 该页的广告列表
     */
    // TODO: 【你来写-简单】实现这个方法
    public List<AdItem> getPage(int page) {
        // ====== 你的代码从这里开始 ======

        if (allAds==null) {
            loadMockData();
        }

        int start=page*Constants.PAGE_SIZE;

        if (start >= allAds.size()) {
            return new ArrayList<>();
        }

        int end=Math.min(start+Constants.PAGE_SIZE, allAds.size());

        return new ArrayList<>(allAds.subList(start,end));

        // ====== 你的代码到这里结束 ======
    }

    /**
     * 按分类+分页获取广告（Tab切换时使用）
     *
     * TODO: 【你来写-中等】getPageByCategory()
     *
     * 思路：
     * 1. 先确保 allAds 已加载
     * 2. 用 stream filter 过滤出匹配 category 的广告
     * 3. 在过滤后的列表中分页截取
     *
     * 💡 知识点：Java 8 的 Stream API
     *         allAds.stream().filter(ad -> ad.getCategory().equals(category)).collect(...)
     *
     * @param category 分类名称（推荐/关注/热门）
     * @param page 页码
     * @return 该分类该页的广告列表
     */
    // TODO: 【你来写-中等】实现这个方法
    public List<AdItem> getPageByCategory(String category, int page) {
        // ====== 你的代码从这里开始 ======
        if (allAds==null) {
            loadMockData();
        }
        List<AdItem> itemList = allAds.stream().filter(ad -> {
            return ad.getCategory().equals(category);
        }).collect(Collectors.toList());

        int start=page*Constants.PAGE_SIZE;
        if (start >= itemList.size()) {
            return new ArrayList<>();
        }
        int end=Math.min(start+Constants.PAGE_SIZE, itemList.size());

        return new ArrayList<>(itemList.subList(start,end));  // 替换这行


        // ====== 你的代码到这里结束 ======
    }

    /**
     * 随机获取N条广告（下拉刷新时模拟"新推荐"）
     *
     * TODO: 【你来写-中等】getRandomAds()
     *
     * 思路：
     * 1. 确保 allAds 已加载
     * 2. 把 allAds 复制到一个新列表
     * 3. Collections.shuffle() 随机打乱
     * 4. 取前 Math.min(count, list.size()) 条
     *
     * @param count 需要获取的条数
     * @return 随机广告列表
     */
    // TODO: 【你来写-中等】实现这个方法
    public List<AdItem> getRandomAds(int count) {
        // ====== 你的代码从这里开始 ======
        if (allAds==null) {
            loadMockData();
        }
        List<AdItem> nList=new ArrayList<>(allAds);


        Collections.shuffle(nList);

        int limit=Math.min(nList.size(), count);
        return new ArrayList<>(nList.subList(0,limit));  // 替换这行

        // ====== 你的代码到这里结束 ======
    }

    /** 获取总广告数 */
    public int getTotalCount() {
        return allAds != null ? allAds.size() : 0;
    }
}
