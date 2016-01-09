package com.jcsoft.emsystem.view.formview;

import android.app.ActionBar;
import android.content.Context;
import android.text.InputType;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.jcsoft.emsystem.bean.FieldModel;
import com.jcsoft.emsystem.constants.AppConfig;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.LinkedHashMap;

/**
 * Created by dive on 2015/1/24.
 */
public class FormViewUtils {

    private static FormViewUtils instance;

    public static FormViewUtils getInstance() {
        return instance;
    }

    private static ActionBar.LayoutParams params = new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

    /**
     * 获取当前的日期
     *
     * @return
     */
    public static String getCurrDate(int timeType) {
        SimpleDateFormat simple = null;
        switch (timeType) {
            case 0:
                simple = new SimpleDateFormat("yyyy-MM-dd");
                break;
            case 1:
                simple = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                break;
        }
        Calendar calendar = Calendar.getInstance();
        return simple.format(calendar.getTime());
    }

    /**
     * @param myContext    上下文路径
     * @param tagText      名称
     * @param columnName   view的name
     * @param hintText     默认显示的值
     * @param single       是否单行
     * @param isRequired   是否必填
     * @param isShowDriver 是否线索底部的线
     * @return
     */
    public static FormEditTextView createEditTextView(Context myContext, String tagText, String columnName, String hintText, boolean isRequired, boolean single, boolean isShowDriver) {
        FormEditTextView view = new FormEditTextView(myContext);
        view.setLayoutParams(params);
        if (columnName.equals("address.tel") || columnName.equals("address.phone")) {
            view.setmEditTextInput(InputType.TYPE_CLASS_PHONE);
        } else if (columnName.equals("address.zip") || columnName.equals("address.qq") || columnName.equals("expect_amount")) {
            view.setmEditTextInput(InputType.TYPE_CLASS_NUMBER);
        } else if (columnName.equals("expect_amount") || columnName.equals("total_amount")) {
            view.setmEditTextInput(InputType.TYPE_CLASS_NUMBER);//暂时用phone类型 可以输入小数点
        } else {
            view.setmEditTextInput(InputType.TYPE_CLASS_TEXT);
        }
        view.setTagTextView(tagText);
        view.setmHintString(hintText);
        view.setIsRequired(isRequired);
        view.setViewSingle(single);
        view.setmShowDivider(isShowDriver);
        return view;
    }

    /**
     * @param myContext    上下文路径
     * @param tagText      名称
     * @param isUpdateView 默认显示的值
     * @param isShowDriver 是否线索底部的线
     * @return
     */
    public static FormTextEditAddressIntentView createEditAddressIntentView(Context myContext, String tagText, boolean isShowDriver, boolean isUpdateView, boolean isEditPermissions) {
        FormTextEditAddressIntentView view = new FormTextEditAddressIntentView(myContext);
        view.setLayoutParams(params);
        view.setTag(tagText);
        view.setHintText("请选择");
        view.setmShowDivider(isShowDriver);
        view.setmShowArrow(isUpdateView);
        if (isUpdateView) {
            if (isEditPermissions) {
                view.setClickable(true);
            } else {
                view.setClickable(false);
                view.setTipsString("暂无编辑权限");
            }
        } else {
            view.setClickable(false);
            view.setTipsString("");
        }
        return view;
    }

    /**
     * @param myContext    上下文路径
     * @param tagText      名称
     * @param isShowDriver 是否线索底部的线
     * @return
     */
    public static FormTextDialogView createFormTextDialogView(Context myContext, String tagText, boolean isShowDriver) {
        FormTextDialogView view = new FormTextDialogView(myContext);
        view.setLayoutParams(params);
        view.setTagTextView(tagText);
        view.setmShowDivider(isShowDriver);
        return view;
    }


