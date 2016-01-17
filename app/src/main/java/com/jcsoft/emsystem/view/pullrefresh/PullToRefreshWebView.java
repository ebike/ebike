package com.jcsoft.emsystem.view.pullrefresh;

import android.content.Context;
import android.util.AttributeSet;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * 封装了WebView的下拉刷新
 * 
 * @author Li Hong
 * @since 2013-8-22
 */
public class PullToRefreshWebView extends PullToRefreshBase<WebView> {
    /**
     * 构造方法
     * 
     * @param context context
     */
    public PullToRefreshWebView(Context context) {
        this(context, null);
    }
    
    /**
     * 构造方法
     * 
     * @param context context
     * @param attrs attrs
     */
    public PullToRefreshWebView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }
    
    /**
     * 构造方法
     * 
     * @param context context
     * @param attrs attrs
     * @param defStyle defStyle
     */
    public PullToRefreshWebView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
    private WebView webView;
    /**
     *  com.common.widget.pullrefresh.PullToRefreshBase#createRefreshableView(android.content.Context, android.util.AttributeSet)
     */
    @Override
    protected WebView createRefreshableView(Context context, AttributeSet attrs) {
         webView = new WebView(context);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        return webView;
    }
    public void goBack(){
        webView.goBack();
    }
    public void goForward(){
        webView.goForward();
    }
    public void reLoad(){
        webView.reload();
    }
    /**
     *  com.common.widget.pullrefresh.PullToRefreshBase#isReadyForPullDown()
     */
    @Override
    protected boolean isReadyForPullDown() {
        return mRefreshableView.getScrollY() == 0;
    }

    /**
     *  com.common.widget.pullrefresh.PullToRefreshBase#isReadyForPullUp()
     */
    @Override
    protected boolean isReadyForPullUp() {
        double exactContentHeight = Math.floor(mRefreshableView.getContentHeight() * mRefreshableView.getScale());
        return mRefreshableView.getScrollY() >= (exactContentHeight - mRefreshableView.getHeight());
    }

    @Override
    protected LoadingLayout createHeaderLoadingLayout(Context context, AttributeSet attrs) {
        return new RotateLoadingLayout(context);
    }

    public void loadUrl(String url){
        getRefreshableView().loadUrl(url);
    }

    public WebSettings getSettings(){
        return getRefreshableView().getSettings();
    }

    public void setWebViewClient(WebViewClient webViewClient){
        getRefreshableView().setWebViewClient(webViewClient);
    }
    public void setWebChromeClient(WebChromeClient webChromeClient){
        getRefreshableView().setWebChromeClient(webChromeClient);
    }

}
