package com.tao.android.ai_ad_recommendation.data.local;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.tao.android.ai_ad_recommendation.model.AdItem;
import com.tao.android.ai_ad_recommendation.model.UserBehavior;

/**
 * Room 数据库定义
 *
 * 💡 知识点：@Database 告诉Room "这是我的数据库，包含这些表"
 *         entities = {AdItem.class, UserBehavior.class} → 两张表
 *         version = 1 → 数据库版本，以后加字段要升级版本
 *
 * 🔗 被使用：AiAdApplication（初始化时创建实例）
 *
 * ====== 框架说明 ======
 * 【已实现】不需要修改。定义了两个DAO接口，由Room自动生成实现类。
 */
@Database(
    entities = {AdItem.class, UserBehavior.class},
    version = 1,
    exportSchema = false
)
public abstract class AppDatabase extends RoomDatabase {

    /**
     * 💡 知识点：Room 会自动生成这些抽象方法的实现
     *         你调用 database.adDao() 就能拿到DAO实例
     */
    public abstract AdDao adDao();
    public abstract BehaviorDao behaviorDao();

    public static final String DATABASE_NAME = "ai_ad_recommendation.db";
}
