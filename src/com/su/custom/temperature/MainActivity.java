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
        int[] data1 = { 30, 39, 33, 40, 30, 20, 30, 37, 38, 39, 39, 30, 37, 38, 40, 40, 39, 39, 40, 40, 30, 39, 33, 40, 30, 20, 30, 37, 38, 39, 39, 30, 37, 38, 40, 40, 39, 39, 40, 40, 30, 39, 33, 40,
                30, 20, 30, 37, 38, 39, 39, 30, 37, 38, 40, 40, 39, 39, 40, 40 };

        int[] data2 = { 38, 38, 38, 53, 40, 39, 38, 37, 38, 37, 38, 40 };
        List<int[]> dataList = new ArrayList<int[]>();
        dataList.add(data1);
        dataList.add(data2);

        List<Integer> colorList = new ArrayList<Integer>();
        colorList.add(R.color.predict_line);
        colorList.add(R.color.real_line);

        curver_chart_view.updateData(dataList, colorList);
    }

    private void initListener() {
        btn_real.setOnClickListener(this);
        btn_predictor.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
        case R.id.btn_real:

            break;
        case R.id.btn_predictor:

            break;
        default:
            break;
        }
    }

}
