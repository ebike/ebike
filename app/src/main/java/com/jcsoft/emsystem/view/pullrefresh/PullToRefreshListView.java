package com.jcsoft.emsystem.view.pullrefresh;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.jcsoft.emsystem.R;
import com.jcsoft.emsystem.view.pullrefresh.ILoadingLayout.State;

/**
 * 这个类实现了ListView下拉刷新，上加载更多和滑到底部自动加载
 *
 * @author Li Hong
 * @since 2013-8-15
 */
public class PullToRefreshListView extends PullToRefreshBase<ListView> implements OnScrollListener {

    /**
     * ListView
     */
    private ListView mListView;
    /**
     * 用于滑到底部自动加载的Footer
     */
    private LoadingLayout mLoadMoreFooterLayout;
    /**
     * 滚动的监听器
     */
    private OnScrollListener mScrollListener;

    /**
     * 构造方法
     *
     * @param context context
     */
    public PullToRefreshListView(Context context) {
        this(context, null);
    }

    /**
     * 构造方法
     *
     * @param context context
     * @param attrs   attrs
     */
    public PullToRefreshListView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    /**
     * 构造方法
     *
     * @param context  context
     * @param attrs    attrs
     * @param defStyle defStyle
     */
    public PullToRefreshListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        setPullLoadEnabled(false);
    }

    @Override
    protected ListView createRefreshableView(Context context, AttributeSet attrs) {
        View view = LayoutInflater.from(context).inflate(R.layout.pull_listview, null);
        ListView listView = null;
        if (view instanceof ListView) {
            listView = (ListView) view;
        } else {
            listView = new ListView(context);
        }
        mListView = listView;
        mListView.setVerticalScrollBarEnabled(true);
        listView.setOnScrollListener(this);
        return listView;
    }

    /**
     * 设置是否有更多数据的标志
     *
     * @param hasMoreData true表示还有更多的数据，false表示没有更多数据了
     */
    public void setHasMoreData(boolean hasMoreData) {
        if (!hasMoreData) {
            if (null != mLoadMoreFooterLayout) {
                mLoadMoreFooterLayout.setState(State.NO_MORE_DATA);
                mLoadMoreFooterLayout.show(false);
                setFooterDivider();
            }
            LoadingLayout footerLoadingLayout = getFooterLoadingLayout();
            if (null != footerLoadingLayout) {
                footerLoadingLayout.setState(State.NO_MORE_DATA);
            }
        } else {
            mLoadMoreFooterLayout.show(true);
            mLoadMoreFooterLayout.setState(State.RESET);
        }
    }

    public void setDriverLine() {
        mListView.setDivider(null);
    }

    private void setFooterDivider() {
        if (mListView != null) {
            int childCount = mListView.getChildCount();
            if (childCount > 0) {
                mLoadMoreFooterLayout.setFooterDivider(View.GONE);
            /*}else if(childCount > 1){
                mLoadMoreFooterLayout.setFooterDivider(View.VISIBLE);
            } else {
                mLoadMoreFooterLayout.setFooterDivider(View.GONE);*/
            }
        }
    }

    /**
     * 设置滑动的监听器
     *
     * @param l 监听器
     */
    public void setOnScrollListener(OnScrollListener l) {
        mScrollListener = l;
    }

    @Override
    protected boolean isReadyForPullUp() {
        return isLastItemVisible();
    }

    @Override
    protected boolean isReadyForPullDown() {
        return isFirstItemVisible();
    }

    @Override
    protected void startLoading() {
        super.startLoading();

        if (null != mLoadMoreFooterLayout) {
            mLoadMoreFooterLayout.setState(State.REFRESHING);
        }
    }

    @Override
    public void onPullUpRefreshComplete() {
        super.onPullUpRefreshComplete();

        if (null != mLoadMoreFooterLayout) {
            mLoadMoreFooterLayout.setState(State.RESET);
        }
    }

    @Override
    public void setScrollLoadEnabled(boolean scrollLoadEnabled) {


        if (isScrollLoadEnabled() == scrollLoadEnabled) {
            return;
        }

        super.setScrollLoadEnabled(scrollLoadEnabled);

        if (scrollLoadEnabled) {
            // 设置Footer
            if (null == mLoadMoreFooterLayout) {
                mLoadMoreFooterLayout = new FooterLoadingLayout(getContext());
            }
            if (null == mLoadMoreFooterLayout.getParent()) {
                mListView.addFooterView(mLoadMoreFooterLayout, null, false);
            }

            mLoadMoreFooterLayout.show(true);
        } else {
            if (null != mLoadMoreFooterLayout) {
                mLoadMoreFooterLayout.show(false);
            }
        }
    }

    @Override
    public LoadingLayout getFooterLoadingLayout() {
        if (isScrollLoadEnabled()) {
            return mLoadMoreFooterLayout;
        }

        return super.getFooterLoadingLayout();
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (isScrollLoadEnabled() && hasMoreData()) {
            if (scrollState == OnScrollListener.SCROLL_STATE_IDLE
                    || scrollState == OnScrollListener.SCROLL_STATE_FLING) {
                if (isReadyForPullUp()) {
                    startLoading();
                }
            }
        }

        if (null != mScrollListener) {
            mScrollListener.onScrollStateChanged(view, scrollState);
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (null != mScrollListener) {
            mScrollListener.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
        }
    }

    @Override
    protected LoadingLayout createHeaderLoadingLayout(Context context, AttributeSet attrs) {
        return new RotateLoadingLayout(context);
    }

    /**
     * 表示是否还有更多数据
     *
     * @return true表示还有更多数据
     */
    private boolean hasMoreData() {
        if ((null != mLoadMoreFooterLayout) && (mLoadMoreFooterLayout.getState() == State.NO_MORE_DATA)) {
            return false;
        }

        return true;
    }

    /**
     * 判断第一个child是否完全显示出来
     *
     * @return true完全显示出来，否则false
     */
    private boolean isFirstItemVisible() {
        final Adapter adapter = mListView.getAdapter();

        if (null == adapter || adapter.isEmpty()) {
            return true;
        }

        int mostTop = (mListView.getChildCount() > 0) ? mListView.getChildAt(0).getTop() : 0;
        if (mostTop >= 0) {
            return true;
        }

        return false;
    }

    /**
     * 判断最后一个child是否完全显示出来
     *
     * @return true完全显示出来，否则false
     */
    private boolean isLastItemVisible() {
        final Adapter adapter = mListView.getAdapter();

        if (null == adapter || adapter.isEmpty()) {
            return true;
        }

        final int lastItemPosition = adapter.getCount() - 1;
        final int lastVisiblePosition = mListView.getLastVisiblePosition();

        /**
         * This check should really just be: lastVisiblePosition == lastItemPosition, but ListView
         * internally uses a FooterView which messes the positions up. For me we'll just subtract
         * one to account for it and rely on the inner condition which checks getBottom().
         */
        if (lastVisiblePosition >= lastItemPosition - 1) {
            final int childIndex = lastVisiblePosition - mListView.getFirstVisiblePosition();
            final int childCount = mListView.getChildCount();
            final int index = Math.min(childIndex, childCount - 1);
            final View lastVisibleChild = mListView.getChildAt(index);
            if (lastVisibleChild != null) {
                return lastVisibleChild.getBottom() <= mListView.getBottom();
            }
        }

        return false;
    }

    public void setOnItemClickListener(AdapterView.OnItemClickListener listener) {
        if (listener != null) {
            mListView.setOnItemClickListener(listener);
        }
    }

    public void setAdapter(BaseAdapter adapter) {
        mListView.setAdapter(adapter);
    }

    public LoadingLayout getLoadingLayout() {
        return mLoadMoreFooterLayout;
    }

    //
    public void doPullRefreshing(final boolean smoothScroll, final long delayMillis) {
        super.doPullRefreshing(smoothScroll, delayMillis);
        if (null != mLoadMoreFooterLayout) {
            mLoadMoreFooterLayout.show(false);
        }
        //setLastUpdateTime();
    }

    public void setSelection(int pos) {
        mListView.setSelection(pos);
    }

    public void setEmptyView(View view) {
        mListView.setEmptyView(view);
    }

    public void setFooterDivider(int visible) {
        if (null != mLoadMoreFooterLayout) {
            mLoadMoreFooterLayout.setFooterDivider(visible);
        }
    }

    public void setVisibility(int visibility) {
        super.setVisibility(visibility);

    }

    public View getAdapterGetView(int id) {
        View view = null;
        if (mListView != null) {
            view = (View) mListView.getAdapter().getItem(id);
        }
        return view;
    }

}
