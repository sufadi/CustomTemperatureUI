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

    private Button btn_real;
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
        btn_predictor = (Button) findViewById(R.id.btn_predictor);
        curver_chart_view = (CurveChartView) findViewById(R.id.curver_chart_view);
    }

    private void initValue() {

    }

    private void initListener() {
        btn_real.setOnClickListener(this);
        btn_predictor.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
        case R.id.btn_real:
            List<Integer> realDatas = new ArrayList<Integer>();
            for (int i = 0; i < 60; i++) {
                realDatas.add(getRandomValue());
            }
            curver_chart_view.updateRealData(realDatas);
            break;
        case R.id.btn_predictor:
            List<Integer> predictDatas = new ArrayList<Integer>();
            for (int i = 0; i < 60; i++) {
                predictDatas.add(getRandomValue());
            }
            curver_chart_view.updatePredictData(predictDatas);
            break;
        default:
            break;
        }
    }

    private int getRandomValue() {
        int num = (int) (Math.random() * 3);
        return 35 + num;
    }
}
