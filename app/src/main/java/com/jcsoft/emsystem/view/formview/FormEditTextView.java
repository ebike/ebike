package com.jcsoft.emsystem.view.formview;

import android.content.Context;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.text.method.DigitsKeyListener;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jcsoft.emsystem.R;
import com.jcsoft.emsystem.utils.CommonUtils;

/**
 * 新增自定义字段view
 */
public class FormEditTextView extends RelativeLayout {

    private TextView mTagTextView;
    private TextView mErrorTextView;
    private EditText mEditTextView;
    private View mDividerView;

    private String mTagText;
    private boolean isRequired;

    public FormEditTextView(Context context) {
        super(context);
        init(null, 0);
    }

    public FormEditTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public FormEditTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {

        View root = LayoutInflater.from(getContext()).inflate(R.layout.view_form_edit_text, this, true);
        mTagTextView = (TextView) findViewById(R.id.form_tag);
        mErrorTextView = (TextView) findViewById(R.id.form_hint);
        mErrorTextView.setVisibility(GONE);
        mEditTextView = (EditText) findViewById(R.id.form_edit);
        mDividerView = findViewById(R.id.form_divider);

        mEditTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0) {
                    mTagTextView.setVisibility(VISIBLE);
                    mTagTextView.setText(mTagText);
                    mErrorTextView.setVisibility(GONE);
                    mEditTextView.setTextColor(getResources().getColor(R.color.black));
                } else {
                    if (isRequired) {
                        mErrorTextView.setVisibility(VISIBLE);
                        mErrorTextView.setText("必填");
                        mErrorTextView.setTextColor(getResources().getColor(R.color.color_gray));
                    }
                    mTagTextView.setVisibility(GONE);
                    mEditTextView.setHint(mTagText);
                    mEditTextView.setTextColor(getResources().getColor(R.color.orange_dark));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        root.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                mEditTextView.requestFocus();
            }
        });
    }

    public void setTagTextView(String tagText) {//设置左边的值
        this.mTagText = tagText;
        mEditTextView.setHint(tagText);
        mEditTextView.setTextColor(getResources().getColor(R.color.color_gray));
    }

    public void setmTextValueView(String value) {
        mEditTextView.setText(value);
        mEditTextView.setSelection(mEditTextView.getText().length());
    }

    public void setmEditTextInput(int inputType) {
        mEditTextView.setInputType(inputType);
    }

    public void setmShowDivider(Boolean mShowDivider) {//设置是否显示先下边的线
        mDividerView.setVisibility(mShowDivider ? View.VISIBLE : View.GONE);
    }

    public void setmHintString(String mHintString) {//设置输入的默认提示语
        if (!CommonUtils.strIsEmpty(mHintString)) {
            mErrorTextView.setVisibility(VISIBLE);
            mErrorTextView.setTextColor(getResources().getColor(R.color.color_gray));
            mErrorTextView.setText(mHintString);
        }
    }

    public String getEditTextValue() {
        return mEditTextView.getText().toString();
    }

    public void setViewSingle(boolean isSingle) {
        mEditTextView.setSingleLine(true);
    }

    public void setmEditTextViewType(boolean isNumber, int length) {
        if (isNumber) {
            mEditTextView.setKeyListener(new DigitsKeyListener(false, true));
            mEditTextView.setFilters(new InputFilter[]{new InputFilter.LengthFilter(length)});
        }
    }

    public void setmTextViewError(String viewErrorText) {
        if (!CommonUtils.strIsEmpty(viewErrorText)) {
            mErrorTextView.setVisibility(VISIBLE);
            mErrorTextView.setTextColor(getResources().getColor(R.color.orange_dark));
            mErrorTextView.setText(viewErrorText);
        } else {
            mErrorTextView.setVisibility(GONE);
        }
    }

    public String getText() {
        return mEditTextView.getText().toString();
    }

    public void setText(String str) {
        mEditTextView.setText(str);
        mEditTextView.setSelection(str.length());
    }

    public void setIsRequired(boolean isRequired) {
        this.isRequired = isRequired;
    }
}
