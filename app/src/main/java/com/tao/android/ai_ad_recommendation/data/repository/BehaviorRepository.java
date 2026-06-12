package com.tao.android.ai_ad_recommendation.data.repository;

import androidx.lifecycle.LiveData;

import com.tao.android.ai_ad_recommendation.data.local.BehaviorDao;
import com.tao.android.ai_ad_recommendation.model.UserBehavior;
import com.tao.android.ai_ad_recommendation.util.Constants;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 用户行为数据仓库 - 管理点赞/收藏/评论的持久化
 *
 * 💡 知识点：数据库写操作不能在主线程！所以用 ExecutorService 在后台线程执行
 *         LiveData 自动处理线程切换，UI观察LiveData即可
 *
 * 🔗 被使用：DetailViewModel / InteractionBar
 *
 * ====== 框架说明 ======
 * 框架已搭好，toggleLike/toggleFavorite/comment 需要你来写核心逻辑。
 */
public class BehaviorRepository {

    private final BehaviorDao behaviorDao;
    // 💡 知识点：单线程线程池，保证所有数据库写操作按顺序执行（避免并发问题）
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    public BehaviorRepository(BehaviorDao behaviorDao) {
        this.behaviorDao = behaviorDao;
    }

    /**
     * 切换点赞状态（点赞↔取消点赞）
     *
     * TODO: 【你来写-中等】toggleLike()
     *
     * 思路：
     * 1. 查数据库：是否已有点赞记录？（用 behaviorDao.getBehavior() 同步查）
     *    ⚠️ 注意：Room不允许在主线程查询，需要在executor中执行
     * 2. 如果已有点赞 → 删除（取消点赞）
     * 3. 如果没有 → 插入新的点赞记录
     *
     * 💡 知识点："切换" = toggle，一套逻辑处理开和关两种状态
     *
     * @param adId 广告ID
     * @param callback 回调，true=现在已点赞, false=现在未点赞
     */
    // TODO: 【你来写-中等】实现这个方法
    public void toggleLike(String adId, OnToggleCallback callback) {
        executor.execute(() -> {
            UserBehavior existing = behaviorDao.getBehaviorSync(adId, Constants.BEHAVIOR_LIKE);
            if (existing != null) {
                // 已有点赞 → 取消
                behaviorDao.deleteBehavior(existing);
                callback.onResult(false);
            } else {
                // 没有点赞 → 点赞
                UserBehavior like = new UserBehavior(adId, Constants.BEHAVIOR_LIKE, null, System.currentTimeMillis());
                behaviorDao.insertBehavior(like);
                callback.onResult(true);
            }
        });
    }

    /**
     * 切换收藏状态
     *
     * TODO: 【你来写-中等】toggleFavorite()
     * 逻辑与 toggleLike 几乎一样，只是 behaviorType 改为 BEHAVIOR_FAVORITE
     */
    // TODO: 【你来写-中等】实现这个方法
    public void toggleFavorite(String adId, OnToggleCallback callback) {
        executor.execute(() -> {
            // ====== 你的代码从这里开始 ======
            UserBehavior existing = behaviorDao.getBehaviorSync(adId, Constants.BEHAVIOR_FAVORITE);
            if (existing!=null) {
                behaviorDao.deleteBehavior(existing);
                callback.onResult(false);
            }else {
                UserBehavior fav = new UserBehavior(adId, Constants.BEHAVIOR_FAVORITE, null, System.currentTimeMillis());
                behaviorDao.insertBehavior(fav);
                callback.onResult(true);
            }

            // ====== 你的代码到这里结束 ======
        });
    }

    /**
     * 发表评论
     *
     * TODO: 【你来写-简单】addComment()
     * 创建 UserBehavior(adId, BEHAVIOR_COMMENT, commentText, System.currentTimeMillis())
     * 插入数据库
     */
    // TODO: 【你来写-简单】
    public void addComment(String adId, String commentText) {
        executor.execute(() -> {
            // ====== 你的代码 ======
            UserBehavior comment = new UserBehavior(adId, Constants.BEHAVIOR_COMMENT, commentText, System.currentTimeMillis());
            behaviorDao.insertBehavior(comment);
        });
    }

    /** 获取点赞总数（可观察） */
    public LiveData<Integer> getLikeCount(String adId) {
        return behaviorDao.getBehaviorCount(adId, Constants.BEHAVIOR_LIKE);
    }

    /** 获取收藏总数 */
    public LiveData<Integer> getFavoriteCount(String adId) {
        return behaviorDao.getBehaviorCount(adId, Constants.BEHAVIOR_FAVORITE);
    }

    /** 获取所有评论 */
    public LiveData<List<UserBehavior>> getComments(String adId) {
        return behaviorDao.getComments(adId);
    }

    // ─── 埋点统计 ───

    /** 记录曝光（卡片进入可见区域时调用） */
    public void recordImpression(String adId) {
        executor.execute(() -> {
            UserBehavior impression = new UserBehavior(adId, Constants.BEHAVIOR_IMPRESSION, null, System.currentTimeMillis());
            behaviorDao.insertBehavior(impression);
        });
    }

    /** 记录点击 */
    public void recordClick(String adId) {
        executor.execute(() -> {
            UserBehavior click = new UserBehavior(adId, Constants.BEHAVIOR_CLICK, null, System.currentTimeMillis());
            behaviorDao.insertBehavior(click);
        });
    }

    /** 获取曝光数 */
    public LiveData<Integer> getImpressionCount(String adId) {
        return behaviorDao.getBehaviorCount(adId, Constants.BEHAVIOR_IMPRESSION);
    }

    /** 获取点击数 */
    public LiveData<Integer> getClickCount(String adId) {
        return behaviorDao.getBehaviorCount(adId, Constants.BEHAVIOR_CLICK);
    }

    /**
     * 回调接口：通知UI操作结果
     */
    public interface OnToggleCallback {
        void onResult(boolean isActive);  // true=已激活(已点赞/收藏), false=已取消
    }
}
