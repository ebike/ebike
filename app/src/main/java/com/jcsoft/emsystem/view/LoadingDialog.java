package com.jcsoft.emsystem.view;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

import com.jcsoft.emsystem.R;
import com.jcsoft.emsystem.utils.CommonUtils;

public class LoadingDialog extends RelativeLayout {
    public LoadingDialog(Context context) {
        super(context);
        init();
    }

    public LoadingDialog(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LoadingDialog(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init(){
        LayoutInflater.from(getContext()).inflate(R.layout.view_loading_bar, this, true);
        RelativeLayout mLlview = (RelativeLayout)findViewById(R.id.ll_view);
        mLlview.setBackgroundResource(R.drawable.selector_loading_frame);
        this.setGravity(Gravity.CENTER);
        this.setEnabled(true);
        this.requestFocus();
        this.setBackgroundColor(Color.WHITE);

        hideSoftInput();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return true;
    }

    public void hideSoftInput() {
        CommonUtils.hideSoftInput(getContext(), this);
    }

    public void dismiss(){}

}
