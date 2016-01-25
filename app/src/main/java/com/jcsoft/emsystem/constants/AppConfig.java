package com.jcsoft.emsystem.constants;

import com.jcsoft.emsystem.bean.UserInfoBean;

/*
 * 全局的常量存放工具
 */
public class AppConfig {
    //极光推送的ID
    public static String registrationId;
    public static String REGISTRATION_ID;
    //登录名
    public static String loginName;
    public static String LOGIN_NAME = "loginName";
    //登录密码（MD5加密）
    public static String password;
    public static String PASSWORD = "password";
    //当前用户信息
    public static UserInfoBean userInfoBean;
    //用户被禁用或删除
    public static boolean isDisabled;
    public static int eventType;
    public static String eventMsg;
    //锁车状态
    public static boolean isLock;
    //正在执行锁车/解锁命令(1/0)
    public static Integer isExecuteLock;
    //正在执行开启/关闭电子围栏命令(1/0)
    public static Integer isExecuteVF;
    //执行的锁车是语音锁车/解锁，还是远程锁车/解锁(1：语音；2：远程)
    public static Integer lockCarType;
    /**
     * 自定义字段类型
     */
    public static final String SELECT2_FIELD = "select2_field";
    public static final String ADDRESS_SELECT = "address_select";
    public static final String ADDRESS_TEL = "address_tel";
    public static final String FIELD_MAP_FIELD = "field_map_field";
    public static final String MOBILE_FIELD = "mobile_field";
    public static final String TEL_FIELD = "tel_field";
    public static final String CURRENCY_FIELD = "currency_field";//商机的预计销售金额
    public static final String NUMBER_FIELD = "number_field";//数值类型
    public static final String FILE_FIELD = "file_field";
    public static final String CHECK_BOX = "check_box";
    public static final String RADIO_BUTTON = "radio_button";
    public static final String SELECT = "select";
    public static final String TIME_FIELD = "time_field";
    public static final String DATE_FIELD = "date_field";
    public static final String DATETIME_FIELD = "datetime_field";
    public static final String URL_FIELD = "url_field";
    public static final String EMAIL_FIELD = "email_field";
    public static final String TEXT_AREA = "text_area";
    public static final String TEXT_FIELD = "text_field";
    public static final String HIDDEN_FIELD = "hidden_field";
    public static final String ATTACHMENTS_FIELD = "attachments_field";//附件
    public static final String GO_ADDRESS_FIELD = "geo_address_field";
    public static final String EXPECT_SIGN_DATE = "expect_sign_date";
}
