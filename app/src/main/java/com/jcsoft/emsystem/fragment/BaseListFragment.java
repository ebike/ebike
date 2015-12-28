package com.jcsoft.emsystem.fragment;


/**
 * Created by jimmy on 2015/07/11.
 */
public abstract class BaseListFragment extends BaseFragment {
    protected static final int DELAY_MILLIS = 50;
    protected static final int PULL_DOWN_TO_REFRESH = 0;
    protected static final int PULL_UP_TO_REFRESH = 1;
    protected static final int PULL_TO_REFRESH_COMPLETE = 2;
    protected int mPage = 1;
    protected int mPageCount = 15;
    protected boolean mIsMore = false;

}
