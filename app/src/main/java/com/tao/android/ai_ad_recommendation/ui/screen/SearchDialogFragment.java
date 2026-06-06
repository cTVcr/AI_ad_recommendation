package com.tao.android.ai_ad_recommendation.ui.screen;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.tao.android.ai_ad_recommendation.R;
import com.tao.android.ai_ad_recommendation.ai.MockAiService;
import com.tao.android.ai_ad_recommendation.model.AdItem;
import com.tao.android.ai_ad_recommendation.viewmodel.SearchViewModel;

import java.util.List;

/**
 * 对话式搜索弹窗 - 底部弹出，类似抖音的搜索
 *
 * 💡 知识点：BottomSheetDialogFragment 是一个从底部滑出的Dialog
 *         比普通Dialog体验更好，常见于搜索场景
 *
 * 🔗 被打开：MainFeedFragment（点击搜索按钮时）
 * 🔗 复用组件：TagChipView（标签展示）
 *
 * ====== 框架说明 ======
 * 框架已搭建。搜索输入监听、结果展示、标签筛选需要你来写。
 */
public class SearchDialogFragment extends BottomSheetDialogFragment {

    private SearchViewModel viewModel;
    private EditText searchInput;
    private RecyclerView resultRecyclerView;
    private SearchResultAdapter adapter;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return super.onCreateDialog(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_search, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // ====== 第1步：绑定控件 ======
        searchInput = view.findViewById(R.id.search_input);
        resultRecyclerView = view.findViewById(R.id.search_results);

        // ====== 第2步：创建ViewModel ======
        SearchViewModelFactory factory = new SearchViewModelFactory(new MockAiService());
        viewModel = new ViewModelProvider(this, factory).get(SearchViewModel.class);

        // ====== 第3步：设置搜索结果列表 ======
        resultRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // TODO: 【你来写-中等】setupSearchInput()
        //
        // 思路：
        // 1. searchInput.addTextChangedListener 监听文字变化
        // 2. 用户输入后延迟300ms再搜索（防抖，避免每敲一个字都搜）
        //    💡 知识点：防抖(Debounce) = 用户停止输入后再执行搜索
        //    用 Handler + postDelayed 实现
        // 3. 调用 viewModel.doSearch(inputText)

        // TODO: 【你来写-中等】setupTagFilter()
        //
        // 思路：
        // 1. 展示一组热门标签（从viewModel获取或预设）
        // 2. 标签点击 → viewModel.filterByTag(tag)
        // 3. 复用 TagChipView 组件展示标签

        // TODO: 【你来写-中等】observeResults()
        //
        // 思路：
        // viewModel.searchResults.observe(this, results -> {
        //     adapter = new SearchResultAdapter(results);
        //     resultRecyclerView.setAdapter(adapter);
        // })
    }

    /**
     * 搜索结果 Adapter
     *
     * TODO: 【你来写-中等】SearchResultAdapter
     * 显示搜索匹配的广告卡片（用小图模式即可，不需要视频）
     */
    private static class SearchResultAdapter extends RecyclerView.Adapter<SearchResultAdapter.ViewHolder> {

        private List<AdItem> results;

        public SearchResultAdapter(List<AdItem> results) {
            this.results = results;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            // TODO: 【你来写-简单】加载小图卡片布局
            return null;
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            // TODO: 【你来写-中等】绑定数据
        }

        @Override
        public int getItemCount() {
            return results != null ? results.size() : 0;
        }

        static class ViewHolder extends RecyclerView.ViewHolder {
            public ViewHolder(@NonNull View itemView) {
                super(itemView);
            }
        }
    }

    /** ViewModel工厂 */
    static class SearchViewModelFactory implements ViewModelProvider.Factory {
        private final MockAiService aiService;
        SearchViewModelFactory(MockAiService aiService) { this.aiService = aiService; }

        @NonNull
        @Override
        public <T extends androidx.lifecycle.ViewModel> T create(@NonNull Class<T> modelClass) {
            return (T) new SearchViewModel(aiService);
        }
    }
}
