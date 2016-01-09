package com.jcsoft.emsystem.view.formview;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jcsoft.emsystem.R;

/**
 * 查看名片信息的view
 */
public class FormTagView extends RelativeLayout implements View.OnClickListener {

    private RelativeLayout mFormContentView;
    private TextView mTagTextView;
    private TextView mValueTextView;
    private Bundle mIntentValue = new Bundle();
    private String mIntentString;
    private String mTagTextString;
    private String mValueTextString;
    private boolean isOnClick;

    public FormTagView(Context context) {
        super(context);
        init(null, 0);
    }

    public FormTagView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public FormTagView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {

        LayoutInflater.from(getContext()).inflate(R.layout.view_form_tag_view, this, true);
        mFormContentView = (RelativeLayout) findViewById(R.id.form_contact_card);
        mFormContentView.setOnClickListener(this);
        mTagTextView = (TextView) findViewById(R.id.form_tag);
        mValueTextView = (TextView) findViewById(R.id.form_value);
    }

    public void setTagTextView(String text) {//设置左边的值
        if (!TextUtils.isEmpty(text)) {
            mTagTextView.setText(text);
        }
    }

    public void setmValueTextView(String mValueText) {
        this.mValueTextString = mValueText;
        mValueTextView.setText(mValueTextString);
    }

    @Override
    public void onClick(View view) {
        if (isOnClick) {
            Intent intent = new Intent();
            intent.setClassName(getContext().getPackageName(), mIntentString);
            intent.putExtras(mIntentValue);
            getContext().startActivity(intent);
        }
    }

    public void setmIntentValue(Bundle mIntentValue) {
        this.mIntentValue = mIntentValue;
    }

    public void setmIntentString(String mIntentString) {
        this.mIntentString = mIntentString;
    }

    public void setOnClick(boolean isOnClick) {
        this.isOnClick = isOnClick;
    }

}
