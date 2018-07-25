package com.su.custom.temperature;

import java.util.ArrayList;
import java.util.List;

import com.su.custom.temperature.view.CurveChartView;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity implements OnClickListener {

    private List<Float> realDatas;
    private List<Float> predictDatas;

    private Button btn_real;
    private Button btn_clear;
    private Button btn_predictor;
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
        btn_real = (Button) findViewById(R.id.btn_real);
        btn_clear = (Button) findViewById(R.id.btn_clear);
        btn_predictor = (Button) findViewById(R.id.btn_predictor);
        curver_chart_view = (CurveChartView) findViewById(R.id.curver_chart_view);
    }

    private void initValue() {
        realDatas = new ArrayList<Float>();
        for (int i = 0; i < CurveChartView.X_SIZE; i++) {
            realDatas.add((float) CurveChartView.Y_OFFSET);
        }

        predictDatas = new ArrayList<Float>();
        for (int i = 0; i < CurveChartView.X_SIZE; i++) {
            predictDatas.add(getRandomValue());
        }
    }

    private void initListener() {
        btn_real.setOnClickListener(this);
        btn_clear.setOnClickListener(this);
        btn_predictor.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
        case R.id.btn_real:
            addReadDataLine();
            break;
        case R.id.btn_predictor:
            curver_chart_view.updatePredictData(predictDatas);
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

    private void addReadDataLine() {
        int indexStart = CurveChartView.X_SIZE / 2;
        for (int i = 0; i < CurveChartView.X_SIZE; i++) {
            if (indexStart == i) {
                realDatas.set(indexStart, getRandomValue());
                realDatas.set(indexStart + 1, getRandomValue());
            }
        }
        curver_chart_view.updateRealData(realDatas);
    }

    private void clearData() {
        curver_chart_view.clearData();
    }
}
