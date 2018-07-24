package com.su.custom.temperature.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.su.custom.temperature.R;

public class CurveChartView extends View {

    // 设置画笔变量
    Paint mPaint1;

    // 自定义View有四个构造函数
    // 如果View是在Java代码里面new的，则调用第一个构造函数
    public CurveChartView(Context context) {
        super(context);

        // 在构造函数里初始化画笔的操作
        init();
    }

    // 如果View是在.xml里声明的，则调用第二个构造函数
    // 自定义属性是从AttributeSet参数传进来的
    public CurveChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
        typedArraySettings(context, attrs);
        init();

    }

    // 不会自动调用
    // 一般是在第二个构造函数里主动调用
    // 如View有style属性时
    public CurveChartView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void typedArraySettings(Context context, AttributeSet attrs) {
        // 加载自定义属性集合CircleView
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CurveChartView);

        // 解析集合中的属性CurveChartView属性
        // 该属性的id为:R.styleable.CurveChartView_bg_color,在R.java可以看到
        // 将解析的属性传入到画圆的画笔颜色变量当中（本质上是自定义画圆画笔的颜色）
        // 第二个参数是默认设置颜色（即无指定circle_color情况下使用）
        a.getColor(R.styleable.CurveChartView_bg_color, Color.RED);

        // 解析后释放资源
        a.recycle();
    }

    // 画笔初始化
    private void init() {

        // 创建画笔
        mPaint1 = new Paint();
        // 设置画笔颜色为蓝色
        mPaint1.setColor(Color.WHITE);
        // 设置画笔宽度为10px
        mPaint1.setStrokeWidth(5f);
        // 设置画笔模式为填充
        mPaint1.setStyle(Paint.Style.FILL);

    }

    @Override
    protected void onDraw(Canvas canvas) {

        super.onDraw(canvas);

        // 获取传入的padding值
        final int paddingLeft = getPaddingLeft();
        final int paddingRight = getPaddingRight();
        final int paddingTop = getPaddingTop();
        final int paddingBottom = getPaddingBottom();

        // 获取绘制内容的高度和宽度（考虑了四个方向的padding值）
        int width = getWidth() - paddingLeft - paddingRight;
        int height = getHeight() - paddingTop - paddingBottom;

        // 设置圆的半径 = 宽,高最小值的2分之1
        int r = Math.min(width, height) / 2;

        // 画出圆(蓝色)
        // 圆心 = 控件的中央,半径 = 宽,高最小值的2分之1
        canvas.drawCircle(paddingLeft + width / 2, paddingTop + height / 2, r, mPaint1);
    }

}
