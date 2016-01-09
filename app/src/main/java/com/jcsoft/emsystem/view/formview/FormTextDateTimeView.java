package com.jcsoft.emsystem.view.formview;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.jcsoft.emsystem.R;
import com.jcsoft.emsystem.utils.CommonUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 日期 时间选择框
 * by dive 2015.10.15
 */
public class FormTextDateTimeView extends RelativeLayout implements View.OnClickListener {

    private static final int DATE_TYPE = 0;
    private static final int TIME_TYPE = DATE_TYPE + 1;
    private static final int DATETIME_TYPE = TIME_TYPE + 1;

    private RelativeLayout mFormContentView;
    private TextView mTagTextView;
    private TextView mHintTextView;
    private TextView mDateTextView;
    private View mDividerView;

    private ImageView mFormArrow;
    /*0为日期 ，1为时间, 2为 日期+时间*/
    private int mDateTimeType = 0;
    private TimePicker mTimePicker;
    private DatePicker mDatePicker;

    private View mTargetView;
    private String mDialogTitle;

    private SimpleDateFormat sdft;
    private String mTime;
    private String mDate;
    private int mYear;
    private int mMonth;
    private int mDay;
    private int mHour;
    private int mMinutes;

    private String custom_column_name;

    private int tagViewColor = R.color.color_gray;

    private boolean mClickable;//是否能被点击
    private String tipsString;

    public FormTextDateTimeView(Context context, int mDateTimeType) {
        super(context);
        this.mDateTimeType = mDateTimeType;
        init(null, 0);
    }

    public FormTextDateTimeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public FormTextDateTimeView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {

        LayoutInflater.from(getContext()).inflate(R.layout.view_form_date_view, this, true);

        mFormContentView = (RelativeLayout) findViewById(R.id.form_content);
        mHintTextView = (TextView) findViewById(R.id.formview_hint);
        mTagTextView = (TextView) findViewById(R.id.formview_tag);
        mDateTextView = (TextView) findViewById(R.id.formview_text);

        mDividerView = findViewById(R.id.formview_baseline);
        mFormArrow = (ImageView) findViewById(R.id.iv_arrow);

        Calendar calendar = Calendar.getInstance();
        Date nowDate = calendar.getTime();
        mYear = calendar.get(Calendar.YEAR);
        mMonth = calendar.get(Calendar.MONTH);
        mDay = calendar.get(Calendar.DAY_OF_MONTH);
        mHour = calendar.get(Calendar.HOUR_OF_DAY);
        mMinutes = calendar.get(Calendar.MINUTE);

        sdft = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        SimpleDateFormat sdft1 = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdft2 = new SimpleDateFormat("HH:mm");
        mDialogTitle = "请选择日期时间";
        mTargetView = getTargetView();

        mDate = sdft1.format(nowDate);
        mTime = sdft2.format(nowDate);

        mClickable = true;

    }

    private View getTargetView() {
        switch (mDateTimeType) {
            case DATE_TYPE:
                mTargetView = LayoutInflater.from(getContext()).inflate(R.layout.view_picker_date, null);
                mDatePicker = (DatePicker) mTargetView.findViewById(R.id.datePicker);
                break;
            case TIME_TYPE:
                mTargetView = LayoutInflater.from(getContext()).inflate(R.layout.view_picker_time, null);
                mTimePicker = (TimePicker) mTargetView.findViewById(R.id.timePicker);
                mTimePicker.setOnTimeChangedListener(timePickerChangeListener);
                mTimePicker.setIs24HourView(true);
                mTimePicker.setCurrentHour(mHour);
                mTimePicker.setCurrentMinute(mMinutes);
                break;
            case DATETIME_TYPE:
                mTargetView = LayoutInflater.from(getContext()).inflate(R.layout.view_picker_date_time, null);
                mDatePicker = (DatePicker) mTargetView.findViewById(R.id.datePicker);
                mTimePicker = (TimePicker) mTargetView.findViewById(R.id.timePicker);
                mTimePicker.setOnTimeChangedListener(timePickerChangeListener);
                mTimePicker.setIs24HourView(true);
                mTimePicker.setCurrentHour(mHour);
                mTimePicker.setCurrentMinute(mMinutes);
                break;
        }
        return mTargetView;
    }

    public void setDateTimeType(int mDateTimeType) {
        this.mDateTimeType = mDateTimeType;
        switch (mDateTimeType) {
            case DATETIME_TYPE:
                sdft = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                break;
            case DATE_TYPE:
                sdft = new SimpleDateFormat("yyyy-MM-dd");
                break;
            case TIME_TYPE:
                sdft = new SimpleDateFormat("HH:mm");
                break;
        }
    }

    private TimePicker.OnTimeChangedListener timePickerChangeListener = new TimePicker.OnTimeChangedListener() {

        @Override
        public void onTimeChanged(TimePicker timePicker, int hourOfDay, int minute) {
            mTime = new StringBuilder().append(pad(hourOfDay)).append(":").append(pad(minute)).toString();
        }
    };

