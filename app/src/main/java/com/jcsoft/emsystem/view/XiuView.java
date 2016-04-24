package com.jcsoft.emsystem.view;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.jcsoft.emsystem.R;

/**
 * Created by jimmy on 16/4/14.
 */
public class XiuView extends FrameLayout {

    /**
     * 按钮
     */
    private ImageView iv_btn;

    /**
     * 按钮点击是否有效（无效即：无动画，无操作）
     */
    private boolean enabled = true;

    /**
     * 按钮按下状态资源
     */
    private int btnPressResource;

    /**
     * 装波纹的容器
     */
    private FrameLayout fl_move_circle;

    /**
     * 波纹背景资源
     */
    private int circleResource;

    /**
     * 是否循环波纹
     */
    private boolean isCycle = true;

    /**
     * 按钮点击监听
     */
    private OnBtnPressListener onBtnPressListener;

    public void setOnBtnPressListener(OnBtnPressListener onBtnPressListener) {
        this.onBtnPressListener = onBtnPressListener;
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            addMoveCircleHandler.sendEmptyMessage(1);
            addMoveCircleHandler.sendEmptyMessageDelayed(1, 400);
            addMoveCircleHandler.sendEmptyMessageDelayed(1, 800);
        }
    };

    private Handler addMoveCircleHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            addMoveCircle();
        }
    };

    public XiuView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    /**
     * 初始化控件个监听按钮
     */
    private void initView() {
        View v = LayoutInflater.from(getContext()).inflate(R.layout.xiu_view, null, false);
        addView(v, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        iv_btn = (ImageView) findViewById(R.id.iv_btn);
        fl_move_circle = (FrameLayout) findViewById(R.id.fl_move_circle);
        iv_btn.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN://手指按钮
                        if (enabled) {
                            if (btnPressResource != 0) {
                                iv_btn.setImageResource(btnPressResource);
                            }
                            //手指又按下，取消掉显示循环波纹的消息
                            handler.removeMessages(1);
                            pressDown();
                        }
                        break;
                    case MotionEvent.ACTION_UP://手指抬起
                        if (enabled) {
                            pressUp();
                            handler.sendEmptyMessage(1);
                            if (onBtnPressListener != null) {
                                onBtnPressListener.btnClick();
                            }
                        }
                        break;
                }
                return true;
            }
        });
    }

    /**
     * 发散波纹
     */
    private void addMoveCircle() {
        final ImageView imageView = new ImageView(getContext());
        LayoutParams lp = new LayoutParams(dip2px(getContext(), 120), dip2px(getContext(), 120));
        lp.gravity = Gravity.CENTER;
        imageView.setLayoutParams(lp);
        if (circleResource != 0) {
            imageView.setImageResource(circleResource);
        } else {
            imageView.setImageResource(R.mipmap.bg_control_close);
        }
        fl_move_circle.addView(imageView);
        ObjectAnimator outCircleAnimX = ObjectAnimator.ofFloat(imageView, "scaleX", 1f, 3f);
        ObjectAnimator outCircleAnimY = ObjectAnimator.ofFloat(imageView, "scaleY", 1f, 3f);
        ObjectAnimator alphaAnim = ObjectAnimator.ofFloat(imageView, "alpha", 0.6f, 0);
        outCircleAnimX.setDuration(1500);
        outCircleAnimY.setDuration(1500);
        alphaAnim.setDuration(1500);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(outCircleAnimX, outCircleAnimY, alphaAnim);
        animatorSet.start();
        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                //移除掉刚才添加的波纹
                fl_move_circle.removeView(imageView);
                if (fl_move_circle.getChildCount() == 0 && isCycle) {
                    handler.sendEmptyMessage(1);
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    /**
     * 按下按钮
     */
    private void pressDown() {
        AnimatorSet animatorSet = new AnimatorSet();
        ObjectAnimator scaleXIn = ObjectAnimator.ofFloat(iv_btn, "scaleX", 1f, 0.94f);
        scaleXIn.setDuration(400);
        scaleXIn.setInterpolator(new LinearInterpolator());
        ObjectAnimator scaleYIn = ObjectAnimator.ofFloat(iv_btn, "scaleY", 1f, 0.94f);
        scaleYIn.setDuration(400);
        scaleYIn.setInterpolator(new LinearInterpolator());
        animatorSet.playTogether(scaleXIn, scaleYIn);
        animatorSet.start();
    }

    /**
     * 抬起按钮
     */
    private void pressUp() {
        AnimatorSet animatorSet1 = new AnimatorSet();
        ObjectAnimator scaleXOut = ObjectAnimator.ofFloat(iv_btn, "scaleX", 0.94f, 1.06f, 1f);
        scaleXOut.setDuration(500);
        scaleXOut.setInterpolator(new LinearInterpolator());
        ObjectAnimator scaleYOut = ObjectAnimator.ofFloat(iv_btn, "scaleY", 0.94f, 1.06f, 1f);
        scaleYOut.setDuration(500);
        scaleYOut.setInterpolator(new LinearInterpolator());
        animatorSet1.playTogether(scaleXOut, scaleYOut);
        animatorSet1.start();
    }

    /**
     * 根据手机的分辨率从 dip 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 按钮点击监听
     */
    public interface OnBtnPressListener {
        void btnClick();
    }

    public void setBtnNormalResource(int btnNormalResource) {
        iv_btn.setImageResource(btnNormalResource);
    }

    public void setBtnPressResource(int btnPressResource) {
        this.btnPressResource = btnPressResource;
    }

    /**
     * 默认开始波纹动画
     */
    public void openMoveCircle() {
        handler.sendEmptyMessage(1);
    }

    /**
     * 是否循环波纹动画
     */
    public void setIsCycle(boolean isCycle) {
        this.isCycle = isCycle;
    }

    /**
     * 清除波纹
     */
    public void clearMoveCircle() {
        if (fl_move_circle.getChildCount() > 0) {
            fl_move_circle.removeAllViews();
        }
    }

    @Override
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void setCircleResource(int circleResource) {
        this.circleResource = circleResource;
    }
}