    /**
     * @param myContext    上下文路径
     * @param tagText      名称
     * @param isShowDriver 是否线索底部的线
     * @return
     */
    public static FormAloneTextView createFormAloneTextView(Context myContext, String tagText, String textText, boolean isShowDriver) {
        FormAloneTextView view = new FormAloneTextView(myContext);
        view.setLayoutParams(params);
        view.setTagTextView(tagText);
        view.setmShowDivider(isShowDriver);
        return view;
    }

    /**
     * @param myContext    上下文路径
     * @param tagText      名称
     * @param isShowDriver 是否线索底部的线
     * @return
     */
    public static FormIntentSelectView createFormIntentSelectView(Context myContext, String tagText, boolean isShowDriver) {
        FormIntentSelectView view = new FormIntentSelectView(myContext);
        view.setLayoutParams(params);
        view.setmTagTextView(tagText);
        view.setmShowDivider(isShowDriver);
        return view;
    }

    /**
     * @param myContext    上下文路径
     * @param tagText      名称
     * @param isUpdateView view的值
     * @param isShowDriver 是否线索底部的线
     * @return
     */
    public static FormTextDialogView createFormTextDialogView(Context myContext, String tagText, boolean isShowDriver, boolean isUpdateView, boolean isEditPermissions) {
        FormTextDialogView view = new FormTextDialogView(myContext);
        view.setLayoutParams(params);
        view.setTagTextView(tagText);
        view.setmShowDivider(isShowDriver);
        view.setmShowArrow(isUpdateView);
        if (isUpdateView) {
            if (isEditPermissions) {
                view.setmFormViewOnclick(true);
            } else {
                view.setmFormViewOnclick(false);
                view.setTipsString("暂无编辑权限");
            }
        } else {
            view.setTipsString("");
        }
        return view;
    }

    /**
     * @param myContext 上下文路径
     * @param tagText   名称
     * @return
     */
    public static FormTextHeaderView createFormTextHeaderView(Context myContext, String tagText) {
        FormTextHeaderView view = new FormTextHeaderView(myContext);
        view.setLayoutParams(params);
        view.setTagTextView(tagText);
        return view;
    }

    /**
     * 名片查看*
     */
    public static FormTagView createFormTagView(Context myContext) {
        FormTagView view = new FormTagView(myContext);
        view.setLayoutParams(params);
        return view;
    }

    /**
     * @param myContext    上下文路径
     * @param tagText      名称
     * @param dateTimeType 日期时间选择类型  0为日期+时间 1为时间 2为日期
     * @param isShowDriver 是否线索底部的线
     * @return
     */
    public static FormTextDateTimeView createFormTextDateTimeView(Context myContext, String tagText, int dateTimeType, boolean isShowDriver, boolean isUpdateView, boolean isEditPermissions) {
        FormTextDateTimeView view = new FormTextDateTimeView(myContext, dateTimeType);
        view.setLayoutParams(params);
        view.setTag(tagText);
        view.setDateTimeType(dateTimeType);
        view.setShowDriver(isShowDriver);
        view.setmShowArrow(isUpdateView);
        if (isUpdateView) {
            if (isEditPermissions) {
                view.setmFormViewOnclick(true);
            } else {
                view.setmFormViewOnclick(false);
                view.setTipsString("暂无编辑权限");
            }
        } else {
            view.setmFormViewOnclick(false);
            view.setTipsString("");
        }
        return view;
    }

    /**
     * 自定义字段的详情编辑view
     *
     * @param context
     * @param lable
     * @param isUpdateView
     * @param isShowDriver
     * @param isEditPermissions
     * @return
     */
    public static FormTextUpdateView createFormTextUpdateView(Context context, String lable, boolean isUpdateView, boolean isShowDriver, boolean isEditPermissions) {
        FormTextUpdateView updateView = new FormTextUpdateView(context);
        updateView.setmShowDivider(isShowDriver);
        updateView.setTagTextView(lable);
        updateView.setmShowArrow(isUpdateView);
        if (isUpdateView) {
            updateView.setmShowArrow(true);
            if (isEditPermissions) {
                updateView.setmClickable(true);
            } else {
                updateView.setmClickable(false);
                updateView.setTipsString("暂无编辑权限");
            }
        } else {
            updateView.setmClickable(false);
            updateView.setmShowArrow(false);
        }
        return updateView;
    }

