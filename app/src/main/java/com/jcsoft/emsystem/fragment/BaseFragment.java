package com.jcsoft.emsystem.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.widget.Toast;

/**
 * Created by dive on 2015/07/11.
 */
public abstract class BaseFragment extends Fragment {
    // Fragment当前状态是否可见
    protected boolean isVisible;
    //是否已被加载过一次，第二次就不再去请求数据了
    protected boolean hasLoadedOnce;

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (getUserVisibleHint()) {
            isVisible = true;
            onVisible();
        } else {
            isVisible = false;
            onInvisible();
        }
    }

    /**
     * 可见
     */
    protected void onVisible() {
        requestDatas();
    }

    /**
     * 不可见
     */
    protected void onInvisible() {

    }

    /**
     * 延迟加载 子类必须重写此方法
     */
    protected abstract void requestDatas();
    /**
     * 长文本提示
     */
    public void showLongText(String text) {
        Toast.makeText(getActivity(), text, Toast.LENGTH_LONG).show();
    }

    /**
     * 短文本提示
     */
    public void showShortText(String text) {
        Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show();
    }
}
