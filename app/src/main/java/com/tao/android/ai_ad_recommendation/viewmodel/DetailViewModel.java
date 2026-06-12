package com.tao.android.ai_ad_recommendation.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.tao.android.ai_ad_recommendation.data.repository.BehaviorRepository;
import com.tao.android.ai_ad_recommendation.model.AdItem;
import com.tao.android.ai_ad_recommendation.model.UserBehavior;

import java.util.List;

/**
 * 详情页 ViewModel - 管理广告详情页的状态
 *
 * 💡 知识点：每个页面有独立的ViewModel，职责单一
 *         这个ViewModel不管信息流，只管详情页的互动状态
 *
 * 🔗 被使用：DetailActivity（观察互动状态）
 *
 * ====== 框架说明 ======
 * 互动功能的 toggleLike/toggleFavorite/addComment 需要你来实现。
 */
public class DetailViewModel extends ViewModel {

    private final BehaviorRepository behaviorRepository;

    /** 当前广告 */
    public MutableLiveData<AdItem> currentAd = new MutableLiveData<>();

    /** 是否已点赞 */
    public MutableLiveData<Boolean> isLiked = new MutableLiveData<>(false);

    /** 是否已收藏 */
    public MutableLiveData<Boolean> isFavorited = new MutableLiveData<>(false);

    /** 点赞数 */
    public MutableLiveData<Integer> likeCount = new MutableLiveData<>(0);

    /** 收藏数 */
    public MutableLiveData<Integer> favoriteCount = new MutableLiveData<>(0);

    /** 评论列表 */
    public MutableLiveData<List<UserBehavior>> comments = new MutableLiveData<>();

    /** 评论数 */
    public MutableLiveData<Integer> commentCount = new MutableLiveData<>(0);

    /** Toast提示信息 */
    public MutableLiveData<String> toastMessage = new MutableLiveData<>();

    public DetailViewModel(BehaviorRepository behaviorRepository) {
        this.behaviorRepository = behaviorRepository;
    }

    /**
     * 设置当前广告并加载互动数据
     *
     * @param ad 当前广告
     */
    public void setAd(AdItem ad) {
        currentAd.setValue(ad);
        loadInteractionData(ad.getId());
    }

    /** 加载互动数据（数量 + 是否已点赞/收藏的状态） */
    private void loadInteractionData(String adId) {
        // 点赞数 → 同时判断是否已点赞
        behaviorRepository.getLikeCount(adId).observeForever(count -> {
            likeCount.setValue(count);
            isLiked.setValue(count != null && count > 0);
        });
        // 收藏数 → 同时判断是否已收藏
        behaviorRepository.getFavoriteCount(adId).observeForever(count -> {
            favoriteCount.setValue(count);
            isFavorited.setValue(count != null && count > 0);
        });
        // 评论列表 → 列表 + 数量
        behaviorRepository.getComments(adId).observeForever(list -> {
            comments.setValue(list);
            commentCount.setValue(list != null ? list.size() : 0);
        });
        //埋点统计
        behaviorRepository.getImpressionCount(adId).observeForever(count -> {});
        behaviorRepository.getClickCount(adId).observeForever(count -> {});
    }

    /**
     * 切换点赞
     *
     * TODO: 【你来写-中等】toggleLike()
     *
     * 思路：
     * 1. AdItem ad = currentAd.getValue()
     * 2. behaviorRepository.toggleLike(ad.getId(), callback)
     * 3. 回调中: isLiked.setValue(isActive)
     * 4. toastMessage.setValue(isActive ? "已点赞" : "已取消点赞")
     */
    // TODO: 【你来写-中等】
    public void toggleLike() {
        // ====== 你的代码 ======
        AdItem ad=currentAd.getValue();
        if (ad==null) return;

        behaviorRepository.toggleLike(ad.getId(),isActive -> {
            isLiked.postValue(isActive);
            toastMessage.postValue(isActive ?"\"❤ 已点赞\"":"已取消点赞");
        });
    }

    /**
     * 切换收藏
     *
     * TODO: 【你来写-中等】toggleFavorite()
     * 逻辑同 toggleLike，用 behaviorRepository.toggleFavorite()
     */
    // TODO: 【你来写-中等】
    public void toggleFavorite() {
        // ====== 你的代码 ======
        AdItem ad=currentAd.getValue();
        if (ad==null) return;

        behaviorRepository.toggleFavorite(ad.getId(),isActive -> {
            isFavorited.postValue(isActive);
            toastMessage.postValue(isActive?"⭐  已收藏":"已取消收藏");
        });
    }

    /**
     * 发表评论
     *
     * TODO: 【你来写-简单】addComment()
     * 调用 behaviorRepository.addComment(adId, commentText)
     */
    // TODO: 【你来写-中等】
    public void addComment(String commentText) {
        // ====== 你的代码 ======
        AdItem ad=currentAd.getValue();
        if (ad==null) return;
        behaviorRepository.addComment(ad.getId(),commentText);
        toastMessage.postValue("评论已发表");
    }
}
