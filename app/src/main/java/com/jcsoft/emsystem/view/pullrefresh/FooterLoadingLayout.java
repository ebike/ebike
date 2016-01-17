package com.jcsoft.emsystem.view.pullrefresh;


import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.jcsoft.emsystem.R;

/**
 * 这个类封装了下拉刷新的布局
 * 
 * @author Li Hong
 * @since 2013-7-30
 */
public class FooterLoadingLayout extends LoadingLayout {

    private View mPullToLoadFooterContent;
    /**进度条*/
    private ProgressBar mProgressBar;
    /** 显示的文本 */
    private TextView mHintView;

    private View mFooterDivider;
    
    /**
     * 构造方法
     * 
     * @param context context
     */
    public FooterLoadingLayout(Context context) {
        super(context);
        init(context);
    }

    /**
     * 构造方法
     * 
     * @param context context
     * @param attrs attrs
     */
    public FooterLoadingLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    /**
     * 初始化
     * 
     * @param context context
     */
    private void init(Context context) {
        mPullToLoadFooterContent = findViewById(R.id.pull_to_load_footer_content);
        mProgressBar = (ProgressBar) findViewById(R.id.pull_to_load_footer_progressbar);
        mHintView = (TextView) findViewById(R.id.pull_to_load_footer_hint_textview);
        mFooterDivider = findViewById(R.id.pull_to_load_footer_divider);
        setState(State.RESET);
    }
    
    @Override
    protected View createLoadingView(Context context, AttributeSet attrs) {
        View container = LayoutInflater.from(context).inflate(R.layout.pull_to_load_footer, null);
        return container;
    }

    @Override
    public void setLastUpdatedLabel(CharSequence label) {}

    @Override
    public int getContentSize() {
        View view = findViewById(R.id.pull_to_load_footer_content);
        if (null != view) {
            return view.getHeight();
        }
        
        return (int) (getResources().getDisplayMetrics().density * 40);
    }
    

    protected void onStateChanged(State curState, State oldState) {
        mProgressBar.setVisibility(View.GONE);
        mHintView.setVisibility(View.GONE);
        
        super.onStateChanged(curState, oldState);
    }
    
    @Override
    protected void onReset() {
        mHintView.setText(R.string.pull_to_refresh_header_hint_loading);
    }

    @Override
    protected void onPullToRefresh() {
        //mHintView.setVisibility(View.VISIBLE);
        mHintView.setText(R.string.pull_to_refresh_header_hint_normal2);
    }

    @Override
    protected void onReleaseToRefresh() {
        mHintView.setVisibility(View.VISIBLE);
        mHintView.setText(R.string.pull_to_refresh_header_hint_ready);
        mFooterDivider.setVisibility(View.GONE);
    }

    @Override
    protected void onRefreshing() {
        mFooterDivider.setVisibility(View.GONE);
        mProgressBar.setVisibility(View.VISIBLE);
        mHintView.setVisibility(View.VISIBLE);
        mHintView.setText(R.string.pull_to_refresh_header_hint_loading);
    }
    
    @Override
    protected void onNoMoreData() {
        mPullToLoadFooterContent.setVisibility(View.GONE);
        mFooterDivider.setVisibility(View.VISIBLE);
    }

	@Override
	public void setHintText(String str) {}

    private boolean mShow;
    @Override
    public void show(boolean show) {
        // If is showing, do nothing.
        // If is showing, do nothing.
        if (show == mShow) {
            return;
        }
        mShow = show;

        ViewGroup.LayoutParams params = this.getLayoutParams();
        if (null != params) {
            if (show) {
                params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                mFooterDivider.setVisibility(View.GONE);
                mPullToLoadFooterContent.setVisibility(View.VISIBLE);
            } else {
                params.height = 1;
                mFooterDivider.setVisibility(View.VISIBLE);
                mPullToLoadFooterContent.setVisibility(View.GONE);
            }
            requestLayout();
        }
    }

    @Override
    protected void setFooterDivider(int visible) {
        if(mFooterDivider != null){
            mFooterDivider.setVisibility(visible);
        }
    }

    public void setFooterDividerColor(int color){
        if(mFooterDivider != null){
            mFooterDivider.setBackgroundColor(color);
        }
    }
}
