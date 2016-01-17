package com.jcsoft.emsystem.view.pullrefresh;


import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jcsoft.emsystem.R;

import java.util.logging.Logger;


public class EmptyViewForList extends RelativeLayout implements View.OnClickListener{
    public EmptyViewForList(Context context) {
        this(context, null);
    }

    public EmptyViewForList(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public EmptyViewForList(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs, defStyle);
    }

    private ImageView mIv_icon;
    private int mIv_iconId;

    private TextView mTv_desc;

    private Button mBtn_action;
    private String mBtn_textStr;

    private String mAction;

    private boolean mHasData;

    private Bundle mBundle;

    private void init(Context context, AttributeSet attrs, int defStyle){
        // Load attributes
        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.EmptyView, defStyle, 0);

        int resourceId = a.getResourceId(R.styleable.EmptyView_empty_iv_src,R.mipmap.ic_launcher);
        String tv_text = a.getString(R.styleable.EmptyView_empty_tv_text);

        int tv_text_color = a.getColor(R.styleable.EmptyView_empty_tv_text_color,0);

        float tv_text_size = a.getDimensionPixelSize(R.styleable.EmptyView_empty_tv_text_size,24);

        String btn_text = a.getString(R.styleable.EmptyView_empty_btn_text);

        int btn_visibility = a.getInt(R.styleable.EmptyView_empty_btn_visibility,2);


        int btn_text_color= a.getColor(R.styleable.EmptyView_empty_btn_text_color,0);

        mAction = a.getString(R.styleable.EmptyView_empty_btn_action);

        mHasData = a.getBoolean(R.styleable.EmptyView_empty_has_data,false);

        int btn_background = a.getResourceId(R.styleable.EmptyView_empty_btn_background,0);

        a.recycle();

        LayoutInflater.from(getContext()).inflate(R.layout.view_empty_for_list, this, true);
        mIv_icon = (ImageView) findViewById(R.id.iv_icon);
        mTv_desc = (TextView) findViewById(R.id.tv_desc);
        mBtn_action = (Button) findViewById(R.id.btn_action);
        if(resourceId > 0){
            mIv_icon.setImageResource(resourceId);
        }

        if(!TextUtils.isEmpty(tv_text)){
            mTv_desc.setText(tv_text);
        }

        if(tv_text_color > 0){
            mTv_desc.setTextColor(tv_text_color);
        }

        mTv_desc.setTextSize(tv_text_size);

        if(!TextUtils.isEmpty(btn_text)){
            mBtn_action.setText(btn_text);
        }

        switch (btn_visibility){
            case 0:
                mBtn_action.setVisibility(View.VISIBLE);
                break;
            case 1:
                mBtn_action.setVisibility(View.INVISIBLE);
                break;
            case 2:
                mBtn_action.setVisibility(View.GONE);
                break;
        }

        if(btn_background > 0){
            mBtn_action.setBackgroundResource(btn_background);
        }

        if(btn_text_color > 0){
            mBtn_action.setTextColor(btn_text_color);
        }

        mBtn_action.setOnClickListener(this);
    }

    public void setButtonVisibility(int visible){
        mBtn_action.setVisibility(visible);
    }

    public void setTextDesc(int desc){
        mTv_desc.setText(desc);
    }
    public void setTextDesc(String desc){
        mTv_desc.setText(desc);
    }

    public void setTextSize(float size){
        mTv_desc.setTextSize(size);
    }
    public void setHasData(boolean hasData){
        this.mHasData = hasData;
    }

    public void setAction(String action){
        this.mAction = action;
    }

    public void setBundleData(Bundle bundle){
        this.mBundle = bundle;
    }

    public void setmBtn_textStr(String mBtn_textStr) {
        if(!TextUtils.isEmpty(mBtn_textStr)){
            mBtn_action.setText(mBtn_textStr);
        }
    }

    public void setmIv_iconId(int mIv_iconId) {
        if(mIv_iconId > 0){
            mIv_icon.setImageResource(mIv_iconId);
        }
    }

    public void setmIv_iconVisibility(int visibility) {
        mIv_icon.setVisibility(visibility);
    }

    public void setmBtn_actionVisibility(boolean isShow) {
        if(isShow){
            mBtn_action.setVisibility(VISIBLE);
        } else {
            mBtn_action.setVisibility(GONE);
        }
    }

    public void setmIv_iconVisibility(boolean visibility) {
        if(visibility){
            mIv_icon.setVisibility(VISIBLE);
        } else {
            mIv_icon.setVisibility(GONE);
        }
    }

    public void setmAction(String mAction) {
        this.mAction = mAction;
    }

    @Override
    public void onClick(View view) {
        if (TextUtils.isEmpty(mAction)) {
            return;
        }
        Intent intent = new Intent();
        intent.setClassName(getContext().getPackageName(), mAction);
        if (mHasData) {
            intent.putExtras(mBundle);
        }
        getContext().startActivity(intent);
    }
}
