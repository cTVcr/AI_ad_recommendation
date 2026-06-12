package com.tao.android.ai_ad_recommendation.ui.screen;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.tao.android.ai_ad_recommendation.R;
import com.tao.android.ai_ad_recommendation.model.AdItem;
import com.tao.android.ai_ad_recommendation.model.ChatMessage;
import com.tao.android.ai_ad_recommendation.viewmodel.SearchViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * 对话式广告推荐 — 微信聊天风格
 *
 * API接入点: SearchViewModel.callAiApi() 预留了注释框架
 * 接入后把 ViewModel 里的 MockAiService 替换为 Retrofit 调用即可
 */
public class SearchDialogFragment extends BottomSheetDialogFragment {

    private SearchViewModel viewModel;
    private EditText input;
    private RecyclerView chatList;
    private ChatAdapter adapter;
    private List<AdItem> allAds;

    @Nullable @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_search, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        input = view.findViewById(R.id.search_input);
        TextView sendBtn = view.findViewById(R.id.search_send);
        chatList = view.findViewById(R.id.search_chat_list);
        view.findViewById(R.id.search_close).setOnClickListener(v -> dismiss());

        chatList.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new ChatAdapter();
        chatList.setAdapter(adapter);

        viewModel = new ViewModelProvider(this).get(SearchViewModel.class);
        viewModel.init(allAds != null ? allAds : new ArrayList<>());

        // 观察对话历史 → 刷新聊天列表（延迟滚动避免和RecyclerView布局冲突）
        final android.os.Handler h = new android.os.Handler();
        viewModel.chatHistory.observe(this, msgs -> {
            adapter.setMessages(msgs);
            h.postDelayed(() -> {
                if (adapter.getItemCount() > 0)
                    chatList.smoothScrollToPosition(adapter.getItemCount() - 1);
            }, 100);
        });

        // 发送
        View.OnClickListener onSend = v -> {
            String text = input.getText().toString().trim();
            if (!text.isEmpty()) {
                viewModel.sendMessage(text);
                input.setText("");
            }
        };
        sendBtn.setOnClickListener(onSend);
        input.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEND) { onSend.onClick(v); return true; }
            return false;
        });
    }

    public void setAllAds(List<AdItem> ads) { this.allAds = ads; }

    // ═══════════════════════════════════════════
    // 聊天 Adapter
    // ═══════════════════════════════════════════
    private static class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private static final int TYPE_USER = 1;
        private static final int TYPE_AI_TEXT = 2;
        private static final int TYPE_AI_ADS = 3;

        private List<ChatMessage> messages = new ArrayList<>();

        void setMessages(List<ChatMessage> msgs) {
            this.messages = msgs != null ? msgs : new ArrayList<>();
            notifyDataSetChanged();
        }

        @Override
        public int getItemViewType(int pos) {
            ChatMessage msg = messages.get(pos);
            if (ChatMessage.ROLE_USER.equals(msg.getRole())) return TYPE_USER;
            if (msg.getAdResults() != null && !msg.getAdResults().isEmpty()) return TYPE_AI_ADS;
            return TYPE_AI_TEXT;
        }

        @NonNull @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup p, int type) {
            LayoutInflater inf = LayoutInflater.from(p.getContext());
            if (type == TYPE_USER)
                return new BubbleVH(inf.inflate(R.layout.item_chat_user, p, false));
            else
                return new AiVH(inf.inflate(R.layout.item_chat_ai, p, false));
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder h, int pos) {
            ChatMessage msg = messages.get(pos);
            if (h instanceof BubbleVH) {
                ((BubbleVH) h).textView.setText(msg.getText());
            } else if (h instanceof AiVH) {
                AiVH vh = (AiVH) h;
                vh.textView.setText(msg.getText());
                List<AdItem> ads = msg.getAdResults();
                vh.adCards.removeAllViews();
                if (ads != null && !ads.isEmpty()) {
                    vh.adCards.setVisibility(View.VISIBLE);
                    for (AdItem ad : ads) {
                        View card = LayoutInflater.from(vh.itemView.getContext())
                                .inflate(R.layout.item_chat_ad_card, vh.adCards, false);
                        TextView title = card.findViewById(R.id.chat_ad_title);
                        TextView tags = card.findViewById(R.id.chat_ad_tags);
                        title.setText(ad.getTitle());
                        tags.setText(ad.getTags().replace(",", "  "));
                        card.setOnClickListener(cc -> {
                            Intent i = new Intent(cc.getContext(), DetailActivity.class);
                            i.putExtra("ad_item", ad);
                            cc.getContext().startActivity(i);
                        });
                        vh.adCards.addView(card);
                    }
                } else {
                    vh.adCards.setVisibility(View.GONE);
                }
            }
        }

        @Override public int getItemCount() { return messages.size(); }
    }

    // 纯文字气泡
    static class BubbleVH extends RecyclerView.ViewHolder {
        TextView textView;
        BubbleVH(@NonNull View v) { super(v); textView = v.findViewById(R.id.chat_bubble_text); }
    }

    // AI气泡 + 广告卡片
    static class AiVH extends RecyclerView.ViewHolder {
        TextView textView;
        LinearLayout adCards;
        AiVH(@NonNull View v) {
            super(v);
            textView = v.findViewById(R.id.chat_bubble_text);
            adCards = v.findViewById(R.id.chat_ai_ad_cards);
        }
    }
}
