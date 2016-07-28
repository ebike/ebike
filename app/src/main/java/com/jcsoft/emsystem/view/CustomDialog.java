package com.jcsoft.emsystem.view;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jcsoft.emsystem.R;
import com.jcsoft.emsystem.utils.CommonUtils;

/**
 * Created by jimmy on 2015/11/18.
 */
public class CustomDialog extends Dialog {

    public CustomDialog(Context context) {
        super(context);
    }

    public CustomDialog(Context context, int theme) {
        super(context, theme);
    }

    public static class Builder {
        private Context context; //上下文对象
        private String title; //对话框标题
        private String message; //对话框内容
        private int msgGravity;//文字内容的排放方式
        private boolean hasEditText;//是否含编辑框
        private String editText;//输入的内容
        private String hintText;//默认显示内容
        private int inputType;//输入内容限制
        private String confirm_btnText; //按钮名称“确定”
        private String cancel_btnText; //按钮名称“取消”
        private View contentView; //对话框中间加载的其他布局界面
        /*按钮坚挺事件*/
        private DialogInterface.OnClickListener confirm_btnClickListener;
        private DialogInterface.OnClickListener cancel_btnClickListener;

        public Builder(Context context) {
            this.context = context;
        }

        /*设置对话框信息*/
        public Builder setMessage(String message) {
            this.message = message;
            return this;
        }

        public void setHasEditText(boolean hasEditText) {
            this.hasEditText = hasEditText;
        }

        public String getEditText() {
            return editText;
        }

        public void setInputType(int inputType) {
            this.inputType = inputType;
        }

        public void setHintText(String hintText) {
            this.hintText = hintText;
        }

        /**
         * Set the Dialog message from resource
         *
         * @param message
         * @return
         */
        public Builder setMessage(int message) {
            this.message = (String) context.getText(message);
            return this;
        }

        /**
         * Set the Dialog title from resource
         *
         * @param title
         * @return
         */
        public Builder setTitle(int title) {
            this.title = (String) context.getText(title);
            return this;
        }

        /**
         * Set the Dialog title from String
         *
         * @param title
         * @return
         */
        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        /**
         * 设置对话框界面
         *
         * @param v View
         * @return
         */
        public Builder setContentView(View v) {
            this.contentView = v;
            return this;
        }

        /**
         * Set the positive button resource and it's listener
         *
         * @param confirm_btnText
         * @return
         */
        public Builder setPositiveButton(int confirm_btnText,
                                         DialogInterface.OnClickListener listener) {
            if (context != null) {
                this.confirm_btnText = (String) context.getText(confirm_btnText);
            }
            this.confirm_btnClickListener = listener;
            return this;
        }

        /**
         * Set the positive button and it's listener
         *
         * @param confirm_btnText
         * @return
         */
        public Builder setPositiveButton(String confirm_btnText,
                                         DialogInterface.OnClickListener listener) {
            this.confirm_btnText = confirm_btnText;
            this.confirm_btnClickListener = listener;
            return this;
        }

        /**
         * Set the negative button resource and it's listener
         *
         * @param cancel_btnText
         * @return
         */
        public Builder setNegativeButton(int cancel_btnText,
                                         DialogInterface.OnClickListener listener) {
            if (context != null) {
                this.cancel_btnText = (String) context.getText(cancel_btnText);
            }
            this.cancel_btnClickListener = listener;
            return this;
        }

        /**
         * Set the negative button and it's listener
         *
         * @param cancel_btnText
         * @return
         */
        public Builder setNegativeButton(String cancel_btnText,
                                         DialogInterface.OnClickListener listener) {
            this.cancel_btnText = cancel_btnText;
            this.cancel_btnClickListener = listener;
            return this;
        }

        public Builder setMsgGravity(int msgGravity) {
            this.msgGravity = msgGravity;
            return this;
        }

        public CustomDialog create() {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final CustomDialog dialog = new CustomDialog(context, R.style.dialog_custom_style);
            View layout = inflater.inflate(R.layout.dialog_custom, null);
            dialog.addContentView(layout, new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            //初始化组件
            final EditText mEditText = (EditText) layout.findViewById(R.id.editText);
            TextView titleTextView = (TextView) layout.findViewById(R.id.title);
            Button confirmButton = (Button) layout.findViewById(R.id.confirm_btn);
            Button cancelButton = (Button) layout.findViewById(R.id.cancel_btn);
            JustifyTextView messageTextView = (JustifyTextView) layout.findViewById(R.id.message);
            TextView divider = (TextView) layout.findViewById(R.id.divider);
            LinearLayout contentViewLL = (LinearLayout) layout.findViewById(R.id.contentView);
            LinearLayout btnLayout = (LinearLayout) layout.findViewById(R.id.ll_btn);
            LinearLayout lineLayout = (LinearLayout) layout.findViewById(R.id.ll_line);
            //设置标题
            titleTextView.setText(title);
            titleTextView.getPaint().setFakeBoldText(true);
            //设置确认按钮
            if (confirm_btnText != null) {
                confirmButton.setText(confirm_btnText);
                if (confirm_btnClickListener != null) {
                    confirmButton.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            confirm_btnClickListener.onClick(dialog,
                                    DialogInterface.BUTTON_POSITIVE);
                        }
                    });
                }
            } else {
                confirmButton.setVisibility(View.GONE);
            }
            //设置取消按钮
            if (cancel_btnText != null) {
                cancelButton.setText(cancel_btnText);
                if (cancel_btnClickListener != null) {
                    cancelButton.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            cancel_btnClickListener.onClick(dialog,
                                    DialogInterface.BUTTON_NEGATIVE);
                        }
                    });
                }
            } else {
                cancelButton.setVisibility(View.GONE);
                divider.setVisibility(View.GONE);
                //没有取消按钮时，底部只有一个按钮，需要修改该按钮的圆角,否则，一边是圆角，一边是方角
                confirmButton.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.single_btn_select));
            }
            //无按钮情况
            if (CommonUtils.strIsEmpty(confirm_btnText) && CommonUtils.strIsEmpty(cancel_btnText)) {
                btnLayout.setVisibility(View.GONE);
                lineLayout.setVisibility(View.GONE);
            }
            //设置文字内容
            if (message != null) {
                messageTextView.setText(message);
            } else if (contentView != null) {
                // if no message set
                // add the contentView to the dialog body
                contentViewLL.setVisibility(View.VISIBLE);
                contentViewLL.removeAllViews();
                contentViewLL.addView(contentView, new ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT));
            }
            //设置文字内容排放方式
            if (msgGravity != 0) {
                messageTextView.setGravity(msgGravity);
            }
            //设置是否显示输入框
            if (hasEditText) {
                mEditText.setVisibility(View.VISIBLE);
                //设置输入框默认内容
                if (!CommonUtils.strIsEmpty(hintText)) {
                    mEditText.setHint(hintText);
                } else {
                    mEditText.setHint("");
                }
                //设置输入限制
                if (inputType != 0) {
                    mEditText.setInputType(inputType);
                }
                //添加内容变化监听
                mEditText.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        editText = mEditText.getText().toString();
                    }
                });
            } else {
                mEditText.setVisibility(View.GONE);
            }
            dialog.setContentView(layout);
            return dialog;
        }
    }
}