    /**
     * 详情界面的自定义字段 会根据相应的字段生成对应的view
     *
     * @param my_content_view 父view
     * @param fieldModel      当前view对象
     * @param mContext        上下文路径
     * @param isShowDriver    是否显示底部的线
     * @return
     */
    public static LinkedHashMap<FieldModel, View> createSelfFromEditView(LinearLayout my_content_view, FieldModel fieldModel, Context mContext, boolean isShowDriver, boolean isEditPermissions) {

        LinkedHashMap<FieldModel, View> hashMapList = new LinkedHashMap<FieldModel, View>();
        String type = fieldModel.getField_type();//view 类型
        String viewTag = fieldModel.getLabel();//当前的view 名称
        String required = "";//是否必填
        boolean isUpdate = !fieldModel.isCannot_edit();
        if (isUpdate) {
            if (AppConfig.CURRENCY_FIELD.equals(type)) {
                if (fieldModel.isRequired()) {
                    required = "必填";
                }
                FormEditTextView formEditTextView = createEditTextView(mContext, fieldModel.getLabel(), fieldModel.getCustom_column_name(), required, fieldModel.isRequired(), true, isShowDriver);
                formEditTextView.setTagTextView(fieldModel.getLabel());
                formEditTextView.setmEditTextViewType(true, 9);
                hashMapList.put(fieldModel, formEditTextView);
                my_content_view.addView(formEditTextView);
            } else if (AppConfig.SELECT2_FIELD.equals(type)) {
                FormIntentSelectView intentSelectView = createFormIntentSelectView(mContext, viewTag, isShowDriver);
                if (fieldModel.isRequired()) {
                    required = "必填";
                    intentSelectView.setHintText(required);
                }
                intentSelectView.setmTagTextView(fieldModel.getLabel());
                intentSelectView.setClickable(true);
                hashMapList.put(fieldModel, intentSelectView);//第一个字段：此字段对应取值的名称 第二个字段： 当前view类型  第三给字段 是否必填字段
                my_content_view.addView(intentSelectView);
            } else if (AppConfig.ADDRESS_SELECT.equals(type)) {
                FormTextEditAddressIntentView addressIntentView = createEditAddressIntentView(mContext, fieldModel.getLabel(), isShowDriver, true, isEditPermissions);
                addressIntentView.setIntentAction("com.vcooline.aike.activity.AddressSelectActivity");
                if (fieldModel.isRequired()) {
                    required = "必填";
                    addressIntentView.setHintText(required);
                }
                addressIntentView.setHasData(true);
                addressIntentView.setClickable(true);
                hashMapList.put(fieldModel, addressIntentView);
                my_content_view.addView(addressIntentView);
            } else if (AppConfig.TEL_FIELD.equals(type)) {
                if (fieldModel.isRequired()) {
                    required = "必填";
                }
                FormEditTextView formEditPhoneView = createEditTextView(mContext, fieldModel.getLabel(), fieldModel.getCustom_column_name(), required, fieldModel.isRequired(), true, isShowDriver);
                hashMapList.put(fieldModel, formEditPhoneView);
                my_content_view.addView(formEditPhoneView);
            } else if (AppConfig.FIELD_MAP_FIELD.equals(type)) {
                FormTextDialogView formTextDialogView = createFormTextDialogView(mContext, fieldModel.getLabel(), true);
                if (fieldModel.isRequired()) {
                    required = "必填";
                    formTextDialogView.setHintText(required);
                }
                if (fieldModel.getCustom_column_name().equals("stage_mapped")) {
                    formTextDialogView.setmShowDivider(false);
                }
                formTextDialogView.setmFormViewOnclick(true);
                hashMapList.put(fieldModel, formTextDialogView);
                my_content_view.addView(formTextDialogView);

            } else if (AppConfig.MOBILE_FIELD.equals(type)) {
                if (fieldModel.isRequired()) {
                    required = "必填";
                }
                FormEditTextView formEditPhoneView = createEditTextView(mContext, fieldModel.getLabel(), fieldModel.getCustom_column_name(), required, fieldModel.isRequired(), true, isShowDriver);
                hashMapList.put(fieldModel, formEditPhoneView);
                my_content_view.addView(formEditPhoneView);
            } else if (AppConfig.FILE_FIELD.equals(type)) {
                if (fieldModel.isRequired()) {
                    required = "必填";
                }
                //hashMapList.put(fieldModel.getCustom_column_name() + ".FormEditTextView", formEditTextView);
            } else if (AppConfig.URL_FIELD.equals(type)) {
                if (fieldModel.isRequired()) {
                    required = "必填";
                }
                FormEditTextView formEditTextView = createEditTextView(mContext, fieldModel.getLabel(), fieldModel.getCustom_column_name(), required, fieldModel.isRequired(), true, isShowDriver);
                hashMapList.put(fieldModel, formEditTextView);
                my_content_view.addView(formEditTextView);
            } else if (AppConfig.EMAIL_FIELD.equals(type)) {
                if (fieldModel.isRequired()) {
                    required = "必填";
                }
                FormEditTextView formEditTextView = createEditTextView(mContext, fieldModel.getLabel(), fieldModel.getCustom_column_name(), required, fieldModel.isRequired(), true, isShowDriver);
                hashMapList.put(fieldModel, formEditTextView);
                my_content_view.addView(formEditTextView);
            } else if (AppConfig.TEXT_AREA.equals(type)) {
                if (fieldModel.isRequired()) {
                    required = "必填";
                }
                FormEditTextView formEditTextView = createEditTextView(mContext, fieldModel.getLabel(), fieldModel.getCustom_column_name(), required, fieldModel.isRequired(), true, isShowDriver);
                hashMapList.put(fieldModel, formEditTextView);
                my_content_view.addView(formEditTextView);
            } else if (AppConfig.TEXT_FIELD.equals(type)) {
                if (fieldModel.isRequired()) {
                    required = "必填";
                }
                FormEditTextView formEditTextVie = createEditTextView(mContext, fieldModel.getLabel(), fieldModel.getCustom_column_name(), required, fieldModel.isRequired(), true, isShowDriver);
                hashMapList.put(fieldModel, formEditTextVie);
                my_content_view.addView(formEditTextVie);
            } else if (AppConfig.CHECK_BOX.equals(type)) {
                FormTextDialogView formTextDialogView = createFormTextDialogView(mContext, fieldModel.getLabel(), isShowDriver);
                if (fieldModel.isRequired()) {
                    required = "必填";
                    formTextDialogView.setHintText(required);
                }
                formTextDialogView.setTagTextView(fieldModel.getLabel());
                formTextDialogView.setmFormViewOnclick(true);
                hashMapList.put(fieldModel, formTextDialogView);
                my_content_view.addView(formTextDialogView);
            } else if (AppConfig.SELECT.equals(type)) {
                FormTextDialogView formTextSelectDialogView = createFormTextDialogView(mContext, fieldModel.getLabel(), isShowDriver);
                if (fieldModel.isRequired()) {
                    required = "必填";
                    formTextSelectDialogView.setHintText(required);
                }
                formTextSelectDialogView.setmFormViewOnclick(true);
                hashMapList.put(fieldModel, formTextSelectDialogView);
                my_content_view.addView(formTextSelectDialogView);
            } else if (AppConfig.RADIO_BUTTON.equals(type)) {
                FormTextDialogView formTextDialogView = createFormTextDialogView(mContext, fieldModel.getLabel(), isShowDriver);
                if (fieldModel.isRequired()) {
                    required = "必填";
                    formTextDialogView.setHintText(required);
                }
                formTextDialogView.setmFormViewOnclick(true);
                hashMapList.put(fieldModel, formTextDialogView);
                my_content_view.addView(formTextDialogView);
            } else if (AppConfig.TIME_FIELD.equals(type)) {
                FormTextDateTimeView formTextDateTimeView = createFormTextDateTimeView(mContext, viewTag, 1, isShowDriver, true, isEditPermissions);
                if (fieldModel.isRequired()) {
                    required = "必填";
                    formTextDateTimeView.setHintText(required);
                }
                formTextDateTimeView.setDateTimeType(1);
                formTextDateTimeView.setDateText(getCurrDate(0));
                formTextDateTimeView.setmFormViewOnclick(true);
                hashMapList.put(fieldModel, formTextDateTimeView);
                my_content_view.addView(formTextDateTimeView);
            } else if (AppConfig.DATE_FIELD.equals(type)) {
                FormTextDateTimeView formTextDateTimeView = createFormTextDateTimeView(mContext, viewTag, 0, isShowDriver, true, isEditPermissions);
                if (fieldModel.isRequired()) {
                    required = "必填";
                    formTextDateTimeView.setHintText(required);
                } else {
                    formTextDateTimeView.setHintText("");
                }
                formTextDateTimeView.setDateTimeType(0);
                if (fieldModel.getCustom_column_name().equals("birth_date") || fieldModel.getCustom_column_name().equals("sign_date")) {
                    formTextDateTimeView.setShowDriver(true);
                }
                formTextDateTimeView.setmFormViewOnclick(true);
                hashMapList.put(fieldModel, formTextDateTimeView);
                my_content_view.addView(formTextDateTimeView);
            } else if (AppConfig.DATETIME_FIELD.equals(type)) {
                FormTextDateTimeView formTextDateTimeView = createFormTextDateTimeView(mContext, viewTag, 2, isShowDriver, true, isEditPermissions);
                if (fieldModel.isRequired()) {
                    required = "必填";
                    formTextDateTimeView.setHintText(required);
                }
                formTextDateTimeView.setDateTimeType(2);
                formTextDateTimeView.setDateText(getCurrDate(1));
                formTextDateTimeView.setmFormViewOnclick(true);
                hashMapList.put(fieldModel, formTextDateTimeView);
                my_content_view.addView(formTextDateTimeView);
            } else if (AppConfig.HIDDEN_FIELD.equals(type)) {
                FormTextHeaderView formTextHeaderView = createFormTextHeaderView(mContext, "");
                formTextHeaderView.setTag(fieldModel.getLabel());
                hashMapList.put(fieldModel, formTextHeaderView);
                my_content_view.addView(formTextHeaderView);
            } else if (AppConfig.GO_ADDRESS_FIELD.equals(type)) {
                if (fieldModel.isRequired()) {
                    required = "必填";
                }
                FormEditTextView addFieldIntentView = createEditTextView(mContext, fieldModel.getLabel(), fieldModel.getCustom_column_name(), required, fieldModel.isRequired(), true, isShowDriver);
                my_content_view.addView(addFieldIntentView);
                hashMapList.put(fieldModel, addFieldIntentView);
            } else if (AppConfig.CURRENCY_FIELD.equals(type)) {
                if (fieldModel.isRequired()) {
                    required = "必填";
                }
                FormEditTextView formEditCurrencyView = createEditTextView(mContext, fieldModel.getLabel(), fieldModel.getCustom_column_name(), required, fieldModel.isRequired(), true, isShowDriver);
                formEditCurrencyView.setmEditTextInput(InputType.TYPE_CLASS_NUMBER);
                formEditCurrencyView.setmEditTextViewType(true, 9);
                hashMapList.put(fieldModel, formEditCurrencyView);
                my_content_view.addView(formEditCurrencyView);
            } else if (AppConfig.EXPECT_SIGN_DATE.equals(type)) {
                FormTextDateTimeView formTextDateTimeView = createFormTextDateTimeView(mContext, fieldModel.getLabel(), 0, isShowDriver, true, isEditPermissions);
                if (fieldModel.isRequired()) {
                    required = "必填";
                    formTextDateTimeView.setHintText(required);
                }
                formTextDateTimeView.setDateTimeType(0);
                formTextDateTimeView.setDateText(getCurrDate(0));
                formTextDateTimeView.setmFormViewOnclick(true);
                hashMapList.put(fieldModel, formTextDateTimeView);
                my_content_view.addView(formTextDateTimeView);
            } else if (AppConfig.NUMBER_FIELD.equals(type)) {//新增的数字类型
                if (fieldModel.isRequired()) {
                    required = "必填";
                }
                FormEditTextView formEditTextVie = createEditTextView(mContext, fieldModel.getLabel(), fieldModel.getCustom_column_name(), required, fieldModel.isRequired(), true, isShowDriver);
                formEditTextVie.setmEditTextInput(InputType.TYPE_CLASS_TEXT);//TYPE_CLASS_NUMBER
                formEditTextVie.setmEditTextViewType(true, 100);
                hashMapList.put(fieldModel, formEditTextVie);
                my_content_view.addView(formEditTextVie);
            }
        }
        return hashMapList;
    }

