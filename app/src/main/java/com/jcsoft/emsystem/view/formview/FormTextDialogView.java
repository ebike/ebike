package com.jcsoft.emsystem.view.formview;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
 * 自定义view 弹出选择框
 * by dive 2015.10.13
 */
public class FormTextDialogView extends RelativeLayout implements View.OnClickListener {

    private CharSequence[] mTextKeyArray;
    private CharSequence[] mTextValueArray;

    private RelativeLayout mLayoutContext;
    private TextView mTagTextView;
    private TextView mHintTextView;
    private TextView mValueTextView;
    private View mDividerView;
    private ImageView mImageArrow;

    private String custom_column_name;
    private String formValuename;

    private boolean isRequred;
    private String requiredText;

    private int tagViewColor = R.color.color_gray;

    private boolean mClickable;//是否能被点击
    private String tipsString;

    public FormTextDialogView(Context context) {
        super(context);
        init(null, 0);
    }

    public FormTextDialogView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public FormTextDialogView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        LayoutInflater.from(getContext()).inflate(R.layout.view_form_dialog_view, this, true);
        mLayoutContext = (RelativeLayout) findViewById(R.id.relayout_context);
        mTagTextView = (TextView) findViewById(R.id.formview_tag);
        mHintTextView = (TextView) findViewById(R.id.formview_hint);
        mValueTextView = (TextView) findViewById(R.id.formview_text);
        mDividerView = findViewById(R.id.view_baselin);
        mImageArrow = (ImageView) findViewById(R.id.iv_arrow);

    }

    /**
     * 显示底部线
     * @param isShowDivider
     */
    public void setmShowDivider(boolean isShowDivider) {
        mDividerView.setVisibility(isShowDivider ? View.VISIBLE : View.GONE);
    }

    /**
     * 点击事件的绑定
     * @param isOnclick
     */
    public void setmFormViewOnclick(boolean isOnclick) {
        this.mClickable = isOnclick;
        mLayoutContext.setOnClickListener(this);
    }

    /**
     * 是否支持编辑 支持编辑则显示右箭头
     * @param mShowArrow
     */
    public void setmShowArrow(Boolean mShowArrow) {
        mImageArrow.setVisibility(mShowArrow ? View.VISIBLE : View.GONE);
    }

    /**
     * 事件点击的处理
     */
    public void onClick(View view) {
        try {
            if (mClickable && mTextKeyArray != null && mTextKeyArray.length > 0) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("请选择");
                builder.setCancelable(true);
                builder.setItems(mTextKeyArray, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String lable = mTextKeyArray[which].toString();
                        formValuename = mTextValueArray[which].toString();
                        if (!CommonUtils.strIsEmpty(lable) && !lable.equals("请选择")) {
                            setValue(lable);
                            if (dialogViewSelect != null) {
                                dialogViewSelect.dialogViewSelect(custom_column_name, getValue(), getText());
                            }
                        } else {
                            setValue("");
                        }

                    }
                });
                builder.show();
            } else {
                if(!CommonUtils.strIsEmpty(tipsString)){
                    CommonUtils.showShortText(getContext(), tipsString);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置左边tag的值
     * @param mTagString
     */
    public void setTagTextView(String mTagString) {
        if (!TextUtils.isEmpty(mTagString)) {
            mTagTextView.setText(mTagString);
        }
    }

    /**
     * 设置左边tag的值
     * @param mTextKeyArray
     */
    public void setmTextkeyArray(CharSequence[] mTextKeyArray) {
        this.mTextKeyArray = mTextKeyArray;
    }

    /**
     * 设置右边value的值
     * @param mTextValueArray
     */
    public void setmTextValueArray(CharSequence[] mTextValueArray) {
        this.mTextValueArray = mTextValueArray;
    }

    /**
     * 设置右边默认的提示信息
     * @param hintText
     */
    public void setHintText(String hintText) {
        if (!CommonUtils.strIsEmpty(hintText)) {
            isRequred = true;
            this.requiredText = hintText;
            mHintTextView.setVisibility(VISIBLE);
            mHintTextView.setText(hintText);
            mHintTextView.setTextColor(getResources().getColor(R.color.color_gray));
        } else {
            isRequred = false;
        }
    }

    /**
     * 校验失败的提示信息
     * @param hintText
     */
    public void setErrorText(String hintText) {
        mHintTextView.setVisibility(VISIBLE);
        mHintTextView.setText(hintText);
        mHintTextView.setTextColor(getResources().getColor(R.color.orange_dark));
    }

    public void setKey(String value) {//显示的值
        this.formValuename = value;
    }

    /**
     * 设置右边value的值
     * @param value
     */
    public void setValue(String value) {//上传的值

        if (!CommonUtils.strIsEmpty(value) && !value.equals("请选择")) {
            mValueTextView.setVisibility(VISIBLE);
            mValueTextView.setText(value);
            mHintTextView.setVisibility(GONE);
            mTagTextView.setTextColor(getResources().getColor(tagViewColor));
            mTagTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
            mValueTextView.setTextColor(getResources().getColor(R.color.black));
        } else {
            mValueTextView.setVisibility(GONE);
            if (isRequred) {
                mHintTextView.setVisibility(VISIBLE);
                mHintTextView.setText(requiredText);
            }
            mTagTextView.setTextColor(getResources().getColor(tagViewColor));
            mTagTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        }
    }

    public String getValue() {
        return formValuename;
    }

    public String getText() {//获取当前选择的显示字段
        return mValueTextView.getText().toString();
    }

    public void setCustom_column_name(String custom_column_name) {
        this.custom_column_name = custom_column_name;
    }

    /**
     * 设置tag的颜色
     * @param viewColor
     */
    public void setmTagTextViewColor(int viewColor) {
        this.tagViewColor = viewColor;
        mTagTextView.setTextColor(viewColor);
    }

    public void setTipsString(String tipsString) {
        this.tipsString = tipsString;
    }

    private FormTextDialogViewSelect dialogViewSelect;

    public interface FormTextDialogViewSelect {
        void dialogViewSelect(String custom_column_name, String formViewTagValue, String formViewTextValue);

    }

    public void dialogViewSelect(FormTextDialogViewSelect dialogViewSelect) {
        this.dialogViewSelect = dialogViewSelect;
    }

}
