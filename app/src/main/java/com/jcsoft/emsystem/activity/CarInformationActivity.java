package com.jcsoft.emsystem.activity;

import android.os.Bundle;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jcsoft.emsystem.R;
import com.jcsoft.emsystem.base.BaseActivity;
import com.jcsoft.emsystem.bean.CarInfoBean;
import com.jcsoft.emsystem.bean.ResponseBean;
import com.jcsoft.emsystem.callback.DCommonCallback;
import com.jcsoft.emsystem.http.DHttpUtils;
import com.jcsoft.emsystem.http.DRequestParamsUtils;
import com.jcsoft.emsystem.http.HttpConstants;
import com.jcsoft.emsystem.view.RowLabelValueView;

import org.xutils.http.RequestParams;
import org.xutils.image.ImageOptions;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

/**
 * 车辆资料
 */
public class CarInformationActivity extends BaseActivity {
    @ViewInject(R.id.rlvv_brand_models)
    RowLabelValueView brandModelsRowLabelValueView;
    @ViewInject(R.id.rlvv_motor_number)
    RowLabelValueView motorNumberRowLabelValueView;
    @ViewInject(R.id.rlvv_frame_number)
    RowLabelValueView frameNumberRowLabelValueView;
    @ViewInject(R.id.rlvv_buy_date)
    RowLabelValueView buyDateRowLabelValueView;
    @ViewInject(R.id.rlvv_buy_price)
    RowLabelValueView buyPriceRowLabelValueView;
    @ViewInject(R.id.iv_car_photo)
    ImageView carPhotoImageView;
    //车辆信息
    private CarInfoBean carInfoBean;

    @Override
    public void loadXml() {
        setContentView(R.layout.activity_car_information);
        x.view().inject(this);
    }

    @Override
    public void getIntentData(Bundle savedInstanceState) {

    }

    @Override
    public void init() {
        if (carInfoBean != null) {
            brandModelsRowLabelValueView.setValue(carInfoBean.getCarBrand() + " " + carInfoBean.getCarModel());
            motorNumberRowLabelValueView.setValue(carInfoBean.getMotorNum());
            frameNumberRowLabelValueView.setValue(carInfoBean.getFrameNum());
            buyDateRowLabelValueView.setValue(carInfoBean.getCarDate());
            buyPriceRowLabelValueView.setValue("￥" + carInfoBean.getCarPrice());
            ImageOptions imageOptions = new ImageOptions.Builder()
                    .setLoadingDrawableId(R.mipmap.icon_default_ebike)
                    .setFailureDrawableId(R.mipmap.icon_default_ebike)
                    .setImageScaleType(ImageView.ScaleType.FIT_START)
                    .build();
            x.image().bind(carPhotoImageView, carInfoBean.getCarPic(), imageOptions);
        }
    }

    @Override
    public void setListener() {

    }

    @Override
    public void setData() {
        //获取车辆基本信息
        RequestParams params = DRequestParamsUtils.getRequestParams_Header(HttpConstants.getCarInfoUrl());
        DHttpUtils.get_String(this, true, params, new DCommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                ResponseBean<CarInfoBean> bean = new Gson().fromJson(result, new TypeToken<ResponseBean<CarInfoBean>>() {
                }.getType());
                if (bean.getCode() == 1) {
                    carInfoBean = bean.getData();
                    //更新界面
                    init();
                } else {
                    showShortText(bean.getErrmsg());
                }
            }
        });
    }
}