    /**
     * 自定义字段详情界面修改的view
     *
     * @param my_content_view
     * @param fieldModel
     * @param mContext
     * @param isShowDriver
     * @return
     */
    public static LinkedHashMap<FieldModel, View> createUpdateFormEditView(LinearLayout my_content_view, FieldModel fieldModel, Context mContext, boolean isShowDriver, boolean isEditPermissions) {

        LinkedHashMap<FieldModel, View> hashMapList = new LinkedHashMap<FieldModel, View>();
        String type = fieldModel.getField_type();//view 类型
        String custom_column_name = fieldModel.getCustom_column_name();//view name
        boolean isUpdate = !fieldModel.isCannot_edit();
        if (AppConfig.CURRENCY_FIELD.equals(type)) {
            FormTextUpdateView formEditTextView = createFormTextUpdateView(mContext, fieldModel.getLabel(), isUpdate, isShowDriver, isEditPermissions);
            hashMapList.put(fieldModel, formEditTextView);
            my_content_view.addView(formEditTextView);
        } else if (AppConfig.SELECT2_FIELD.equals(type)) {
            FormTextUpdateView formEditTextView = createFormTextUpdateView(mContext, fieldModel.getLabel(), isUpdate, isShowDriver, isEditPermissions);
            hashMapList.put(fieldModel, formEditTextView);//第一个字段：此字段对应取值的名称 第二个字段： 当前view类型  第三给字段 是否必填字段
            my_content_view.addView(formEditTextView);
        } else if (AppConfig.ADDRESS_SELECT.equals(type)) {
            FormTextEditAddressIntentView addressIntentView = createEditAddressIntentView(mContext, fieldModel.getLabel(), isShowDriver, isUpdate, isEditPermissions);
            addressIntentView.setIntentAction("com.vcooline.aike.activity.AddressSelectActivity");
            hashMapList.put(fieldModel, addressIntentView);
            my_content_view.addView(addressIntentView);
        } else if (AppConfig.TEL_FIELD.equals(type)) {
            FormTextUpdateView formEditTextView = createFormTextUpdateView(mContext, fieldModel.getLabel(), isUpdate, isShowDriver, isEditPermissions);
            hashMapList.put(fieldModel, formEditTextView);
            my_content_view.addView(formEditTextView);
        } else if (AppConfig.FIELD_MAP_FIELD.equals(type)) {

            FormTextDialogView formTextDialogView = createFormTextDialogView(mContext, fieldModel.getLabel(), isShowDriver, isUpdate, isEditPermissions);
            formTextDialogView.setCustom_column_name(custom_column_name);
            hashMapList.put(fieldModel, formTextDialogView);
            my_content_view.addView(formTextDialogView);

        } else if (AppConfig.MOBILE_FIELD.equals(type)) {
            FormTextUpdateView formEditTextView = createFormTextUpdateView(mContext, fieldModel.getLabel(), isUpdate, isShowDriver, isEditPermissions);
            hashMapList.put(fieldModel, formEditTextView);
            my_content_view.addView(formEditTextView);
        } else if (AppConfig.FILE_FIELD.equals(type)) {
            FormTextUpdateView formEditTextView = createFormTextUpdateView(mContext, fieldModel.getLabel(), isUpdate, isShowDriver, isEditPermissions);
            hashMapList.put(fieldModel, formEditTextView);
            my_content_view.addView(formEditTextView);
        } else if (AppConfig.URL_FIELD.equals(type)) {
            FormTextUpdateView formEditTextView = createFormTextUpdateView(mContext, fieldModel.getLabel(), isUpdate, isShowDriver, isEditPermissions);
            hashMapList.put(fieldModel, formEditTextView);
            my_content_view.addView(formEditTextView);
        } else if (AppConfig.EMAIL_FIELD.equals(type)) {
            FormTextUpdateView formEditTextView = createFormTextUpdateView(mContext, fieldModel.getLabel(), isUpdate, isShowDriver, isEditPermissions);
            hashMapList.put(fieldModel, formEditTextView);
            my_content_view.addView(formEditTextView);
        } else if (AppConfig.TEXT_AREA.equals(type)) {
            FormTextUpdateView formEditTextView = createFormTextUpdateView(mContext, fieldModel.getLabel(), isUpdate, isShowDriver, isEditPermissions);
            hashMapList.put(fieldModel, formEditTextView);
            my_content_view.addView(formEditTextView);
        } else if (AppConfig.TEXT_FIELD.equals(type)) {
            FormTextUpdateView formEditTextView = createFormTextUpdateView(mContext, fieldModel.getLabel(), isUpdate, isShowDriver, isEditPermissions);
            hashMapList.put(fieldModel, formEditTextView);
            my_content_view.addView(formEditTextView);
        } else if (AppConfig.CHECK_BOX.equals(type)) {
            FormTextDialogView formTextDialogView = createFormTextDialogView(mContext, fieldModel.getLabel(), isShowDriver, isUpdate, isEditPermissions);
            formTextDialogView.setCustom_column_name(custom_column_name);
            hashMapList.put(fieldModel, formTextDialogView);
            my_content_view.addView(formTextDialogView);
        } else if (AppConfig.SELECT.equals(type)) {

            FormTextDialogView formTextSelectDialogView = createFormTextDialogView(mContext, fieldModel.getLabel(), isShowDriver, isUpdate, isEditPermissions);
            formTextSelectDialogView.setCustom_column_name(custom_column_name);
            hashMapList.put(fieldModel, formTextSelectDialogView);
            my_content_view.addView(formTextSelectDialogView);
        } else if (AppConfig.RADIO_BUTTON.equals(type)) {

            FormTextDialogView formTextDialogView = createFormTextDialogView(mContext, fieldModel.getLabel(), isShowDriver, isUpdate, isEditPermissions);
            formTextDialogView.setCustom_column_name(custom_column_name);
            hashMapList.put(fieldModel, formTextDialogView);
            my_content_view.addView(formTextDialogView);
        } else if (AppConfig.TIME_FIELD.equals(type)) {

            FormTextDateTimeView formTextDateTimeView = createFormTextDateTimeView(mContext, fieldModel.getLabel(), 0, isShowDriver, isUpdate, isEditPermissions);
            formTextDateTimeView.setCustom_column_name(fieldModel.getCustom_column_name());
            formTextDateTimeView.setDateTimeType(0);
            formTextDateTimeView.setDateText(getCurrDate(0));
            hashMapList.put(fieldModel, formTextDateTimeView);
            my_content_view.addView(formTextDateTimeView);
        } else if (AppConfig.DATE_FIELD.equals(type)) {
            FormTextDateTimeView formTextDateTimeView = createFormTextDateTimeView(mContext, fieldModel.getLabel(), 0, isShowDriver, isUpdate, isEditPermissions);
            formTextDateTimeView.setCustom_column_name(fieldModel.getCustom_column_name());
            formTextDateTimeView.setDateTimeType(0);
            formTextDateTimeView.setDateText(getCurrDate(0));
            hashMapList.put(fieldModel, formTextDateTimeView);
            my_content_view.addView(formTextDateTimeView);
        } else if (AppConfig.DATETIME_FIELD.equals(type)) {
            FormTextDateTimeView formTextDateTimeView = createFormTextDateTimeView(mContext, fieldModel.getLabel(), 0, isShowDriver, isUpdate, isEditPermissions);
            formTextDateTimeView.setCustom_column_name(fieldModel.getCustom_column_name());
            formTextDateTimeView.setDateTimeType(2);
            formTextDateTimeView.setDateText(getCurrDate(1));
            hashMapList.put(fieldModel, formTextDateTimeView);
            my_content_view.addView(formTextDateTimeView);
        } else if (AppConfig.HIDDEN_FIELD.equals(type)) {
            FormTextHeaderView formTextHeaderView = createFormTextHeaderView(mContext, "");
            formTextHeaderView.setTag(fieldModel.getLabel());
            hashMapList.put(fieldModel, formTextHeaderView);
            my_content_view.addView(formTextHeaderView);
        } else if (AppConfig.GO_ADDRESS_FIELD.equals(type)) {
            FormTextUpdateView formEditTextView = createFormTextUpdateView(mContext, fieldModel.getLabel(), isUpdate, isShowDriver, isEditPermissions);
            hashMapList.put(fieldModel, formEditTextView);
            my_content_view.addView(formEditTextView);
        } else if (AppConfig.CURRENCY_FIELD.equals(type)) {
            FormTextUpdateView formEditTextView = createFormTextUpdateView(mContext, fieldModel.getLabel(), isUpdate, isShowDriver, isEditPermissions);
            hashMapList.put(fieldModel, formEditTextView);
            my_content_view.addView(formEditTextView);
        } else if (AppConfig.EXPECT_SIGN_DATE.equals(type)) {
            FormTextDateTimeView formTextDateTimeView = createFormTextDateTimeView(mContext, fieldModel.getLabel(), 0, isShowDriver, isUpdate, isEditPermissions);
            formTextDateTimeView.setCustom_column_name(fieldModel.getCustom_column_name());
            formTextDateTimeView.setDateTimeType(0);
            formTextDateTimeView.setDateText(getCurrDate(0));
            hashMapList.put(fieldModel, formTextDateTimeView);
            my_content_view.addView(formTextDateTimeView);
        } else if (AppConfig.NUMBER_FIELD.equals(type)) {//新增的数字类型
            FormTextUpdateView formEditTextView = createFormTextUpdateView(mContext, fieldModel.getLabel(), isUpdate, isShowDriver, isEditPermissions);
            hashMapList.put(fieldModel, formEditTextView);
            my_content_view.addView(formEditTextView);
        } else if (AppConfig.ATTACHMENTS_FIELD.equals(type)) {//附件类型
            FormTextUpdateView formEditTextView = createFormTextUpdateView(mContext, fieldModel.getLabel(), isUpdate, isShowDriver, isEditPermissions);
            hashMapList.put(fieldModel, formEditTextView);
            my_content_view.addView(formEditTextView);
        }
        return hashMapList;
    }
}
