package com.jcsoft.emsystem.constants;

import android.app.Activity;
import android.os.Environment;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/*
 * 全局的常量存放工具
 */
public class AppConfig {
	/** 本地保存用户账户 **/
	public static String USER_ACCOUNT = "userAccount";
	public static String userAccount;
	/** 本地保存用户Id **/
	public static String USER_ID = "userId";
	public static int userId;
	/** 本地保存用户Token **/
	public static String USER_TOKE = "userToken";
	public static String userToken;
	/** 本地保存用户公司Id **/
	public static String ORG_ID = "orgId";
	public static int orgId;
	/** 本地保存用户公司Id **/
	public static String ORG_NAME = "orgName";
	public static String orgName;
	/** 本地保存用户公司Id **/
	public static String ORG_SERIAL_NUBER = "orgSerialNumber";
	public static String orgSerialNumber;
	/** 用户被禁用或删除 **/
	public static boolean isDisabled;
	public static String disabledType;
	/** 用户信息 **/
//	public static UserInfoBean userInfoBean;
	/** 美洽 Key **/
	public static String MCClientkey = "55a71a774eae35a217000004";
	/** 个推下发的 client_id **/
	public static String CLIENT_ID = "client_id";
	public static String client_id = "";
	/**  相册中图片对象集合 **/
	public static final String EXTRA_IMAGE_LIST = "image_list";
	/**  相册名称 **/
	public static final String EXTRA_BUCKET_NAME = "buck_name";
	/** 选择图片activity **/
//	public static ImageBucketChooseActivity IMAGEBUCKETCHOOSEACTIVITY;
	/**  当前选择的照片位置 **/
	public static final String CAMERA_PIC_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separatorChar + "Documentary"
			+ File.separatorChar + "CameraPic" + File.separatorChar;
	/** 保存着当前开启的所有activity **/
	public static List<Activity> runningActivities = new ArrayList<Activity>();
	/** 姓名 **/
	public static final int NAME_CODE = 1;
	/** 邮箱 **/
	public static final int EMAIL_CODE = 2;
	/** 性别类型 **/
	public static final int GENDER_CODE = 3;
}
