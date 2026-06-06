package com.tao.android.ai_ad_recommendation.ui.component;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * 统计柱状图 - 自定义绘制View（展示曝光/点击/点赞/收藏数据）
 *
 * 💡 知识点：自定义View的高级用法 — 用Canvas自己画
 *         这是你最深入学习的组件！
 *         三个核心方法：onMeasure(计算尺寸) → onLayout(确认位置) → onDraw(画！)
 *         这里只需要重写 onDraw
 *
 * 🔗 被使用：DetailActivity（在详情页展示统计数据）
 *
 * ====== 框架说明 ======
 * onDraw 中的绘制逻辑需要你来写。
 *
 * Canvas核心API回顾：
 *   canvas.drawRect(left, top, right, bottom, paint)  // 画矩形
 *   canvas.drawText(text, x, y, paint)                 // 画文字
 *   paint.setColor(...) / paint.setTextSize(...)       // 设置画笔属性
 */
public class StatsChartView extends View {

    private Paint barPaint;
    private Paint textPaint;

    // 数据
    private List<BarData> dataList = new ArrayList<>();

    // 颜色
    private static final int[] COLORS = {
        Color.parseColor("#4CAF50"),  // 绿色: 曝光
        Color.parseColor("#2196F3"),  // 蓝色: 点击
        Color.parseColor("#F44336"),  // 红色: 点赞
        Color.parseColor("#FF9800"),  // 橙色: 收藏
    };

    public StatsChartView(Context context) {
        this(context, null);
    }

    public StatsChartView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StatsChartView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaints();
    }

    private void initPaints() {
        barPaint = new Paint();
        barPaint.setStyle(Paint.Style.FILL);
        barPaint.setAntiAlias(true);

        textPaint = new Paint();
        textPaint.setColor(Color.DKGRAY);
        textPaint.setTextSize(36f);
        textPaint.setAntiAlias(true);
        textPaint.setTextAlign(Paint.Align.CENTER);
    }

    /**
     * 设置图表数据
     *
     * @param dataList 柱状图数据
     */
    public void setData(List<BarData> dataList) {
        this.dataList = dataList;
        invalidate();  // 💡 知识点：invalidate() = 通知系统"我要重绘了"
    }

    /**
     * 核心绘制方法
     *
     * TODO: 【你来写-挑战】onDraw()
     *
     * 思路：
     * 1. 计算每个柱子的宽度：getWidth() / dataList.size() / 2
     *    每个柱子占一半宽度，留一半间距
     * 2. 找出数据中的最大值 → 用于计算柱子高度比例
     *    float maxValue = dataList中最大的value
     *    float scale = (getHeight() - 100) / maxValue  // 留100px给文字
     * 3. 遍历 dataList：
     *    - float barHeight = item.value * scale
     *    - float left = i * barWidth * 2 + barWidth / 2
     *    - float top = getHeight() - barHeight - 80
     *    - float right = left + barWidth
     *    - float bottom = getHeight() - 80
     *    - barPaint.setColor(COLORS[i % COLORS.length])
     *    - canvas.drawRect(left, top, right, bottom, barPaint)
     *    - canvas.drawText(item.label, left + barWidth/2, getHeight() - 30, textPaint)
     *    - canvas.drawText(String.valueOf(item.value), left + barWidth/2, top - 10, textPaint)
     *
     * 💡 知识点：Canvas坐标系
     *         (0,0) 在左上角，x向右增大，y向下增大
     *         所以"柱子从底部网上画"需要 top = 底部 - 高度
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (dataList.isEmpty()) {
            // 没有数据时显示提示文字
            canvas.drawText("暂无统计数据", getWidth() / 2f, getHeight() / 2f, textPaint);
            return;
        }

        // TODO: 【你来写-挑战】绘制柱状图
        // ====== 你的代码从这里开始 ======

        // ====== 你的代码到这里结束 ======
    }

    /**
     * 柱状图数据类
     */
    public static class BarData {
        public String label;   // 标签（如"曝光"、"点击"）
        public int value;      // 数值

        public BarData(String label, int value) {
            this.label = label;
            this.value = value;
        }
    }
}
