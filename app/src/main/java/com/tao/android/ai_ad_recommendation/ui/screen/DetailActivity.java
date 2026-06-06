package com.tao.android.ai_ad_recommendation.ui.screen;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.tao.android.ai_ad_recommendation.AiAdApplication;
import com.tao.android.ai_ad_recommendation.R;
import com.tao.android.ai_ad_recommendation.data.repository.BehaviorRepository;
import com.tao.android.ai_ad_recommendation.model.AdItem;
import com.tao.android.ai_ad_recommendation.viewmodel.DetailViewModel;

/**
 * 广告详情页 - 全屏图片/视频预览 + 互动操作
 *
 * 💡 知识点：通过 Intent 接收 MainFeedFragment 传来的广告数据
 *         getIntent().getSerializableExtra("ad_item") 获取 AdItem 对象
 *
 * 🔗 被打开：MainFeedFragment (点击广告卡片时)
 * 🔗 复用组件：VideoPlayerView / InteractionBar / TagChipView
 *
 * ====== 框架说明 ======
 * 框架已搭建。初始化控件、绑定数据、设置互动逻辑需要你来写。
 */
public class DetailActivity extends AppCompatActivity {

    private DetailViewModel viewModel;
    private AdItem adItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        // ====== 第1步：接收Intent传来的广告数据 ======
        // 💡 知识点：AdItem 实现了 Serializable，可以通过Intent传递
        adItem = (AdItem) getIntent().getSerializableExtra("ad_item");
        if (adItem == null) {
            finish();  // 数据异常，直接关闭
            return;
        }

        // ====== 第2步：创建ViewModel ======
        BehaviorRepository behaviorRepository = new BehaviorRepository(
                ((AiAdApplication) getApplication()).getDatabase().behaviorDao());
        viewModel = new ViewModelProvider(this).get(DetailViewModel.class);
        // TODO: 如果ViewModel需要构造参数，创建Factory（参考MainFeedFragment的做法）
        viewModel.setAd(adItem);

        // ====== 第3步：绑定控件 + 设置数据 ======
        // TODO: 【你来写-中等】setupUI()
        //
        // 思路：
        // 1. findViewById 获取各种控件
        // 2. 根据 adItem.getType() 决定显示 ImageView 还是 VideoPlayerView
        // 3. 如果是图片：用 Glide 加载大图
        // 4. 如果是视频：初始化 ExoPlayer，设置视频URL
        // 5. 设置标题、描述、广告主等信息

        // ====== 第4步：设置互动按钮 ======
        // TODO: 【你来写-中等】setupInteraction()
        //
        // 思路：
        // 1. 点赞按钮点击 → viewModel.toggleLike()
        // 2. 收藏按钮点击 → viewModel.toggleFavorite()
        // 3. 评论按钮点击 → 弹出评论DialogFragment
        // 4. 观察 viewModel.isLiked / isFavorited 更新按钮状态
        // 5. 观察 viewModel.likeCount / favoriteCount 更新数字

        // ====== 第5步：观察Toast提示 ======
        // viewModel.toastMessage.observe(this, msg ->
        //     Toast.makeText(this, msg, Toast.LENGTH_SHORT).show());
    }

    /**
     * 返回时保存浏览位置信息
     *
     * 💡 知识点：startActivityForResult + setResult 可以回传数据
     *         这里用更简单的方式：详情页的互动状态存在Room数据库里，
     *         信息流Fragment重新可见时会自动从数据库读取最新状态
     */
}
