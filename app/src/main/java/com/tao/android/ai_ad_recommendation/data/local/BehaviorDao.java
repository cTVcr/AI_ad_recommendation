package com.tao.android.ai_ad_recommendation.data.local;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.tao.android.ai_ad_recommendation.model.UserBehavior;

import java.util.List;

/**
 * 用户行为 DAO (点赞/收藏/评论/曝光/点击)
 *
 * 💡 知识点：每个 @Query 方法对应一条SQL语句
 *         Room会在编译时检查SQL语法是否正确 ← 比手写SQLiteOpenHelper安全多了！
 *
 * 🔗 被使用：BehaviorRepository → ViewModel → UI
 *
 * ====== 框架说明 ======
 * 【已实现】不需要修改。已覆盖所有行为操作的查询。
 * ⚠️ 注意：方法命名规范 - get前缀返回LiveData(可观察), insert/delete是void(写操作)
 */
@Dao
public interface BehaviorDao {

    /** 查某个广告某种行为的数量（比如"有多少人点赞"） */
    @Query("SELECT COUNT(*) FROM user_behaviors WHERE ad_id = :adId AND behavior_type = :type")
    LiveData<Integer> getBehaviorCount(String adId, String type);

    /** 查某个广告的所有评论 */
    @Query("SELECT * FROM user_behaviors WHERE ad_id = :adId AND behavior_type = 'comment' ORDER BY timestamp DESC")
    LiveData<List<UserBehavior>> getComments(String adId);

    /** 同步查询（仅在后台线程使用！toggle逻辑用） */
    @Query("SELECT * FROM user_behaviors WHERE ad_id = :adId AND behavior_type = :type LIMIT 1")
    UserBehavior getBehaviorSync(String adId, String type);

    /** 插入一条行为记录 */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertBehavior(UserBehavior behavior);

    /** 删除一条行为记录（取消点赞/取消收藏时用） */
    @Delete
    void deleteBehavior(UserBehavior behavior);

}
