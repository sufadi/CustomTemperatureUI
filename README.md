## 1.功能说明
曲线1为实际温度，曲线2为预测温度，实现2条曲线同时绘制

### 1.1 下载
https://github.com/sufadi/CustomTemperatureUI

Eclipse开发环境

apk 在CustomTemperatureUI\bin\CustomTemperatureUI.apk

### 1.2 博客
https://blog.csdn.net/su749520/article/details/81211841
或者本文

### 1.3 UI 效果
![这里写图片描述](https://img-blog.csdn.net/20180725224328213?watermark/2/text/aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3N1NzQ5NTIw/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70)

## 2. 自定义View的实现介绍
#### 1.1 简单介绍
继承View并重写onDraw实现坐标轴和曲线的绘制

```
package com.su.custom.temperature.view;

public class CurveChartView extends View {

    @Override
    protected void onDraw(Canvas canvas) {
    

```

#### 1.2 标题的绘制
这里主要获取文本的宽度和高度，进行drawText的居中置顶显示

```
    private void drawTitle(Canvas canvas, Paint paint) {
        String result = "温度变化与预测";
        float width = paint.measureText(result);
        FontMetrics fontMetrics = paint.getFontMetrics();
        float top = Math.abs(fontMetrics.top);
        canvas.drawText(result, (this.getWidth() - width) / 2, top, paint);
    }
```
#### 1.3 实时数据的绘制
通过获取文本的宽度，实现文本drawText的右边对齐和完整显示
```
    private void drawCurrentData(Canvas canvas, Paint paint) {
        String result = String.format("当前值: %.1f °C %d s后：%.1f °C", realValue, stepValue, predictValue);
        float width = paint.measureText(result);
        canvas.drawText(result, this.getWidth() - (width + 20), yPoint - (Y_SIZE) * yScale, paint);
    }
```

#### 1.4 坐标轴绘制
这里主要绘制X和Y的轴边和对应的箭头
```
    /**
     * 绘制坐标轴
     */
    private void drawAxesLine(Canvas canvas, Paint paint) {
        // X 的坐标轴边
        canvas.drawLine(xPoint, yPoint, this.getWidth() - margin / 6, yPoint, paint);
        // X 的箭头
        canvas.drawLine(this.getWidth() - margin / 6, yPoint, this.getWidth() - margin / 2, yPoint - margin / 3, paint);
        canvas.drawLine(this.getWidth() - margin / 6, yPoint, this.getWidth() - margin / 2, yPoint + margin / 3, paint);

        // Y 的坐标轴边
        canvas.drawLine(xPoint, yPoint, xPoint, margin / 6, paint);
        // Y 的箭头
        canvas.drawLine(xPoint, margin / 6, xPoint - margin / 3, margin / 2, paint);
        canvas.drawLine(xPoint, margin / 6, xPoint + margin / 3, margin / 2, paint);
    }
```

#### 1.5 绘制网格线
按客户要求定制显示网格线，即形成x,y的网格线
```
    /**
     * 绘制表格
     */
    private void drawTable(Canvas canvas, Paint paint) {
        Path path = new Path();
        // 横向线
        for (int i = 0; (yPoint - i * yScale) >= margin; i++) {
            switch (i + Y_START) {
            case 19:
            case 24:
            case 29:
            case 34:
            case 39:
            case 44:
            case 49:
            case 54:
            case 59:
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

        // 纵向线
        if (showXTable) {
            for (int i = 1; i * xScale <= (this.getWidth() - margin); i++) {
                int startX = xPoint + i * xScale;
                int startY = yPoint;
                int stopY = yPoint - (yLabel.length - 1) * yScale;
                path.moveTo(startX, startY);
                path.lineTo(startX, stopY);
                canvas.drawPath(path, paint);
            }
        }
    }
```

#### 1.6 坐标轴添加文本标签
根据上面的绘制网格线进行刻度划分，并客制化显示内容
```
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
```

#### 1.7 绘制曲线
主要通过path.moveTo和path.lineTo进行坐标位置的控制，最后canvas.drawPath实现曲线的绘制
```
    /**
     * 绘制曲线
     */
    private void drawCurve(Canvas canvas, Paint paint, List<Float> data, int color) {
        paint.setColor(mContext.getResources().getColor(color));
        paint.setAntiAlias(true);// 抗锯齿
        Path path = new Path();
        boolean isFist = true;
        float lastX = 0f;
        float lastY = 0f;

        for (int i = 0; i < data.size(); i++) {
            float y = toY(data.get(i));
            float x = xPoint + i * xScale;

            if (y == Y_START) {
                // do nothing
            } else {
                if (isFist) {
                    path.moveTo(x, y);
                    Log.d(TAG, "moveTo x = " + x + ", y = " + y);
                    isFist = false;

                } else {
                    path.lineTo(x, y);
                    Log.d(TAG, "lineTo x = " + x + ", y = " + y);
                }

                lastX = x;
                lastY = y;
            }
        }

        if (lastX != 0f && lastY != 0f) {
            canvas.drawCircle(lastX, lastY, 2, paint);
        }

        canvas.drawPath(path, paint);
    }
```

## 3. 调用实现
#### 3.1 添加一条曲线
由于要让数据实现实时更新效果，且realDataSource数据如果与容器大小相等，则舍去最早数据，新增最新数据，达到数据的实时更新

但是客户要求坐标的起点是不是坐标轴原点，故需要realDatas进行坐标位置的偏移转换

```
private void addRealDataLine() {
        Log.d(TAG, "X_START = " + CurveChartView.X_START);
        int size = realDataSource.size();
        float value = getRandomValue();
        if (size > CurveChartView.X_START) {
            realDataSource.remove(0);
        }

        realDataSource.add(value);

        int j = 0;
        for (int i = realDataSource.size() - 1; i >= 0; i--) {
            int index = CurveChartView.X_START - j;
            j++;
            realDatas.set(index, realDataSource.get(i));
            Log.d(TAG, "realDatas:" + i + ", index = " + index + ", value = " + realDataSource.get(i));
        }

        curver_chart_view.updateReal(value);
        curver_chart_view.updateRealData(realDatas);
    }

```

#### 3.2 清除所有数据

```
    private void clearData() {
        reset();
        curver_chart_view.clearData();
    }

    private void reset() {
        if (realDataSource != null) {
            realDataSource.clear();
        }

        if (realDatas != null) {
            realDatas.clear();
        }
        for (int i = 0; i < CurveChartView.X_SIZE; i++) {
            realDatas.add((float) CurveChartView.Y_START);
        }

        if (predictDatasSource != null) {
            predictDatasSource.clear();
        }

        if (predictDatas != null) {
            predictDatas.clear();
        }
        for (int i = 0; i < CurveChartView.X_SIZE; i++) {
            predictDatas.add((float) CurveChartView.Y_START);
        }
    }


```

## 4. 项目结语
本项目主要参考
https://blog.csdn.net/carson_ho/article/details/62037696

最开始是使用MPandchart的开源库实现曲线列表，但是发现很多客制化需求很难修改，做完这个小demo，深刻发觉，开源库是拿来参考的，如果是自己的产品尽量使用自己封装的方法和自定义，这样修改起来得心应手。
