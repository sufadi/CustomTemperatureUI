package com.su.custom.temperature;

import java.util.ArrayList;
import java.util.List;

import com.su.custom.temperature.view.CurveChartView;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity implements OnClickListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    // 20 秒后
    private int predictStep = 20;
    private int predictPeriod = predictStep / CurveChartView.X_UINIT;

    private List<Float> realDatas;
    private List<Float> realDataSource;

    private List<Float> predictDatas;
    private List<Float> predictDatasSource;

    private Button btn_add;
    private Button btn_auto;
    private Button btn_clear;
    private CurveChartView curver_chart_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initValue();
        initListener();
    }

    private void initView() {
        btn_add = (Button) findViewById(R.id.btn_add);
        btn_auto = (Button) findViewById(R.id.btn_auto);
        btn_clear = (Button) findViewById(R.id.btn_clear);
        curver_chart_view = (CurveChartView) findViewById(R.id.curver_chart_view);
    }

    private void initValue() {
        realDatas = new ArrayList<Float>();
        realDataSource = new ArrayList<Float>();

        predictDatas = new ArrayList<Float>();
        predictDatasSource = new ArrayList<Float>();

        curver_chart_view.updateStep(predictStep);
        reset();
    }

    private void initListener() {
        btn_add.setOnClickListener(this);
        btn_auto.setOnClickListener(this);
        btn_clear.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
        case R.id.btn_add:
            addRealDataLine();
            addPredictDataLine();
            break;
        case R.id.btn_auto:
            break;
        case R.id.btn_clear:
            clearData();
            break;
        default:
            break;
        }
    }

    private float getRandomValue() {
        int num = (int) (Math.random() * 3);
        return (float) (35 + num + 0.5);
    }

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

    private void addPredictDataLine() {
        int size = predictDatasSource.size();
        float value = getRandomValue();
        final int PREDICT_START_X = CurveChartView.X_START + predictPeriod;
        if (size > PREDICT_START_X) {
            predictDatasSource.remove(0);
        }
        predictDatasSource.add(value);

        int j = 0;
        for (int i = predictDatasSource.size() - 1; i >= 0; i--) {
            int index = PREDICT_START_X - j;
            j++;
            predictDatas.set(index, predictDatasSource.get(i));
            Log.d(TAG, "predictData:" + i + ", index = " + index + ", value = " + predictDatasSource.get(i));
        }

        curver_chart_view.updatepPredict(value);
        curver_chart_view.updatePredictData(predictDatas);
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

    private void clearData() {
        reset();
        curver_chart_view.clearData();
    }
}
