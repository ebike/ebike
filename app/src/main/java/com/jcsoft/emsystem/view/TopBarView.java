package com.jcsoft.emsystem.view;

import android.app.Activity;
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
 * Created by jimmy on 15/12/4.
 */
public class TopBarView extends RelativeLayout {
    private Context context;
    //总布局
    private View rootView;
    //左侧布局
    @ViewInject(R.id.relayout_left)
    RelativeLayout leftRelativeLayout;
    //左侧文字
    @ViewInject(R.id.tv_left)
    TextView leftTextView;
    //左侧图片
    @ViewInject(R.id.iv_left)
    ImageView leftImageView;
    //中间标题
    @ViewInject(R.id.tv_center)
    TextView centerTextView;
    //右侧布局
    @ViewInject(R.id.relayout_right)
    RelativeLayout rightRelativeLayout;
    //右侧文字
    @ViewInject(R.id.tv_right)
    TextView rightTextView;
    //右侧图片
    @ViewInject(R.id.iv_right)
    ImageView rightImageView;
    //是否显示顶部菜单左侧布局
    private boolean has_left;
    //是否显示顶部菜单左侧文字
    private String left_text;
    //是否显示顶部菜单左侧图片
    private Integer left_icon;
    //顶部菜单左侧布局是否可点击
    private boolean has_left_clickable;
    //是否显示顶部菜单中间布局
    private boolean has_center;
    //是否显示顶部菜单中间文字
    private String center_text;
    //是否显示顶部菜单右侧布局
    private boolean has_right;
    //是否显示顶部菜单右侧文字
    private String right_text;
    //是否显示顶部菜单右侧图片
    private Integer right_icon;
    //顶部菜单右侧布局是否可点击
    private boolean has_right_clickable;
    //左侧点击是关闭当前活动
    private boolean back_is_finish;
    //回调接口
    private TopBarLeftCallback leftCallback;
    private TopBarRightCallback rightCallback;

    //左侧布局点击事件
    public interface TopBarLeftCallback {
        void setLeftOnClickListener();
    }

    //右侧布局点击事件
    public interface TopBarRightCallback {
        void setRightOnClickListener();
    }

    public TopBarView(Context context) {
        this(context, null);
    }

    public TopBarView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TopBarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyle) {
        //获取字段信息
        TypedArray typedArray = getContext().obtainStyledAttributes(
                attrs, R.styleable.TopBarView, defStyle, 0);
        has_left = typedArray.getBoolean(R.styleable.TopBarView_has_left, false);
        left_text = typedArray.getString(R.styleable.TopBarView_left_text);
        left_icon = typedArray.getResourceId(R.styleable.TopBarView_left_icon, 0);
        has_left_clickable = typedArray.getBoolean(R.styleable.TopBarView_has_left_clickable, false);
        has_center = typedArray.getBoolean(R.styleable.TopBarView_has_center, false);
        center_text = typedArray.getString(R.styleable.TopBarView_center_text);
        has_right = typedArray.getBoolean(R.styleable.TopBarView_has_right, false);
        right_text = typedArray.getString(R.styleable.TopBarView_right_text);
        right_icon = typedArray.getResourceId(R.styleable.TopBarView_right_icon, 0);
        has_right_clickable = typedArray.getBoolean(R.styleable.TopBarView_has_right_clickable, false);
        back_is_finish = typedArray.getBoolean(R.styleable.TopBarView_back_is_finish, false);
        typedArray.recycle();
        //获取布局文件
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        rootView = inflater.inflate(R.layout.view_top_bar, this, true);
        x.view().inject(this, rootView);
        //初始化组件
        //设置左侧布局
        if (has_left) {
            leftRelativeLayout.setVisibility(View.VISIBLE);
            //左侧文字不为空时，显示文字
            if (!CommonUtils.strIsEmpty(left_text)) {
                leftTextView.setVisibility(View.VISIBLE);
                leftTextView.setText(left_text);
            } else {
                leftTextView.setVisibility(View.GONE);
            }
            //左侧图片不为0时，显示
            if (left_icon != 0) {
                leftImageView.setVisibility(View.VISIBLE);
                leftImageView.setImageResource(left_icon);
            } else {
                leftImageView.setVisibility(View.GONE);
            }
        } else {
            leftRelativeLayout.setVisibility(View.GONE);
        }
        //设置中间布局
        if (has_center) {
            centerTextView.setVisibility(View.VISIBLE);
            if (!CommonUtils.strIsEmpty(center_text)) {
                centerTextView.setText(center_text);
            }
        } else {
            centerTextView.setVisibility(View.GONE);
        }
        //设置右侧布局
        if (has_right) {
            rightRelativeLayout.setVisibility(View.VISIBLE);
            //右侧文字不为空时，显示文字
            if (!CommonUtils.strIsEmpty(right_text)) {
                rightTextView.setVisibility(View.VISIBLE);
                rightTextView.setText(right_text);
            } else {
                rightTextView.setVisibility(View.GONE);
            }
            //右侧图片不为0时，显示
            if (right_icon != 0) {
                rightImageView.setVisibility(View.VISIBLE);
                rightImageView.setImageResource(right_icon);
            } else {
                rightImageView.setVisibility(View.GONE);
            }
        } else {
            rightRelativeLayout.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        //添加事件监听
        //左侧布局点击事件
        if (has_left_clickable) {
            leftRelativeLayout.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (leftCallback != null) {
                        leftCallback.setLeftOnClickListener();
                    } else if (back_is_finish && context instanceof Activity) {
                        ((Activity) context).finish();
                    }
                }
            });
        }
        //右侧布局点击事件
        if (has_right_clickable) {
            rightRelativeLayout.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (rightCallback != null) {
                        rightCallback.setRightOnClickListener();
                    }
                }
            });
        }
    }

    //设置右边文字是否置灰
    public void setRightTextViewEnabled(boolean enabled) {
        rightRelativeLayout.setEnabled(enabled);
        if (enabled) {
            rightTextView.setTextColor(getResources().getColor(R.color.white));
        } else {
            rightTextView.setTextColor(getResources().getColor(R.color.dc_title_text));
        }
    }

    //设置右侧文字是否显示
    public void setRightTextViewVisibility(int visibility) {
        rightTextView.setVisibility(visibility);
    }

    //设置右侧文字
    public void setRightTextView(String text) {
        rightRelativeLayout.setVisibility(View.VISIBLE);
        rightTextView.setVisibility(View.VISIBLE);
        rightTextView.setText(text);
    }

    //设置右侧文字
    public void setRightTextView(int resource) {
        rightRelativeLayout.setVisibility(View.VISIBLE);
        rightTextView.setVisibility(View.VISIBLE);
        rightTextView.setText(context.getText(resource));
    }

    //设置标题文字
    public void setCenterTextView(String text) {
        centerTextView.setText(text);
    }

    //设置标题文字
    public void setCenterTextView(int resource) {
        centerTextView.setText(context.getText(resource));
    }

    public void setLeftCallback(TopBarLeftCallback leftCallback) {
        this.leftCallback = leftCallback;
    }

    public void setRightCallback(TopBarRightCallback rightCallback) {
        this.rightCallback = rightCallback;
    }
}
