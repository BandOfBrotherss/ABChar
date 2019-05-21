package com.example.abchar.ScreenActivities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Cartesian;
import com.anychart.core.axes.Linear;
import com.anychart.core.cartesian.series.Bar;
import com.anychart.data.Mapping;
import com.anychart.data.Set;
import com.anychart.enums.Anchor;
import com.anychart.enums.HoverMode;
import com.anychart.enums.LabelsOverlapMode;
import com.anychart.enums.Orientation;
import com.anychart.enums.ScaleStackMode;
import com.anychart.enums.TooltipDisplayMode;
import com.anychart.enums.TooltipPositionMode;
import com.example.abchar.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ChildInfo extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child_info);

        HashMap<String,Long> trueFalse = (HashMap<String, Long>) getIntent().getSerializableExtra("trueFalse");
        Log.d("CHILD_INFO", String.valueOf(trueFalse.get(3)));
        String name = getIntent().getStringExtra("name");

        AnyChartView anyChartView = findViewById(R.id.any_chart_view);
        anyChartView.setProgressBar(findViewById(R.id.progress_bar));

        ArrayList<String> labelList = getLabelList();

        Cartesian barChart = AnyChart.bar();

        barChart.animation(true);

        barChart.padding(10d, 20d, 5d, 20d);

        barChart.yScale().stackMode(ScaleStackMode.VALUE);

        barChart.yAxis(0).labels().format(
                "function() {\n" +
                        "    return Math.abs(this.value).toLocaleString();\n" +
                        "  }");

        barChart.yAxis(0d).title("True and False Tries CO");

        barChart.xAxis(0d).overlapMode(LabelsOverlapMode.ALLOW_OVERLAP);

        Linear xAxis1 = barChart.xAxis(3d);
        xAxis1.enabled(true);
        xAxis1.orientation(Orientation.RIGHT);
        xAxis1.overlapMode(LabelsOverlapMode.ALLOW_OVERLAP);

        barChart.title(name);

        barChart.interactivity().hoverMode(HoverMode.BY_X);

        barChart.tooltip()
                .title(false)
                .separator(false)
                .displayMode(TooltipDisplayMode.SEPARATED)
                .positionMode(TooltipPositionMode.POINT)
                .useHtml(true)
                .fontSize(12d)
                .offsetX(5d)
                .offsetY(0d)
                .format(
                        "function() {\n" +
                                "      return '<span style=\"color: #D9D9D9\">$</span>' + Math.abs(this.value).toLocaleString();\n" +
                                "    }");


        List<DataEntry> seriesData = new ArrayList<>();

        for(int i = 0; i < labelList.size(); i+=1) {
            seriesData.add(new CustomDataEntry(labelList.get(i), -1 * trueFalse.get(labelList.get(i) + "_F"), trueFalse.get(labelList.get(i) + "_T")));
        }

        Set set = Set.instantiate();
        set.data(seriesData);
        Mapping series1Data = set.mapAs("{ x: 'x', value: 'value' }");
        Mapping series2Data = set.mapAs("{ x: 'x', value: 'value2' }");

        Bar series1 = barChart.bar(series2Data);
        series1.name("True")
                .color("Green");
        series1.tooltip()
                .position("center")
                .anchor(Anchor.LEFT_CENTER);

        Bar series2 = barChart.bar(series1Data);
        series2.name("False")
                .color("Red");
        series2.tooltip()
                .position("center")
                .anchor(Anchor.RIGHT_CENTER);

        barChart.legend().enabled(true);
        barChart.legend().inverted(true);
        barChart.legend().fontSize(13d);
        barChart.legend().padding(0d, 0d, 20d, 0d);

        anyChartView.setChart(barChart);


    }

    private class CustomDataEntry extends ValueDataEntry {
        CustomDataEntry(String x, Number value, Number value2) {
            super(x, value);
            setValue("value2", value2);
        }
    }


    private ArrayList<String> getLabelList(){
        ArrayList<String> labelList = new ArrayList<>();
        labelList.add("0");
        labelList.add("1");
        labelList.add("2");
        labelList.add("3");
        labelList.add("4");
        labelList.add("A");
        labelList.add("B");
        labelList.add("C");
        labelList.add("D");
        labelList.add("E");
        return labelList;

    }
}
