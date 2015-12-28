package com.jcsoft.emsystem.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jcsoft.emsystem.R;

import org.xutils.x;

/**
 * Created by jimmy on 15/12/28.
 */
public class LocationFragment extends BaseFragment {

    /**
     * 标志位，标志已经初始化完成
     */
    private boolean isPrepared;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_location, container, false);
        x.view().inject(this, view);
        isPrepared = true;

        return view;
    }

    @Override
    protected void requestDatas() {

    }
}
