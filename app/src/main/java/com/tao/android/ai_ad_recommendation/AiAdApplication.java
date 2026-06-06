package com.tao.android.ai_ad_recommendation;

import android.app.Application;

import androidx.room.Room;

import com.tao.android.ai_ad_recommendation.data.local.AppDatabase;

/**
 * Application 类 - App启动时最先初始化
 *
 * 💡 知识点：Application 是整个App的入口，比任何Activity都早创建
 *         在这里初始化全局单例（数据库），避免重复创建
 *
 * ====== 框架说明 ======
 * 【已实现】不需要修改。
 * 其他类通过 ((AiAdApplication) getApplication()).getDatabase() 获取数据库实例。
 */
public class AiAdApplication extends Application {

    private AppDatabase database;

    @Override
    public void onCreate() {
        super.onCreate();
        // 💡 知识点：Room.databaseBuilder 创建数据库实例
        //         通常只需要在整个App生命周期中创建一个实例
        database = Room.databaseBuilder(
                getApplicationContext(),
                AppDatabase.class,
                AppDatabase.DATABASE_NAME
        ).build();
    }

    public AppDatabase getDatabase() {
        return database;
    }
}
