package com.jcsoft.emsystem.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jcsoft.emsystem.R;
import com.jcsoft.emsystem.activity.MainActivity;
import com.jcsoft.emsystem.callback.DCommonCallback;
import com.jcsoft.emsystem.http.DHttpUtils;
import com.jcsoft.emsystem.http.HttpConstants;

import org.xutils.http.RequestParams;
import org.xutils.x;

/**
 * Created by jimmy on 15/12/28.
 */
public class MyFragment extends BaseFragment {

    /**
     * 标志位，标志已经初始化完成
     */
    private boolean isPrepared;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my, container, false);
        x.view().inject(this, view);
        isPrepared = true;
        //获取车辆基本信息
        getCarInfo();
        return view;
    }

    @Override
    public void requestDatas() {

    }

    //获取车辆基本信息
    private void getCarInfo() {
        RequestParams params = new RequestParams(HttpConstants.getCarInfoUrl());
        DHttpUtils.get_String((MainActivity) getActivity(), false, params, new DCommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e("getCarInfo", "result:" + result);
            }
        });
    }
}
