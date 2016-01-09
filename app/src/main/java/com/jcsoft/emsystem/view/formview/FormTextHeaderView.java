package com.jcsoft.emsystem.view.formview;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jcsoft.emsystem.R;

import java.util.logging.Logger;

/**
 * 区域块的头部
 */
public class FormTextHeaderView extends RelativeLayout implements View.OnClickListener {

    private TextView mTextTextView;
    private int showDriver = 0;//0 只显示上边的线  1只显示下边的线 2上下线都显示
    private String mTagString;

    public FormTextHeaderView(Context context) {
        super(context);
        init(null, 0);
    }

    public FormTextHeaderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public FormTextHeaderView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {

        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.FormView, defStyle, 0);

        showDriver = a.getIndex(R.styleable.FormView_form_header_type);
        mTagString = a.getString(R.styleable.FormView_form_tag);
        a.recycle();

        LayoutInflater.from(getContext()).inflate(R.layout.form_view_area, this, true);
        mTextTextView = (TextView) findViewById(R.id.formview_area);
        mTextTextView.setText(mTagString == null ? "" : mTagString);
        mTextTextView.setTextColor(getResources().getColor(R.color.dc_title_text));

        mTextTextView.setOnClickListener(this);

        setViewDriverLine(showDriver);
    }

    public void setTagTextView(String text) {//设置左边的值
        if (!TextUtils.isEmpty(text)) {
            mTextTextView.setText(text);
        }
    }


    public void onClick(View view) {
    }

    public void setViewDriverLine(int lineIndex) {
        this.showDriver = lineIndex;
        switch (lineIndex) {
            case 0:
                findViewById(R.id.view_top_line).setVisibility(VISIBLE);
                findViewById(R.id.view_bottom_line).setVisibility(GONE);
                break;
            case 1:
                findViewById(R.id.view_top_line).setVisibility(GONE);
                findViewById(R.id.view_bottom_line).setVisibility(VISIBLE);
                break;
            case 2:
            case 3:
                findViewById(R.id.view_top_line).setVisibility(VISIBLE);
                findViewById(R.id.view_bottom_line).setVisibility(VISIBLE);
                break;
        }
    }

}
