package com.su.custom.temperature.view;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.su.custom.temperature.R;

public class CurveChartView extends View {

    private static final String TAG = CurveChartView.class.getSimpleName();

    public static final int Y_START = 20;
    public static final int X_SIZE = 60;
    public static final int X_START = X_SIZE / 2;
    public static final int Y_SIZE = 60 - Y_START;

    public static final int TYPE_REAL = 0;
    public static final int TYPE_PREDICTE = 1;

    private Context mContext;
    // 坐标单位
    private String[] xLabel;
    private String[] yLabel;

    // 默认边距
    private int margin = 20;
    // 距离左边偏移量
    private int marginX = 30;
    // 原点坐标
    private int xPoint;
    private int yPoint;
    // X,Y轴的单位长度
    private int xScale;
    private int yScale;
    // 画笔
    private Paint paintAxes;
    private Paint paintCoordinate;
    // 画网格线
    private Paint paintTable;
    // 画曲线
    private Paint paintCurve;

    // 曲线数据
    private List<Float> realData;
    private List<Float> pridictData;

    // 自定义View有四个构造函数
    // 如果View是在Java代码里面new的，则调用第一个构造函数
    public CurveChartView(Context context) {
        super(context);
        // 在构造函数里初始化画笔的操作
        init(context);
    }

