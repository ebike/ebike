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

/**
 * 客户列表选择
 */
public class FormIntentSelectView extends RelativeLayout implements View.OnClickListener {

    private String mIntentString;

    private boolean mHasData;//是否传值过去
    private String mValue;

    private boolean mClickable;//是否能被点击
    private Bundle mIntentValue = new Bundle();

    private View mDividerView;
    private ImageView mArrowView;

    private RelativeLayout mFormContentNewView;
    private TextView mTagTextNewView;
    private TextView mErrorTextNewView;
    private TextView mTextNewView;

    private int tagViewColor = R.color.color_gray;

    public FormIntentSelectView(Context context) {
        super(context);
        init(null, 0);
    }

    public FormIntentSelectView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public FormIntentSelectView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {

        LayoutInflater.from(getContext()).inflate(R.layout.view_form_intent_view, this, true);
        mDividerView = findViewById(R.id.form_divider);
        mArrowView = (ImageView) findViewById(R.id.iv_arrow);

        mFormContentNewView = (RelativeLayout) findViewById(R.id.relayout_context);
        mFormContentNewView.setOnClickListener(this);
        mTagTextNewView = (TextView) findViewById(R.id.form_tag_new);
        mErrorTextNewView = (TextView) findViewById(R.id.formview_intent_hint);
        mTextNewView = (TextView) findViewById(R.id.form_text_new);
        mTextNewView.setVisibility(GONE);
        mTagTextNewView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
    }

    /**
     * 右边显示的文字
     *
     * @param text
     */
    public void setText(String text) {
        if (!TextUtils.isEmpty(text)) {
            mTextNewView.setVisibility(VISIBLE);
            mTagTextNewView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
            mTextNewView.setText(text);
            mErrorTextNewView.setVisibility(GONE);
        } else {
            mTextNewView.setVisibility(GONE);
            mTagTextNewView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        }
    }

    public String getText() {
        if (!TextUtils.isEmpty(mTextNewView.getText().toString().trim())) {
            return mTextNewView.getText().toString().trim();
        }
        return null;
    }

    /**
     * 右边的箭头是否显示
     *
     * @param mShowArrow
     */
    public void setmShowArrow(Boolean mShowArrow) {
        mArrowView.setVisibility(mShowArrow ? View.VISIBLE : View.GONE);
    }

    /**
     * tag的显示文字颜色
     *
     * @param viewColor
     */
    public void setmTagTextViewColor(int viewColor) {
        this.tagViewColor = viewColor;
        mTagTextNewView.setTextColor(tagViewColor);
    }

    /**
     * tag的显示文字
     *
     * @param mTagString
     */
    public void setmTagTextView(String mTagString) {
        if (mTagString != null) {
            mTagTextNewView.setText(mTagString);
        }
    }

    /**
     * 默认的显示文字
     *
     * @param mHintString
     */
    public void setHintTag(String mHintString) {
        if (!TextUtils.isEmpty(mHintString)) {
            mTextNewView.setHint(mHintString);
        }
    }

    /**
     * 底部线是否显示
     *
     * @param mShowDivider
     */
    public void setmShowDivider(Boolean mShowDivider) {
        mDividerView.setVisibility(mShowDivider ? View.VISIBLE : View.GONE);
    }

    /**
     * 默认的提示信息
     *
     * @param mHintString
     */
    public void setHintText(String mHintString) {
        mErrorTextNewView.setVisibility(VISIBLE);
        mErrorTextNewView.setText(mHintString);
        mErrorTextNewView.setTextColor(getResources().getColor(R.color.color_gray));
    }

    /**
     * 提交时候校验的提示信息
     *
     * @param hintText
     */
    public void setErrorText(String hintText) {
        mErrorTextNewView.setVisibility(VISIBLE);
        mErrorTextNewView.setText(hintText);
        mErrorTextNewView.setTextColor(getResources().getColor(R.color.orange_dark));
    }

    public void setTextColor(int color) {
        mTextNewView.setTextColor(color);
    }

    public String getValue() {
        return mValue;
    }

    public void setValue(String mValue) {
        this.mValue = mValue;
    }

    public void setIntentData(Bundle bundle) {
        this.mIntentValue = bundle;
    }

    public void setIntentAction(String action) {
        this.mIntentString = action;
    }

    public void setHasData(boolean hasData) {
        mHasData = hasData;
    }

    public void setClickable(boolean clickable) {
        this.mClickable = clickable;
    }

    @Override
    public void onClick(View view) {
        try {
            if (mClickable) {
                if (TextUtils.isEmpty(mIntentString)) {
                    return;
                }
                Intent intent = new Intent();
                intent.setClassName(getContext().getPackageName(), mIntentString);
                if (mHasData) {
                    intent.putExtras(mIntentValue);
                }
                getContext().startActivity(intent);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
