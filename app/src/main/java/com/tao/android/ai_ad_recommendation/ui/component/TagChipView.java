package com.tao.android.ai_ad_recommendation.ui.component;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.tao.android.ai_ad_recommendation.R;

/**
 * 自定义标签组件 - 圆角小标签，展示AI生成的广告标签
 *
 * 💡 知识点：自定义View = 组合已有的View来实现新功能
 *         这里用的是"组合"方式：一个LinearLayout包裹一个TextView
 *         ← 这是最常用的自定义View方式（不用从零画Canvas）
 *
 * 🔗 复用点：
 *    - AdCardView（广告卡片上的标签）
 *    - SearchDialogFragment（搜索页的热门标签）
 *    - DetailActivity（详情页的标签）
 *
 * ====== 框架说明 ======
 * 【已实现】不需要修改。用法：
 *   TagChipView chip = new TagChipView(context);
 *   chip.setText("科技");
 *   chip.setOnClickListener(v -> { ... });
 */
public class TagChipView extends LinearLayout {

    private TextView textView;

    public TagChipView(Context context) {
        this(context, null);
    }

    public TagChipView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TagChipView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        // Inflate标签布局（一个简单的TextView包裹在LinearLayout中）
        LayoutInflater.from(context).inflate(R.layout.view_tag_chip, this, true);
        textView = findViewById(R.id.tag_text);

        // 点击效果
        setClickable(true);
        setFocusable(true);
    }

    /** 设置标签文字 */
    public void setTagText(String text) {
        textView.setText(text);
    }

    /** 设置选中状态 */
    public void setSelected(boolean selected) {
        super.setSelected(selected);
        textView.setSelected(selected);
        if (selected) {
            textView.setBackgroundResource(R.drawable.bg_tag_selected);
            textView.setTextColor(getContext().getColor(android.R.color.white));
        } else {
            textView.setBackgroundResource(R.drawable.bg_tag_rounded);
            textView.setTextColor(getContext().getColor(R.color.tag_text_color));
        }
    }
}