    private DatePicker.OnDateChangedListener datePickerChangeListener = new DatePicker.OnDateChangedListener() {

        @Override
        public void onDateChanged(DatePicker datePicker, int year, int month, int day) {
            mDate = new StringBuilder().append(year).append("-").append(pad(month + 1)).append("-").append(pad(day)).toString();
        }
    };

    private static String pad(int c) {
        if (c >= 10)
            return String.valueOf(c);
        else
            return "0" + String.valueOf(c);
    }

    /**
     * 设置左边tag的值
     * @param tag
     */
    public void setTag(String tag) {
        mTagTextView.setText(tag);
    }

    /**
     * 必输字段的 提示信息
     * @param hintText
     */
    public void setHintText(String hintText) {
        mHintTextView.setVisibility(VISIBLE);
        mHintTextView.setText(hintText);
        mHintTextView.setTextColor(getResources().getColor(R.color.color_gray));
        mDateTextView.setVisibility(GONE);
    }

    /**
     * 是否显示底部的线
     * @param mShowDivider
     */
    public void setShowDriver(boolean mShowDivider) {
        mDividerView.setVisibility(mShowDivider ? View.VISIBLE : View.GONE);
    }

    /**
     * 点击事件的绑定
     * @param isOnclick
     */
    public void setmFormViewOnclick(boolean isOnclick) {
        this.mClickable = isOnclick;
        mFormContentView.setOnClickListener(this);
    }

    /**
     * 是否支持编辑 支持编辑则显示右箭头
     * @param mShowArrow
     */
    public void setmShowArrow(Boolean mShowArrow) {
        mFormArrow.setVisibility(mShowArrow ? View.VISIBLE : View.GONE);
    }

    /**
     * 校验时候的提示信息
     * @param hintText
     */
    public void setErrorText(String hintText) {
        mHintTextView.setVisibility(VISIBLE);
        mHintTextView.setText(hintText);
        mHintTextView.setTextColor(getResources().getColor(R.color.orange_dark));
    }

    /**
     * 设置左边tag的值
     * @param custom_column_name
     */
    public void setCustom_column_name(String custom_column_name) {
        this.custom_column_name = custom_column_name;
    }

    /**
     * 设置右边value的值
     * @param text
     */
    public void setDateText(String text) {

        if (!CommonUtils.strIsEmpty(text)) {
            mHintTextView.setVisibility(GONE);
            mDateTextView.setVisibility(VISIBLE);
            mDateTextView.setText(text);
            mTagTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
            mDateTextView.setTextColor(getResources().getColor(R.color.black));
        } else {
            mDateTextView.setVisibility(GONE);
            mTagTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        }

        Calendar calendar = Calendar.getInstance();
        try {
            if(!CommonUtils.strIsEmpty(text)){
                calendar.setTime(sdft.parse(text));
                mYear = calendar.get(Calendar.YEAR);
                mMonth = calendar.get(Calendar.MONTH);
                mDay = calendar.get(Calendar.DAY_OF_MONTH);
                mHour = calendar.get(Calendar.HOUR_OF_DAY);
                mMinutes = calendar.get(Calendar.MINUTE);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取右边value的值
     */
    public String getDateText() {
        if (!TextUtils.isEmpty(mDateTextView.getText().toString()) && !mDateTextView.getText().toString().equals("请选择")) {
            return mDateTextView.getText().toString();
        } else {
            return "";
        }
    }

    @Override
    public void onClick(View view) {
        try {
            if (mClickable) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle(mDialogTitle);
                if (mTargetView.getParent() == null) {
                    builder.setView(mTargetView);
                } else {
                    ((ViewGroup) mTargetView.getParent()).removeAllViews();
                    mTargetView = getTargetView();
                    builder.setView(mTargetView);
                }
                mDatePicker.init(mYear, mMonth, mDay, datePickerChangeListener);

                builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String dataTimeText = "";
                        switch (mDateTimeType) {
                            case DATETIME_TYPE:
                                dataTimeText = mDate + " " + mTime;
                                break;
                            case DATE_TYPE:
                                dataTimeText = mDate;
                                break;
                            case TIME_TYPE:
                                dataTimeText = mTime;
                                break;
                        }
                        setDateText(dataTimeText);
                        if (dateTimeViewSelect != null) {
                            dateTimeViewSelect.dateTimeViewSelect(custom_column_name, dataTimeText);
                        }
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            } else {
                if(!CommonUtils.strIsEmpty(tipsString)){
                    CommonUtils.showShortText(getContext(), tipsString);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void setTipsString(String tipsString) {
        this.tipsString = tipsString;
    }

    private FormDateTimeViewSelect dateTimeViewSelect;

    public interface FormDateTimeViewSelect {
        void dateTimeViewSelect(String custom_column_name, String formViewTextValue);
    }

    public void dialogViewSelect(FormDateTimeViewSelect dateTimeViewSelect) {
        this.dateTimeViewSelect = dateTimeViewSelect;
    }

    public void setmTagTextViewColor(int viewColor) {
        this.tagViewColor = viewColor;
        mTagTextView.setTextColor(tagViewColor);
    }
}
