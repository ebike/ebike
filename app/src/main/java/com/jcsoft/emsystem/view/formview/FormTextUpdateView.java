package com.jcsoft.emsystem.view.formview;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jcsoft.emsystem.R;
import com.jcsoft.emsystem.utils.CommonUtils;

import java.util.logging.Logger;

/***
 * 修改自定义字段 跳转单独修改界面的view
 */
public class FormTextUpdateView extends RelativeLayout implements View.OnClickListener {

    private RelativeLayout mFormContentView;
    private TextView mTagTextView;
    private TextView mTextTextView;
    private View mDividerView;

    private ImageView mImageView;

    private boolean mHasData;
    private String mIntentString;
    private boolean mClickable;

    private String tipsString;

    private Bundle mIntentValue = new Bundle();

    public FormTextUpdateView(Context context) {
        super(context);
        init(null, 0);
    }

    public FormTextUpdateView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public FormTextUpdateView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {

        LayoutInflater.from(getContext()).inflate(R.layout.view_form_update, this, true);
        mFormContentView = (RelativeLayout) findViewById(R.id.relayout_context);
        mTagTextView = (TextView) findViewById(R.id.formview_tag);
        mTextTextView = (TextView) findViewById(R.id.formview_text);
        mTextTextView.setVisibility(GONE);
        mImageView = (ImageView) findViewById(R.id.iv_arrow);
        mDividerView = findViewById(R.id.view_baselin);
        mTagTextView.setTextSize(16);
        mTextTextView.setTextSize(14);
    }

    /**
     * 点击事件的处理  mClickable 支持编辑 有编辑权限
     *
     * @param view
     */
    public void onClick(View view) {
        try {
            if (mClickable) {
                Intent intent = new Intent();
                if (CommonUtils.strIsEmpty(mIntentString)) {
                    if (!CommonUtils.strIsEmpty(tipsString)) {
                        CommonUtils.showShortText(getContext(), tipsString);
                        return;
                    }
                }
                intent.setClassName(getContext().getPackageName(), mIntentString);
                if (mHasData) {
                    intent.putExtras(mIntentValue);
                }
                getContext().startActivity(intent);
            } else {
                if (!CommonUtils.strIsEmpty(tipsString)) {
                    CommonUtils.showShortText(getContext(), tipsString);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置左边tag的值
     *
     * @param text
     */
    public void setTagTextView(String text) {
        if (!TextUtils.isEmpty(text)) {
            mTagTextView.setText(text);
        }
    }

    /**
     * 设置右边value的值
     *
     * @param text
     */
    public void setTextView(String text) {
        if (!TextUtils.isEmpty(text)) {
            mTextTextView.setVisibility(VISIBLE);
            mTextTextView.setText(text);
            mTagTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        } else {
            mTextTextView.setVisibility(GONE);
            mTagTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        }
    }

    public String getTextViewValue() {//获取右边对应值
        return mTextTextView.getText().toString();
    }

    /**
     * 传递数据的bundle
     *
     * @param mIntentValue
     */
    public void setmIntentValue(Bundle mIntentValue) {
        this.mIntentValue = mIntentValue;
    }

    /**
     * 是否支持点击事件
     *
     * @param mClickable
     */
    public void setmClickable(boolean mClickable) {
        this.mClickable = mClickable;
        mFormContentView.setOnClickListener(this);
    }

    /**
     * 跳转的activity
     *
     * @param mIntentString
     */
    public void setmIntentString(String mIntentString) {
        this.mIntentString = mIntentString;
    }

    /**
     * 是否传递数据
     *
     * @param mHasData
     */
    public void setmHasData(boolean mHasData) {
        this.mHasData = mHasData;
    }

    /**
     * 是否显示底部的线
     *
     * @param mShowDivider
     */
    public void setmShowDivider(Boolean mShowDivider) {
        mDividerView.setVisibility(mShowDivider ? View.VISIBLE : View.GONE);
    }

    /**
     * 是否支持编辑 支持编辑则显示右箭头
     *
     * @param mShowArrow
     */
    public void setmShowArrow(Boolean mShowArrow) {
        mImageView.setVisibility(mShowArrow ? View.VISIBLE : View.GONE);
    }

    /**
     * 提示信息
     *
     * @param tipsString
     */
    public void setTipsString(String tipsString) {
        this.tipsString = tipsString;
    }
}
