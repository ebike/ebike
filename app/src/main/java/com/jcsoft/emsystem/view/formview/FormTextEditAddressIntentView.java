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

/**
 * 地址选择器 自定义view
 * by dive 2015.09.29
 */
public class FormTextEditAddressIntentView extends RelativeLayout implements View.OnClickListener {

    private String mIntentString;
    private String mIntentTagString;
    private String mIntentHintString;
    private String mIntentTextString;

    private boolean mHasData;

    private boolean mClickable;//是否能被点击
    private Bundle mIntentValue = new Bundle();

    private View mDividerView;
    private ImageView mArrowView;
    private RelativeLayout mFormContentNewView;
    private TextView mTagTextNewView;
    private TextView mTextNewView;
    private TextView mHintTextNewView;

    private int tagViewColor = R.color.color_gray;
    private String tipsString;

    public FormTextEditAddressIntentView(Context context) {
        super(context);
        init(null, 0);
    }

    public FormTextEditAddressIntentView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public FormTextEditAddressIntentView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {

        LayoutInflater.from(getContext()).inflate(R.layout.view_form_intentselect_view, this, true);
        mFormContentNewView = (RelativeLayout) findViewById(R.id.relayout_context);

        mArrowView = (ImageView)findViewById(R.id.iv_arrow);
        mHintTextNewView = (TextView) findViewById(R.id.formview_intent_hint);

        mTagTextNewView = (TextView) findViewById(R.id.formview_intent_tag);
        mTextNewView = (TextView) findViewById(R.id.formview_intent_text);
        mDividerView = findViewById(R.id.view_intent_line);
    }

    /**
     * 设置左边tag的值
     * @param tag
     */
    public void setTag(String tag) {
        this.mIntentTagString = tag;
        mTagTextNewView.setText(mIntentTagString);
        mTextNewView.setVisibility(GONE);
    }

    /**
     * 设置填字段的默认信息
     * @param hintText
     */
    public void setHintText(String hintText) {
        this.mIntentHintString = hintText;
        mHintTextNewView.setVisibility(VISIBLE);
        mHintTextNewView.setText(mIntentHintString);
        mHintTextNewView.setTextColor(getResources().getColor(R.color.color_gray));
    }

    /**
     * 校验错误的提示信息
     * @param hintText
     */
    public void setErrorText(String hintText) {
        mHintTextNewView.setVisibility(VISIBLE);
        mHintTextNewView.setText(hintText);
        mHintTextNewView.setTextColor(getResources().getColor(R.color.orange_dark));
    }

    /**
     * 设置右边的value值
     * @param addressText
     */
    public void setText(String addressText) {
        this.mIntentTextString = addressText;
        if(!CommonUtils.strIsEmpty(mIntentTextString)){
            mTextNewView.setVisibility(VISIBLE);
            mTextNewView.setText(addressText);
            mHintTextNewView.setVisibility(GONE);
            mTagTextNewView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
            mTagTextNewView.setTextColor(getResources().getColor(tagViewColor));
        } else {
            mTagTextNewView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
            mTextNewView.setVisibility(GONE);
            mHintTextNewView.setVisibility(VISIBLE);
        }
    }

    /**
     * 获取右边的value值
     */
    public String getText() {
        if (!TextUtils.isEmpty(mTextNewView.getText().toString().trim())) {
            return mTextNewView.getText().toString().trim();
        }
        return null;
    }

    /**
     * 设置传递是数据的bundle
     * @param bundle
     */
    public void setIntentData(Bundle bundle){
        this.mIntentValue = bundle;
    }

    /**
     * 设置跳转的activity
     * @param action
     */
    public void setIntentAction(String action){
        this.mIntentString = action;
    }

    /**
     * 设置跳转时候是否携带数据
     * @param hasData
     */
    public void setHasData(boolean hasData){
        mHasData =  hasData;
    }

    /**
     * 点击事件的绑定
     * @param clickable
     */
    public void setClickable(boolean clickable){
        this.mClickable = clickable;
        mFormContentNewView.setOnClickListener(this);
    }

    /**
     * 是否支持编辑 支持编辑则显示右箭头
     * @param mShowArrow
     */
    public void setmShowArrow(Boolean mShowArrow) {
        mArrowView.setVisibility(mShowArrow ? View.VISIBLE : View.GONE);
    }

    /**
     * 是否显示底部线
     * @param mShowDivider
     */
    public void setmShowDivider(Boolean mShowDivider) {
        mDividerView.setVisibility(mShowDivider ? View.VISIBLE : View.GONE);
    }

    /**
     * 设置左边tag的值颜色
     * @param viewColor
     */
    public void setmTagTextViewColor(int viewColor) {
        this.tagViewColor = viewColor;
        mTagTextNewView.setTextColor(tagViewColor);
    }

    @Override
    public void onClick(View view) {
        if(mClickable){
            if(TextUtils.isEmpty(mIntentString)){
                return;
            }

            Intent intent = new Intent();
            intent.setClassName(getContext().getPackageName(), mIntentString);
            if(mHasData){
                intent.putExtras(mIntentValue);
            }
            getContext().startActivity(intent);
        } else {
            if(!CommonUtils.strIsEmpty(tipsString)){
                CommonUtils.showShortText(getContext(), tipsString);
            }
        }
    }


    public void setTipsString(String tipsString) {
        this.tipsString = tipsString;
    }
}
