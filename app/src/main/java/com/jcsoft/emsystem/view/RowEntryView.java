package com.jcsoft.emsystem.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jcsoft.emsystem.R;
import com.jcsoft.emsystem.utils.CommonUtils;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

/**
 * 横向的条目组件
 * 包含：图标、标题、右箭头
 * Created by jimmy on 16/1/18.
 */
public class RowEntryView extends RelativeLayout {
    private View rootView;
    @ViewInject(R.id.iv_icon)
    ImageView iconImageView;
    @ViewInject(R.id.tv_title)
    TextView titleTextView;
    @ViewInject(R.id.iv_arrow)
    ImageView arrowImageView;
    //显示左侧图片
    private boolean iconVisible;
    //左侧图片
    private int icon;
    //图片右边文字
    private String text;
    //含右侧箭头
    private boolean hasRightArrow;
    //可点击
    private boolean canClick;
    //点击事件回调接口
    private OnClickCallback onClickCallback;

    public interface OnClickCallback {
        void onClick(View view);
    }

    public RowEntryView(Context context) {
        this(context, null);
    }

    public RowEntryView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RowEntryView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        //获取字段信息
        TypedArray typedArray = getContext().obtainStyledAttributes(
                attrs, R.styleable.RowEntryView, defStyleAttr, 0);
        iconVisible = typedArray.getBoolean(R.styleable.RowEntryView_row_entry_icon_visible, true);
        icon = typedArray.getResourceId(R.styleable.RowEntryView_row_entry_icon, 0);
        text = typedArray.getString(R.styleable.RowEntryView_row_entry_text);
        hasRightArrow = typedArray.getBoolean(R.styleable.RowEntryView_row_entry_has_right_arrow, false);
        canClick = typedArray.getBoolean(R.styleable.RowEntryView_row_entry_can_click, false);
        typedArray.recycle();

        //获取布局文件
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        rootView = inflater.inflate(R.layout.view_row_entry, this, true);
        x.view().inject(this, rootView);
        //初始化组件
        //左侧icon是否显示
        if (iconVisible) {
            iconImageView.setVisibility(View.VISIBLE);
            iconImageView.setImageResource(icon);
        } else {
            iconImageView.setVisibility(View.GONE);
        }
        //标题显示
        if (!CommonUtils.strIsEmpty(text)) {
            titleTextView.setText(text);
        }
        //右侧箭头显示
        if (hasRightArrow) {
            arrowImageView.setVisibility(View.VISIBLE);
        } else {
            arrowImageView.setVisibility(View.GONE);
        }
        //是否可点击
        if (canClick) {
            rootView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onClickCallback != null) {
                        onClickCallback.onClick(view);
                    }
                }
            });
        }
    }

    public void setOnClickCallback(OnClickCallback onClickCallback) {
        this.onClickCallback = onClickCallback;
    }

    public void setTitleTextView(String title) {
        titleTextView.setText(title);
    }
}
