package com.jcsoft.emsystem.view.formview;

import android.content.Context;
import android.text.ClipboardManager;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jcsoft.emsystem.R;
import com.jcsoft.emsystem.utils.CommonUtils;

/**
 * 自定义view 纯文本展示 上下结构  数据上报的详情界面用到
 * by dive 2015.10.27
 */
public class FormAloneTextView extends RelativeLayout implements View.OnLongClickListener{
    private LinearLayout mFormContentView;
    private TextView mTagTextView;
    private TextView mTextTextView;
    private View mDividerView;
    private ImageView mImageView;

    /****获取当前的显示文字和对应的值*****/
    private String textValue;

    private boolean isFormSelect;

    private LinearLayout mFormContentNewView;
    private TextView mTagTextNewView;
    private TextView mSubTagTextView;
    private TextView mTextNewView;

    private boolean isNewView;//是否是新的布局样式

    public FormAloneTextView(Context context) {
        super(context);
        init(null, 0);
    }

    public FormAloneTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public FormAloneTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {

        LayoutInflater.from(getContext()).inflate(R.layout.view_form_text_view, this, true);
        mFormContentView = (LinearLayout) findViewById(R.id.form_content);
        mTagTextView = (TextView) findViewById(R.id.form_tag);
        mTextTextView = (TextView) findViewById(R.id.form_text);
        mImageView = (ImageView) findViewById(R.id.form_image_view);
        mImageView.setVisibility(GONE);
        mDividerView = findViewById(R.id.form_divider);

        mTagTextView.setTextColor(getResources().getColor(R.color.dc_title_text));
        mTextTextView.setTextColor(getResources().getColor(R.color.dc_context_text));

        mFormContentNewView = (LinearLayout) findViewById(R.id.form_content_new);
        mTagTextNewView = (TextView) findViewById(R.id.form_tag_new);
        mSubTagTextView = (TextView) findViewById(R.id.form_sub_tag);
        mTextNewView = (TextView) findViewById(R.id.form_text_new);

        mTagTextNewView.setTextColor(getResources().getColor(R.color.dc_title_text));

        showView();

    }

    public boolean isFormSelect() {//设置是否是选择项
        return isFormSelect;
    }

    public void setFormSelect(boolean isFormSelect) {
        this.isFormSelect = isFormSelect;
    }

    public void setTagTextView(String text) {//设置左边的值
        if (!TextUtils.isEmpty(text)) {
            mTagTextView.setText(text);
            mTagTextNewView.setText(text);
        }
    }

    public void setSubTagTextView(CharSequence text) {//设置副标题
        if (!TextUtils.isEmpty(text)) {
            mSubTagTextView.setText(text);
        }
    }

    public void setTextView(String text) {//设置右边的值
        if (!TextUtils.isEmpty(text)) {
            mTextTextView.setVisibility(View.VISIBLE);
            mTextNewView.setVisibility(View.VISIBLE);
            mTextTextView.setText(text);
            mTextNewView.setText(text);
        } else {
            mTextTextView.setVisibility(View.GONE);
            mTextNewView.setVisibility(View.GONE);
        }
    }

    public void setmShowDivider(Boolean mShowDivider) {//设置是否显示先下边的线
        mDividerView.setVisibility(mShowDivider ? View.VISIBLE : View.GONE);
    }

    public void setmShowArrow(Boolean mShowArrow) {//设置是否显示先下边的线
        mImageView.setVisibility(mShowArrow ? View.VISIBLE : View.GONE);
    }

    public void setTextViewColor(int colorIndex){//设置右边字体颜色
        if(colorIndex != 0){
            mTextTextView.setTextColor(getResources().getColor(colorIndex));
            mTextNewView.setTextColor(getResources().getColor(colorIndex));
        }
    }

    public void setTextiewValue(String textValue) {//设置右边对应的上送值
        this.textValue = textValue;
    }

    public String getTextViewValue() {//获取右边对应值
        if(isFormSelect){
            return textValue;
        } else {
            return mTextTextView.getText().toString();
        }
    }

    public void setNewView(boolean isNewView) {
        this.isNewView = isNewView;
        showView();
    }

    private void showView() {
        if (isNewView) {
            mFormContentNewView.setVisibility(View.VISIBLE);
            mFormContentView.setVisibility(View.GONE);
            setmShowDivider(false);
        } else {
            mFormContentNewView.setVisibility(View.GONE);
            mFormContentView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public boolean onLongClick(View view) {
        String text="";
        if (mTextTextView != null) {
            text = mTextTextView.getText().toString().trim();
        }
        if (TextUtils.isEmpty(text)) {
            return false;
        }
        ClipboardManager cmb = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
        cmb.setText(text);
        CommonUtils.showShortText(getContext(), "复制成功");
        return false;
    }

}
