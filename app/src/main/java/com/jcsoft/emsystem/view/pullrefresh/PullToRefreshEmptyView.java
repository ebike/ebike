package com.jcsoft.emsystem.view.pullrefresh;


import android.content.Context;
import android.util.AttributeSet;

public class PullToRefreshEmptyView extends PullToRefreshBase<EmptyViewForList>{


    public PullToRefreshEmptyView(Context context) {
        super(context);
    }

    public PullToRefreshEmptyView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PullToRefreshEmptyView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected EmptyViewForList createRefreshableView(Context context, AttributeSet attrs) {
        EmptyViewForList emptyViewForList = new EmptyViewForList(getContext());
        return emptyViewForList;
    }

    @Override
    protected boolean isReadyForPullDown() {
        return mRefreshableView.getScrollY() == 0;
    }

    @Override
    protected boolean isReadyForPullUp() {
        return false;
    }
}
