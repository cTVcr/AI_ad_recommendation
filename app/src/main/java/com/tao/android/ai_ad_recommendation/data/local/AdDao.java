package com.tao.android.ai_ad_recommendation.data.local;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.tao.android.ai_ad_recommendation.model.AdItem;

import java.util.List;

/**
 * 广告数据 DAO (Data Access Object)
 *
 * 💡 知识点：@Dao 接口由 Room 自动生成实现类
 *         LiveData<List<AdItem>> 返回的是可观察数据，
 *         数据库一变，UI自动刷新 ← 这就是MVVM的精髓！
 *
 * 🔗 被使用：AdRepository → MainFeedViewModel → UI
 *
 * ====== 框架说明 ======
 * 【已实现】不需要修改。已覆盖本项目需要的所有查询。
 */
@Dao
public interface AdDao {

    /** 获取全部广告，按时间倒序（最新的在前） */
    @Query("SELECT * FROM ad_items ORDER BY timestamp DESC")
    LiveData<List<AdItem>> getAllAds();

    /** 按分类筛选广告，Tab切换时用 */
    @Query("SELECT * FROM ad_items WHERE category = :category ORDER BY timestamp DESC")
    LiveData<List<AdItem>> getAdsByCategory(String category);

    /** 根据ID查单条广告 */
    @Query("SELECT * FROM ad_items WHERE id = :adId")
    LiveData<AdItem> getAdById(String adId);

    /** 批量插入广告（Mock数据初始化用） */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAds(List<AdItem> ads);

    /** 曝光次数+1 */
    @Query("UPDATE ad_items SET impressions = impressions + 1 WHERE id = :adId")
    void incrementImpression(String adId);

    /** 点击次数+1 */
    @Query("UPDATE ad_items SET clicks = clicks + 1 WHERE id = :adId")
    void incrementClick(String adId);

    /** 清空所有数据 */
    @Query("DELETE FROM ad_items")
    void clearAll();
}
