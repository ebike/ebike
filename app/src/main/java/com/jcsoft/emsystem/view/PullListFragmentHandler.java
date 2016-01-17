package com.jcsoft.emsystem.view;

import android.os.Handler;
import android.os.Message;

import com.jcsoft.emsystem.fragment.BaseListFragment;
import com.jcsoft.emsystem.utils.CommonUtils;
import com.jcsoft.emsystem.view.pullrefresh.PullToRefreshListView;

/**
 * 封装加载的列表数据的handler
 * 适用于Fragment
 * Created by jimmy on 16/1/4.
 */
public class PullListFragmentHandler extends Handler {
    private static final int PULL_DOWN_TO_REFRESH = 0;
    private static final int PULL_UP_TO_REFRESH = 1;
    private static final int PULL_TO_REFRESH_COMPLETE = 2;
    private BaseListFragment fragment;
    private PullToRefreshListView pullToRefreshListView;

    public PullListFragmentHandler(BaseListFragment fragment, PullToRefreshListView pullToRefreshListView) {
        this.fragment = fragment;
        this.pullToRefreshListView = pullToRefreshListView;
    }

    @Override
    public void handleMessage(Message msg) {
        switch (msg.what) {
            case PULL_DOWN_TO_REFRESH:
                fragment.mPage = 1;
                fragment.hasLoadedOnce = false;
                fragment.requestDatas();
                break;
            case PULL_UP_TO_REFRESH:
                if (fragment.mIsMore) {
                    fragment.mPage++;
                    fragment.hasLoadedOnce = false;
                    fragment.requestDatas();
                } else {
                    pullToRefreshListView.doComplete(false);
                    CommonUtils.showShortText(fragment.getActivity(), "暂无更多数据");
                    pullToRefreshListView.setHasMoreData(false);
                }
                break;
            case PULL_TO_REFRESH_COMPLETE:
                pullToRefreshListView.setHasMoreData(fragment.mIsMore);
                if (fragment.mPage == 1) {//pull down refresh
                    pullToRefreshListView.doComplete(true);
                } else if (fragment.mPage > 1) {//pull up load more
                    pullToRefreshListView.doComplete(false);
                }
                break;
        }
    }
}