    // 如果View是在.xml里声明的，则调用第二个构造函数
    // 自定义属性是从AttributeSet参数传进来的
    public CurveChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
        typedArraySettings(context, attrs);
        init(context);

    }

    // 不会自动调用
    // 一般是在第二个构造函数里主动调用
    // 如View有style属性时
    public CurveChartView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
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
    private void init(Context context) {
        if (mContext == null) {
            this.mContext = context;
        }

        // 原点坐标
        xPoint = margin + marginX;
        yPoint = this.getHeight() - margin;
        Log.d(TAG, "xPoint = " + xPoint + ", yPoint = " + yPoint);
        // 缩放
        xLabel = getXLabel();
        yLabel = getYLabel();

        xScale = (this.getWidth() - 2 * margin - marginX) / (xLabel.length - 1);
        yScale = (this.getHeight() - 2 * margin) / (yLabel.length - 1);

        paintAxes = new Paint();
        paintAxes.setStyle(Paint.Style.STROKE);
        paintAxes.setAntiAlias(true);
        paintAxes.setDither(true);
        paintAxes.setColor(mContext.getResources().getColor(R.color.xy_system));
        paintAxes.setStrokeWidth(4);

        paintCoordinate = new Paint();
        paintCoordinate.setStyle(Paint.Style.STROKE);
        paintCoordinate.setDither(true);
        paintCoordinate.setAntiAlias(true);
        paintCoordinate.setColor(mContext.getResources().getColor(R.color.xy_system));
        paintCoordinate.setTextSize(15);

        paintTable = new Paint();
        paintTable.setStyle(Paint.Style.STROKE);
        paintTable.setAntiAlias(true);
        paintTable.setDither(true);
        paintTable.setColor(mContext.getResources().getColor(R.color.color4));
        paintTable.setStrokeWidth(2);

        paintCurve = new Paint();
        paintCurve.setStyle(Paint.Style.STROKE);
        paintCurve.setDither(true);
        paintCurve.setAntiAlias(true);
        paintCurve.setStrokeWidth(3);
        PathEffect pathEffect = new CornerPathEffect(25);
        paintCurve.setPathEffect(pathEffect);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        init(mContext);
        drawTable(canvas, paintTable);
        drawAxesLine(canvas, paintAxes);
        drawCoordinate(canvas, paintCoordinate);
        drawRealCurve(canvas);
        drawPredictCurve(canvas);
    }

    private String[] getXLabel() {
        String[] xLabel = new String[X_SIZE];
        for (int i = 0; i < X_SIZE; i++) {
            if (i == 0) {
                xLabel[i] = "30 秒前";
            } else if (i == X_SIZE / 2) {
                xLabel[i] = "当前";
            } else if (i == X_SIZE - 1) {
                xLabel[i] = "30 秒后";
            } else {
                xLabel[i] = "";
            }
        }

        return xLabel;
    }

    private String[] getYLabel() {
        String[] yLabel = new String[Y_SIZE];
        for (int i = 0; i < Y_SIZE; i++) {
            switch (i) {
            case 0:
                yLabel[i] = String.format("%d °c", i + Y_START);
                break;
            case 19:
            case 39:
            case 59:
                yLabel[i] = String.format("%d °c", i + Y_START + 1);
                break;
            default:
                yLabel[i] = "";
                break;
            }
        }
        return yLabel;
    }

    /**
     * 绘制坐标轴
     */
    private void drawAxesLine(Canvas canvas, Paint paint) {
        // X
        canvas.drawLine(xPoint, yPoint, this.getWidth() - margin / 6, yPoint, paint);
        canvas.drawLine(this.getWidth() - margin / 6, yPoint, this.getWidth() - margin / 2, yPoint - margin / 3, paint);
        canvas.drawLine(this.getWidth() - margin / 6, yPoint, this.getWidth() - margin / 2, yPoint + margin / 3, paint);

        // Y
        canvas.drawLine(xPoint, yPoint, xPoint, margin / 6, paint);
        canvas.drawLine(xPoint, margin / 6, xPoint - margin / 3, margin / 2, paint);
        canvas.drawLine(xPoint, margin / 6, xPoint + margin / 3, margin / 2, paint);
    }

    /**
     * 绘制表格
     */
    private void drawTable(Canvas canvas, Paint paint) {
        Path path = new Path();
        // 横向线
        for (int i = 0; (yPoint - i * yScale) >= margin; i++) {
            switch (i + Y_START) {
            case 20:
            case 25:
            case 30:
            case 35:
            case 40:
            case 45:
            case 50:
            case 55:
            case 60:
                int startX = xPoint;
                int startY = yPoint - i * yScale;
                int stopX = xPoint + (xLabel.length - 1) * xScale;
                path.moveTo(startX, startY);
                path.lineTo(stopX, startY);
                canvas.drawPath(path, paint);
                break;
            default:
                break;
            }

        }
    }

    /**
     * 绘制刻度
     */
    private void drawCoordinate(Canvas canvas, Paint paint) {
        // X轴坐标
        for (int i = 0; i <= (xLabel.length - 1); i++) {
            paint.setTextAlign(Paint.Align.CENTER);
            int startX = xPoint + i * xScale;
            canvas.drawText(xLabel[i], startX, this.getHeight() - margin / 6, paint);
        }

        // Y轴坐标
        for (int i = 0; i <= (yLabel.length - 1); i++) {
            paint.setTextAlign(Paint.Align.LEFT);
            int startY = yPoint - i * yScale;
            int offsetX;
            switch (yLabel[i].length()) {
            case 1:
                offsetX = 28;
                break;

            case 2:
                offsetX = 20;
                break;

            case 3:
                offsetX = 12;
                break;

            case 4:
                offsetX = 5;
                break;

            default:
                offsetX = 0;
                break;
            }
            int offsetY;
            if (i == 0) {
                offsetY = 0;
            } else {
                offsetY = margin / 5;
            }
            // x默认是字符串的左边在屏幕的位置，y默认是字符串是字符串的baseline在屏幕上的位置
            canvas.drawText(yLabel[i], margin / 4 + offsetX, startY + offsetY, paint);
        }
    }

    private void drawRealCurve(Canvas canvas) {
        if (realData != null && !realData.isEmpty()) {
            drawCurve(TYPE_REAL, canvas, paintCurve, realData);
        }
    }

    private void drawPredictCurve(Canvas canvas) {
        if (pridictData != null && !pridictData.isEmpty()) {
            drawCurve(TYPE_PREDICTE, canvas, paintCurve, pridictData);
        }
    }

    private void drawCurve(int type, Canvas canvas, Paint paint, List<Float> data) {
        switch (type) {
        case TYPE_PREDICTE:
            drawCurve(canvas, paint, data, R.color.predict_line);
            break;
        case TYPE_REAL:
            drawCurve(canvas, paint, data, R.color.real_line);
            break;
        default:
            break;
        }
    }

    /**
     * 绘制曲线
     */
    private void drawCurve(Canvas canvas, Paint paint, List<Float> data, int color) {
        paint.setColor(mContext.getResources().getColor(color));
        Path path = new Path();
        boolean isFist = true;

        for (int i = 0; i < data.size(); i++) {
            float y = toY(data.get(i));
            float x = xPoint + i * xScale;

            if (y == Y_START) {

            } else {
                if (isFist) {
                    path.moveTo(x, y);
                    Log.d(TAG, "moveTo x = " + x + ", y = " + y);
                    isFist = false;
                } else {
                    path.lineTo(x, y);
                    Log.d(TAG, "lineTo x = " + x + ", y = " + y);
                }
            }
        }
        canvas.drawPath(path, paint);
    }

    /**
     * 数据按比例转坐标
     */
    private float toY(float num) {
        if (num == Y_START) {
            return Y_START;
        }

        float y;
        try {
            y = yPoint - (num - Y_START) * yScale;
        } catch (Exception e) {
            return 0;
        }
        return y;
    }

    public void updateData(List<Float> real, List<Float> predict) {
        updateRealData(real);
        updatePredictData(predict);
    }

    public void updateRealData(List<Float> real) {
        if (realData == null) {
            realData = new ArrayList<Float>();
        }

        if (real != null) {
            realData.clear();
            realData.addAll(real);
        }

        invalidate();
    }

    public void updatePredictData(List<Float> predict) {
        if (pridictData == null) {
            pridictData = new ArrayList<Float>();
        }

        if (predict != null) {
            pridictData.clear();
            pridictData.addAll(predict);
        }

        invalidate();
    }

    public void clearData() {
        if (pridictData != null) {
            pridictData.clear();
        }

        if (realData != null) {
            realData.clear();
        }

        invalidate();
    }
}
