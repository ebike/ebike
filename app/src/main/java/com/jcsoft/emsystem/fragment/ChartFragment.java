package com.jcsoft.emsystem.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jcsoft.emsystem.R;
import com.jcsoft.emsystem.activity.MainActivity;
import com.jcsoft.emsystem.bean.DayDataBean;
import com.jcsoft.emsystem.bean.ResponseBean;
import com.jcsoft.emsystem.callback.DCommonCallback;
import com.jcsoft.emsystem.constants.AppConfig;
import com.jcsoft.emsystem.db.XUtil;
import com.jcsoft.emsystem.http.DHttpUtils;
import com.jcsoft.emsystem.http.HttpConstants;
import com.jcsoft.emsystem.utils.CommonUtils;
import com.jcsoft.emsystem.utils.ReportUtils;

import org.xutils.common.util.KeyValue;
import org.xutils.db.sqlite.WhereBuilder;
import org.xutils.ex.DbException;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by jimmy on 15/12/28.
 */
public class ChartFragment extends BaseFragment implements View.OnClickListener {
    @ViewInject(R.id.bar_chart)
    BarChart barChart;
    @ViewInject(R.id.rl_today_mileage)
    RelativeLayout todayMileageRelativeLayout;
    @ViewInject(R.id.rl_average_speed)
    RelativeLayout averageSpeedRelativeLayout;
    @ViewInject(R.id.rl_max_speed)
    RelativeLayout maxSpeedRelativeLayout;
    @ViewInject(R.id.rl_min_speed)
    RelativeLayout minSpeedRelativeLayout;
    @ViewInject(R.id.tv_today_mileage)
    TextView todayMileageTextView;
    @ViewInject(R.id.tv_average_speed)
    TextView averageSpeedTextView;
    @ViewInject(R.id.tv_max_speed)
    TextView maxSpeedTextView;
    @ViewInject(R.id.tv_min_speed)
    TextView minSpeedTextView;
    @ViewInject(R.id.iv_today_mileage)
    ImageView todayMileageImageView;
    @ViewInject(R.id.iv_average_speed)
    ImageView averageSpeedImageView;
    @ViewInject(R.id.iv_max_speed)
    ImageView maxSpeedImageView;
    @ViewInject(R.id.iv_min_speed)
    ImageView minSpeedImageView;
    //标志位，标志已经初始化完成
    private boolean isPrepared;
    //统计数据
    private List<DayDataBean> dayDataBeans;
    //选中的统计(0：里程；1：平均速度；2：最大速度；3：最小速度)
    private int which;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chart, container, false);
        x.view().inject(this, view);
        isPrepared = true;
        initListener();
        return view;
    }

    private void initListener() {
        todayMileageRelativeLayout.setOnClickListener(this);
        averageSpeedRelativeLayout.setOnClickListener(this);
        maxSpeedRelativeLayout.setOnClickListener(this);
        minSpeedRelativeLayout.setOnClickListener(this);
    }

    @Override
    public void requestDatas() {
        if (!isPrepared || !isVisible || hasLoadedOnce || !isAdded()) {
            return;
        }
        //查询数据库中统计数据
        try {
            dayDataBeans = XUtil.db.selector(DayDataBean.class).where("carId", "=", AppConfig.userInfoBean.getCarId()).orderBy("date", false).findAll();
        } catch (DbException e) {
            e.printStackTrace();
        }
        //请求今日统计数据
        RequestParams params = new RequestParams(HttpConstants.getDayDataUrl(CommonUtils.DateToString(new Date(), "yyyy-MM-dd")));
        DHttpUtils.get_String((MainActivity) getActivity(), true, params, new DCommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                ResponseBean<DayDataBean> bean = new Gson().fromJson(result, new TypeToken<ResponseBean<DayDataBean>>() {
                }.getType());
                if (bean.getCode() == 1) {
                    boolean isUpdate = false;
                    if (dayDataBeans == null) {
                        dayDataBeans = new ArrayList<DayDataBean>();
                    } else {
                        if (dayDataBeans.get(dayDataBeans.size() - 1).getDate().equals(bean.getData().getDate())) {
                            isUpdate = true;
                        }
                    }
                    //数据入库
                    try {
                        if (isUpdate) {
                            dayDataBeans.remove(dayDataBeans.size() - 1);
                            dayDataBeans.add(bean.getData());
                            KeyValue[] keyValues = new KeyValue[4];
                            keyValues[0] = new KeyValue("maxSpeed", bean.getData().getMaxSpeed());
                            keyValues[1] = new KeyValue("minSpeed", bean.getData().getMinSpeed());
                            keyValues[2] = new KeyValue("avgSpeed", bean.getData().getAvgSpeed());
                            keyValues[3] = new KeyValue("mileage", bean.getData().getMileage());
                            XUtil.db.update(bean.getData().getClass(), WhereBuilder.b("date", "=", bean.getData().getDate()), keyValues);
                        } else {
                            dayDataBeans.add(bean.getData());
                            XUtil.db.save(bean.getData());
                        }
                        //初始报表
                        BarData mBarData = getBarData();
                        showChart(mBarData);
                        //赋值今日统计信息
                        initViews();
                    } catch (DbException e) {
                        e.printStackTrace();
                    }
                } else {
                    showShortText(bean.getErrmsg());
                }
            }
        });

    }

    private void initViews() {
        todayMileageRelativeLayout.setBackgroundColor(getResources().getColor(R.color.white_chart));
        todayMileageImageView.setVisibility(View.VISIBLE);
        todayMileageTextView.setText(dayDataBeans.get(dayDataBeans.size() - 1).getMileage() + "公里");
        averageSpeedTextView.setText(dayDataBeans.get(dayDataBeans.size() - 1).getAvgSpeed() + "km/h");
        maxSpeedTextView.setText(dayDataBeans.get(dayDataBeans.size() - 1).getMaxSpeed() + "km/h");
        minSpeedTextView.setText(dayDataBeans.get(dayDataBeans.size() - 1).getMinSpeed() + "km/h");
    }

    private void showChart(BarData barData) {
        ReportUtils.setChartLayout(getActivity(), barChart, dayDataBeans.size());
        // 如果没有数据的时候，会显示这个
        barChart.setNoDataTextDescription("");
        barChart.setNoDataText("");
        barChart.setBackgroundColor(getResources().getColor(R.color.login_text_disabled));
        barChart.setDrawBorders(false);  ////是否在折线图上添加边框
        barChart.setDescription("");// 数据描述
        barChart.setDrawGridBackground(false); // 是否显示表格颜色
        barChart.setTouchEnabled(false); // 设置是否可以触摸
        barChart.setDragEnabled(false);// 是否可以拖拽
        barChart.setScaleEnabled(false);// 是否可以缩放
        barChart.setPinchZoom(false);//
        XAxis xAxis = barChart.getXAxis();
        xAxis.setDrawAxisLine(false);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setSpaceBetweenLabels(0);
        xAxis.setLabelsToSkip(0);
        YAxis leftAxis = barChart.getAxisLeft();
        leftAxis.setEnabled(false);
        YAxis rightAxis = barChart.getAxisRight();
        rightAxis.setEnabled(false);
        Legend mLegend = barChart.getLegend(); // 设置比例图标示
        mLegend.setEnabled(false);
        barChart.fitScreen();
        barChart.setData(barData); // 设置数据
        barChart.animateXY(1000, 1000); // 立即执行的动画
    }

    private BarData getBarData() {
        ArrayList<String> xValues = new ArrayList<String>();
        for (int i = 0; i < dayDataBeans.size(); i++) {
            xValues.add(dayDataBeans.get(i).getDate());
        }
        ArrayList<BarEntry> yValues = new ArrayList<BarEntry>();
        for (int i = 0; i < dayDataBeans.size(); i++) {
            switch (which) {
                case 0:
                    yValues.add(new BarEntry((float) dayDataBeans.get(i).getMileage(), i));
                    break;
                case 1:
                    yValues.add(new BarEntry((float) dayDataBeans.get(i).getAvgSpeed(), i));
                    break;
                case 2:
                    yValues.add(new BarEntry((float) dayDataBeans.get(i).getMaxSpeed(), i));
                    break;
                case 3:
                    yValues.add(new BarEntry((float) dayDataBeans.get(i).getMinSpeed(), i));
                    break;
            }
        }
        // y轴的数据集合
        BarDataSet barDataSet = new BarDataSet(yValues, "");
        barDataSet.setColor(getResources().getColor(R.color.orange_chart));// 显示颜色
        barDataSet.setDrawValues(false);
        ArrayList<BarDataSet> barDataSets = new ArrayList<BarDataSet>();
        barDataSets.add(barDataSet);
        BarData barData = new BarData(xValues, barDataSets);
        return barData;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_today_mileage:
                todayMileageRelativeLayout.setBackgroundColor(getResources().getColor(R.color.white_chart));
                averageSpeedRelativeLayout.setBackgroundColor(getResources().getColor(R.color.gray_bg));
                maxSpeedRelativeLayout.setBackgroundColor(getResources().getColor(R.color.gray_bg));
                minSpeedRelativeLayout.setBackgroundColor(getResources().getColor(R.color.gray_bg));
                todayMileageImageView.setVisibility(View.VISIBLE);
                averageSpeedImageView.setVisibility(View.GONE);
                maxSpeedImageView.setVisibility(View.GONE);
                minSpeedImageView.setVisibility(View.GONE);
                //初始报表
                showChart(getBarData());
                break;
            case R.id.rl_average_speed:
                todayMileageRelativeLayout.setBackgroundColor(getResources().getColor(R.color.gray_bg));
                averageSpeedRelativeLayout.setBackgroundColor(getResources().getColor(R.color.white_chart));
                maxSpeedRelativeLayout.setBackgroundColor(getResources().getColor(R.color.gray_bg));
                minSpeedRelativeLayout.setBackgroundColor(getResources().getColor(R.color.gray_bg));
                todayMileageImageView.setVisibility(View.GONE);
                averageSpeedImageView.setVisibility(View.VISIBLE);
                maxSpeedImageView.setVisibility(View.GONE);
                minSpeedImageView.setVisibility(View.GONE);
                //初始报表
                showChart(getBarData());
                break;
            case R.id.rl_max_speed:
                todayMileageRelativeLayout.setBackgroundColor(getResources().getColor(R.color.gray_bg));
                averageSpeedRelativeLayout.setBackgroundColor(getResources().getColor(R.color.gray_bg));
                maxSpeedRelativeLayout.setBackgroundColor(getResources().getColor(R.color.white_chart));
                minSpeedRelativeLayout.setBackgroundColor(getResources().getColor(R.color.gray_bg));
                todayMileageImageView.setVisibility(View.GONE);
                averageSpeedImageView.setVisibility(View.GONE);
                maxSpeedImageView.setVisibility(View.VISIBLE);
                minSpeedImageView.setVisibility(View.GONE);
                //初始报表
                showChart(getBarData());
                break;
            case R.id.rl_min_speed:
                todayMileageRelativeLayout.setBackgroundColor(getResources().getColor(R.color.gray_bg));
                averageSpeedRelativeLayout.setBackgroundColor(getResources().getColor(R.color.gray_bg));
                maxSpeedRelativeLayout.setBackgroundColor(getResources().getColor(R.color.gray_bg));
                minSpeedRelativeLayout.setBackgroundColor(getResources().getColor(R.color.white_chart));
                todayMileageImageView.setVisibility(View.GONE);
                averageSpeedImageView.setVisibility(View.GONE);
                maxSpeedImageView.setVisibility(View.GONE);
                minSpeedImageView.setVisibility(View.VISIBLE);
                //初始报表
                showChart(getBarData());
                break;
        }
    }
}
