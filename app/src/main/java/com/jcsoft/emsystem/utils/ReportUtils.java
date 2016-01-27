package com.jcsoft.emsystem.utils;

import android.app.Activity;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.text.DecimalFormat;

/**
 * Created by jimmy on 2015/11/2.
 */
public class ReportUtils {

    public static ValueFormatter formatter = new ValueFormatter() {
        @Override
        public String getFormattedValue(float v, Entry entry, int i, ViewPortHandler viewPortHandler) {
            return new DecimalFormat("#").format(v);
        }
    };

    public static ValueFormatter formatterOnePoint = new ValueFormatter() {
        @Override
        public String getFormattedValue(float v, Entry entry, int i, ViewPortHandler viewPortHandler) {
            return new DecimalFormat("0.0").format(v);
        }
    };
    //处理超过1000的
    public static ValueFormatter handleKFormatter = new ValueFormatter() {
        @Override
        public String getFormattedValue(float v, Entry entry, int i, ViewPortHandler viewPortHandler) {
            if (v >= 1000000) {
                return new DecimalFormat("#.##").format(v / 1000000) + "M";
            } else if (v >= 1000) {
                return new DecimalFormat("#.##").format(v / 1000) + "k";
            } else {
                return new DecimalFormat("#").format(v);
            }

        }
    };

    /**
     * 设置图表的宽高
     *
     * @param context
     * @param chart
     * @param xValueCount:X轴显示项个数
     */
    public static void setChartLayout(Activity context, Chart chart, int xValueCount) {
        ViewGroup.LayoutParams params = null;
        //如果横项数据项过多，超出屏幕宽度，则，每项的宽度设为48dp
        int width = xValueCount * DensityUtil.dip2px(context, 48);
        int screenWidth = DensityUtil.screenWidth();
        if (width > screenWidth) {
            params = new LinearLayout.LayoutParams(width, ViewGroup.LayoutParams.MATCH_PARENT);
        } else {
            params = new LinearLayout.LayoutParams(screenWidth, ViewGroup.LayoutParams.MATCH_PARENT);
        }
        chart.setLayoutParams(params);
    }

    /**
     * 设置图表的基本属性
     *
     * @param chart
     */
    public static void setChartBaseAttribute(Chart chart) {

    }

    public static void setYAxisAttribute(YAxis leftAxis) {
        leftAxis.setDrawAxisLine(false);
        leftAxis.setDrawLabels(true);
        //Y轴从0开始显示
        leftAxis.setStartAtZero(true);
        //Y轴坐标显示位置
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
    }
}
