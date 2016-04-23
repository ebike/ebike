package com.jcsoft.emsystem.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TimePicker;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jcsoft.emsystem.R;
import com.jcsoft.emsystem.base.BaseActivity;
import com.jcsoft.emsystem.base.SendParamsBean;
import com.jcsoft.emsystem.bean.CarInfoBean;
import com.jcsoft.emsystem.bean.ImageItem;
import com.jcsoft.emsystem.bean.ResponseBean;
import com.jcsoft.emsystem.callback.DCommonCallback;
import com.jcsoft.emsystem.callback.DSingleDialogCallback;
import com.jcsoft.emsystem.constants.AppConfig;
import com.jcsoft.emsystem.event.SelectPhotoEvent;
import com.jcsoft.emsystem.http.DHttpUtils;
import com.jcsoft.emsystem.http.DRequestParamsUtils;
import com.jcsoft.emsystem.http.HttpConstants;
import com.jcsoft.emsystem.utils.CommonUtils;
import com.jcsoft.emsystem.utils.DateUtil;
import com.jcsoft.emsystem.utils.ImageCompress;
import com.jcsoft.emsystem.view.ActionSheetDialog;
import com.jcsoft.emsystem.view.RowLabelValueView;

import org.xutils.http.RequestParams;
import org.xutils.image.ImageOptions;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 车辆资料
 */
public class CarInformationActivity extends BaseActivity implements View.OnClickListener {
    @ViewInject(R.id.rlvv_brand)
    RowLabelValueView brandView;
    @ViewInject(R.id.rlvv_models)
    RowLabelValueView modelsView;
    @ViewInject(R.id.rlvv_motor_number)
    RowLabelValueView motorNumberView;
    @ViewInject(R.id.rlvv_frame_number)
    RowLabelValueView frameNumberView;
    @ViewInject(R.id.rlvv_remote_control)
    RowLabelValueView remoteControlView;
    @ViewInject(R.id.rlvv_voltage)
    RowLabelValueView voltageView;
    @ViewInject(R.id.rlvv_buy_date)
    RowLabelValueView buyDateView;
    @ViewInject(R.id.rlvv_buy_price)
    RowLabelValueView buyPriceView;
    @ViewInject(R.id.rlvv_car_photo)
    RowLabelValueView carPhotoValueView;
    @ViewInject(R.id.iv_car_photo)
    ImageView carPhotoView;

    LinearLayout remoteLockLayout;
    ImageView remoteLockView;
    LinearLayout voiceSeachLayout;
    ImageView voiceSeachView;
    LinearLayout oneKeyStartLayout;
    ImageView oneKeyStartView;

    LinearLayout v48Layout;
    ImageView v48View;
    LinearLayout v60Layout;
    ImageView v60View;
    LinearLayout v72Layout;
    ImageView v72View;
    EditText customVoltageText;

    //车辆信息
    private CarInfoBean carInfoBean;
    private ImageCompress compress;
    //拍照时间
    private long takePhotoTime;

    private View remoteControlDialogView;
    private Integer remoteControlTempValue;
    private Integer remoteControlValue;
    private View voltageDialogView;
    private Integer voltageTempValue;
    private Integer voltageValue;

    @Override
    public void loadXml() {
        setContentView(R.layout.activity_car_information);
        x.view().inject(this);
    }

    @Override
    public void getIntentData(Bundle savedInstanceState) {

    }

    @Override
    public void init() {
        compress = new ImageCompress();
        if (carInfoBean != null) {
            brandView.setValue(carInfoBean.getCarBrand());
            modelsView.setValue(carInfoBean.getCarModel());
            motorNumberView.setValue(carInfoBean.getMotorNum());
            frameNumberView.setValue(carInfoBean.getFrameNum());
            if (!CommonUtils.strIsEmpty(carInfoBean.getControlType())) {
                remoteControlValue = Integer.valueOf(carInfoBean.getControlType());
                if (carInfoBean.getControlType().equals("1")) {
                    remoteControlView.setValue("远程锁车");
                } else if (carInfoBean.getControlType().equals("2")) {
                    remoteControlView.setValue("语音寻车");
                }
                if (carInfoBean.getControlType().equals("3")) {
                    remoteControlView.setValue("一键启动");
                }
            }
            if (!CommonUtils.strIsEmpty(carInfoBean.getVoltage())) {
                voltageValue = Integer.valueOf(carInfoBean.getVoltage());
                voltageView.setValue(carInfoBean.getVoltage() + "V");
            }
            buyDateView.setValue(carInfoBean.getCarDate());
            buyPriceView.setValue("￥" + carInfoBean.getCarPrice());
            ImageOptions imageOptions = new ImageOptions.Builder()
                    .setLoadingDrawableId(R.mipmap.icon_default_ebike)
                    .setFailureDrawableId(R.mipmap.icon_default_ebike)
                    .setImageScaleType(ImageView.ScaleType.CENTER_CROP)
                    .build();
            x.image().bind(carPhotoView, carInfoBean.getCarPic(), imageOptions);
        }
    }

    @Override
    public void setListener() {
        brandView.setOnClickListener(this);
        modelsView.setOnClickListener(this);
        motorNumberView.setOnClickListener(this);
        frameNumberView.setOnClickListener(this);
        remoteControlView.setOnClickListener(this);
        voltageView.setOnClickListener(this);
        buyDateView.setOnClickListener(this);
        buyPriceView.setOnClickListener(this);
        carPhotoValueView.setOnClickListener(this);
    }

    @Override
    public void setData() {
        //获取车辆基本信息
        RequestParams params = DRequestParamsUtils.getRequestParams_Header(HttpConstants.getCarInfoUrl());
        DHttpUtils.get_String(this, true, params, new DCommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                ResponseBean<CarInfoBean> bean = new Gson().fromJson(result, new TypeToken<ResponseBean<CarInfoBean>>() {
                }.getType());
                if (bean.getCode() == 1) {
                    carInfoBean = bean.getData();
                    //更新界面
                    init();
                } else {
                    showShortText(bean.getErrmsg());
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.rlvv_brand:
                intent = new Intent(this, UpdateTextValueActivity.class);
                intent.putExtra("type", 2);
                intent.putExtra("fieldName_CH", getString(R.string.car_information_brand));
                intent.putExtra("fieldValue", carInfoBean.getCarBrand());
                intent.putExtra("fieldName", "carBrand");
                startActivity(intent);
                break;
            case R.id.rlvv_models:
                intent = new Intent(this, UpdateTextValueActivity.class);
                intent.putExtra("type", 2);
                intent.putExtra("fieldName_CH", getString(R.string.car_information_models));
                intent.putExtra("fieldValue", carInfoBean.getCarModel());
                intent.putExtra("fieldName", "carModel");
                startActivity(intent);
                break;
            case R.id.rlvv_motor_number:
                intent = new Intent(this, UpdateTextValueActivity.class);
                intent.putExtra("type", 2);
                intent.putExtra("fieldName_CH", getString(R.string.car_information_motor_number));
                intent.putExtra("fieldValue", carInfoBean.getMotorNum());
                intent.putExtra("fieldName", "motorNum");
                startActivity(intent);
                break;
            case R.id.rlvv_frame_number:
                intent = new Intent(this, UpdateTextValueActivity.class);
                intent.putExtra("type", 2);
                intent.putExtra("fieldName_CH", getString(R.string.car_information_frame_number));
                intent.putExtra("fieldValue", carInfoBean.getFrameNum());
                intent.putExtra("fieldName", "frameNum");
                startActivity(intent);
                break;
            case R.id.rlvv_remote_control:
                initRemoteControlDialogView(remoteControlValue);
                CommonUtils.showCustomDialog1(this, "选择远程控制功能", remoteControlDialogView, new DSingleDialogCallback() {
                    @Override
                    public void onPositiveButtonClick(String editText) {
                        if (remoteControlTempValue != null) {
                            remoteControlValue = remoteControlTempValue;
                            if (remoteControlValue == 1) {
                                remoteControlView.setValue("远程锁车");
                            } else if (remoteControlValue == 2) {
                                remoteControlView.setValue("语音寻车");
                            } else if (remoteControlValue == 3) {
                                remoteControlView.setValue("一键启动");
                            }
                            update("controlType", remoteControlValue + "");
                        }
                    }
                });
                break;
            case R.id.rlvv_voltage:
                initVoltageDialogView(voltageValue);
                CommonUtils.showCustomDialog1(this, "选择电动车电压", voltageDialogView, new DSingleDialogCallback() {
                    @Override
                    public void onPositiveButtonClick(String editText) {
                        if (voltageTempValue != null) {
                            voltageValue = voltageTempValue;
                            voltageView.setValue(voltageValue + "V");
                            update("voltage", voltageValue + "");
                        }
                    }
                });
                break;
            case R.id.rlvv_buy_date:
                try {
                    showDataCardelar();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.rlvv_buy_price:
                intent = new Intent(this, UpdateTextValueActivity.class);
                intent.putExtra("type", 2);
                intent.putExtra("fieldName_CH", getString(R.string.car_information_buy_price));
                intent.putExtra("fieldValue", carInfoBean.getCarPrice() + "");
                intent.putExtra("fieldName", "carPrice");
                startActivity(intent);
                break;
            case R.id.rlvv_car_photo:
                takePhotoTime = System.currentTimeMillis();
                showPhotoDialog(CarInformationActivity.this, takePhotoTime);
                break;
            case R.id.ll_remote_lock:
                remoteControlTempValue = 1;
                remoteLockView.setVisibility(View.VISIBLE);
                voiceSeachView.setVisibility(View.GONE);
                oneKeyStartView.setVisibility(View.GONE);
                break;
            case R.id.ll_voice_seach:
                remoteControlTempValue = 2;
                remoteLockView.setVisibility(View.GONE);
                voiceSeachView.setVisibility(View.VISIBLE);
                oneKeyStartView.setVisibility(View.GONE);
                break;
            case R.id.ll_one_key_start:
                remoteControlTempValue = 3;
                remoteLockView.setVisibility(View.GONE);
                voiceSeachView.setVisibility(View.GONE);
                oneKeyStartView.setVisibility(View.VISIBLE);
                break;
            case R.id.ll_48v:
                voltageTempValue = 48;
                v48View.setVisibility(View.VISIBLE);
                v60View.setVisibility(View.GONE);
                v72View.setVisibility(View.GONE);
                customVoltageText.setText("");
                break;
            case R.id.ll_60v:
                voltageTempValue = 60;
                v48View.setVisibility(View.GONE);
                v60View.setVisibility(View.VISIBLE);
                v72View.setVisibility(View.GONE);
                customVoltageText.setText("");
                break;
            case R.id.ll_72v:
                voltageTempValue = 72;
                v48View.setVisibility(View.GONE);
                v60View.setVisibility(View.GONE);
                v72View.setVisibility(View.VISIBLE);
                customVoltageText.setText("");
                break;
        }
    }

    private View initRemoteControlDialogView(Integer remoteControlValue) {
        remoteControlDialogView = LayoutInflater.from(this).inflate(R.layout.view_remote_control, null);
        remoteLockLayout = (LinearLayout) remoteControlDialogView.findViewById(R.id.ll_remote_lock);
        voiceSeachLayout = (LinearLayout) remoteControlDialogView.findViewById(R.id.ll_voice_seach);
        oneKeyStartLayout = (LinearLayout) remoteControlDialogView.findViewById(R.id.ll_one_key_start);
        remoteLockView = (ImageView) remoteControlDialogView.findViewById(R.id.iv_remote_lock);
        voiceSeachView = (ImageView) remoteControlDialogView.findViewById(R.id.iv_voice_seach);
        oneKeyStartView = (ImageView) remoteControlDialogView.findViewById(R.id.iv_one_key_start);

        if (remoteControlValue != null) {
            if (remoteControlValue == 1) {
                remoteLockView.setVisibility(View.VISIBLE);
                voiceSeachView.setVisibility(View.GONE);
                oneKeyStartView.setVisibility(View.GONE);
            } else if (remoteControlValue == 2) {
                remoteLockView.setVisibility(View.GONE);
                voiceSeachView.setVisibility(View.VISIBLE);
                oneKeyStartView.setVisibility(View.GONE);
            } else if (remoteControlValue == 3) {
                remoteLockView.setVisibility(View.GONE);
                voiceSeachView.setVisibility(View.GONE);
                oneKeyStartView.setVisibility(View.VISIBLE);
            }
        }

        remoteLockLayout.setOnClickListener(this);
        voiceSeachLayout.setOnClickListener(this);
        oneKeyStartLayout.setOnClickListener(this);

        return remoteControlDialogView;
    }

    private View initVoltageDialogView(Integer voltageValue) {
        voltageDialogView = LayoutInflater.from(this).inflate(R.layout.view_voltage, null);
        v48Layout = (LinearLayout) voltageDialogView.findViewById(R.id.ll_48v);
        v60Layout = (LinearLayout) voltageDialogView.findViewById(R.id.ll_60v);
        v72Layout = (LinearLayout) voltageDialogView.findViewById(R.id.ll_72v);
        v48View = (ImageView) voltageDialogView.findViewById(R.id.iv_48v);
        v60View = (ImageView) voltageDialogView.findViewById(R.id.iv_60v);
        v72View = (ImageView) voltageDialogView.findViewById(R.id.iv_72v);
        customVoltageText = (EditText) voltageDialogView.findViewById(R.id.et_custom_voltage);

        if (voltageValue != null) {
            if (voltageValue == 48) {
                v48View.setVisibility(View.VISIBLE);
                v60View.setVisibility(View.GONE);
                v72View.setVisibility(View.GONE);
                customVoltageText.setText("");
            } else if (voltageValue == 60) {
                v48View.setVisibility(View.GONE);
                v60View.setVisibility(View.VISIBLE);
                v72View.setVisibility(View.GONE);
                customVoltageText.setText("");
            } else if (voltageValue == 72) {
                v48View.setVisibility(View.GONE);
                v60View.setVisibility(View.GONE);
                v72View.setVisibility(View.VISIBLE);
                customVoltageText.setText("");
            } else {
                customVoltageText.setText(voltageValue);
            }
        }

        v48Layout.setOnClickListener(this);
        v60Layout.setOnClickListener(this);
        v72Layout.setOnClickListener(this);
        customVoltageText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String value = customVoltageText.getText().toString().trim();
                if (!CommonUtils.strIsEmpty(value)) {
                    voltageTempValue = Integer.valueOf(value);
                    v48View.setVisibility(View.GONE);
                    v60View.setVisibility(View.GONE);
                    v72View.setVisibility(View.GONE);
                }
            }
        });

        return voltageDialogView;
    }

    public static void showPhotoDialog(final Activity activity, final long takePhotoTime) {
        new ActionSheetDialog(activity)
                .builder()
                .setCancelable(true)
                .setCanceledOnTouchOutside(true)
                .addSheetItem("拍照", ActionSheetDialog.SheetItemColor.Blue,
                        new ActionSheetDialog.OnSheetItemClickListener() {
                            @Override
                            public void onClick(int which) {
                                File file = new File(AppConfig.CAMERA_PIC_PATH);
                                if (!file.exists()) {
                                    file.mkdirs();
                                }
                                File file2 = new File(AppConfig.CAMERA_PIC_PATH, takePhotoTime + ".jpg");
                                try {
                                    file2.createNewFile();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file2));
                                activity.startActivityForResult(intent, 2);
                            }
                        })
                .addSheetItem("从手机相册选择", ActionSheetDialog.SheetItemColor.Blue,
                        new ActionSheetDialog.OnSheetItemClickListener() {
                            @Override
                            public void onClick(int which) {
                                Intent intent = new Intent(activity, PhotoAlbumListActivity.class);
                                activity.startActivity(intent);
                            }
                        }).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //判断请求码
        switch (requestCode) {
            case 2://拍照
                //设置文件保存路径这里放在跟目录下
                File mFile = new File(AppConfig.CAMERA_PIC_PATH + takePhotoTime + ".jpg");
                if (mFile.length() != 0) {
                    ImageItem item = new ImageItem();
                    item.imageId = takePhotoTime + "";
                    item.picName = takePhotoTime + ".jpg";
                    item.size = String.valueOf(mFile.length());
                    item.sourcePath = AppConfig.CAMERA_PIC_PATH + takePhotoTime + ".jpg";
                    uploadImage(item);
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    //根据当前操作的照片进行赋值
    private void uploadImage(final ImageItem imageItem) {
        //由于目前没有查看图片，每次选择图片都是覆盖更新，所以，只用到路径字段，其他字段预留
        if (imageItem != null && !CommonUtils.strIsEmpty(imageItem.sourcePath)) {
            //对图片做压缩处理
            Bitmap bitmap = compress.getimage(imageItem.sourcePath);
            if (null != bitmap) {
                try {
                    compress.compressAndGenImage(bitmap, imageItem.sourcePath, AppConfig.compressedImage + imageItem.picName, 100);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            //压缩后的图片文件
            File file = new File(AppConfig.compressedImage + imageItem.picName);
            List<SendParamsBean> sendParamsBeans = new ArrayList<SendParamsBean>();
            sendParamsBeans.add(new SendParamsBean("carId", AppConfig.userInfoBean.getCarId() + "", false));
            sendParamsBeans.add(new SendParamsBean("carPic", file, true));
            RequestParams params = DRequestParamsUtils.getRequestParamsHasFile_Header(HttpConstants.getUpdateCarUrl(), sendParamsBeans);
            DHttpUtils.post_String(this, true, params, new DCommonCallback<String>() {
                @Override
                public void onSuccess(String result) {
                    ResponseBean<CarInfoBean> bean = new Gson().fromJson(result, new TypeToken<ResponseBean<CarInfoBean>>() {
                    }.getType());
                    if (bean.getCode() == 1) {
                        carInfoBean = bean.getData();
                        init();
                    } else {
                        showShortText(bean.getErrmsg());
                    }
                }
            });
        }
    }

    //从相册选择
    public void onEvent(SelectPhotoEvent event) {
        if (event != null && event.getItem() != null) {
            uploadImage(event.getItem());
        }
    }

    public void onEvent(CarInfoBean car) {
        if (car != null) {
            carInfoBean = car;
            init();
        }
    }

    private int mYear;
    private int mMonth;
    private int mDay;
    private String mDate;
    private Date mDatetime;
    private Date nowDate;

    private void showDataCardelar() throws ParseException {
        String buyDate = carInfoBean.getCarDate();
        SimpleDateFormat sdft1 = new SimpleDateFormat("yyyy-MM-dd");
        if (!CommonUtils.strIsEmpty(buyDate) && !buyDate.equals("必选")) {
            mYear = Integer.parseInt(buyDate.substring(0, 4));
            mMonth = Integer.parseInt(buyDate.substring(5, 7)) - 1;
            mDay = Integer.parseInt(buyDate.substring(8, 10));
            mDate = buyDate;
        } else {
            Calendar calendar = Calendar.getInstance();
            Calendar mCalendar = new GregorianCalendar();
            nowDate = calendar.getTime();
            if (mDatetime == null) {
                mYear = calendar.get(Calendar.YEAR);
                mMonth = calendar.get(Calendar.MONTH);
                mDay = calendar.get(Calendar.DAY_OF_MONTH);
            } else {
                mCalendar.setTime(mDatetime);
                mYear = mCalendar.get(Calendar.YEAR);
                mMonth = mCalendar.get(Calendar.MONTH);
                mDay = mCalendar.get(Calendar.DAY_OF_MONTH);
            }
            mDate = sdft1.format(nowDate);
        }
        View mTargetView = LayoutInflater.from(this).inflate(R.layout.view_picker_date_time, null);
        DatePicker mDatePicker = (DatePicker) mTargetView.findViewById(R.id.datePicker);
        TimePicker mTimePicker = (TimePicker) mTargetView.findViewById(R.id.timePicker);
        mTimePicker.setVisibility(View.GONE);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("请选择购买日期");
        builder.setView(mTargetView);
        mDatePicker.init(mYear, mMonth, mDay, datePickerChangeListener);
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                buyDateView.setValue(mDate);
                update("carDate", DateUtil.changeDateFormat(mDate));
            }
        });
        builder.show();
    }

    private DatePicker.OnDateChangedListener datePickerChangeListener = new DatePicker.OnDateChangedListener() {
        @Override
        public void onDateChanged(DatePicker datePicker, int year, int month, int day) {
            mDate = new StringBuilder().append(year).append("年").append(CommonUtils.pad(month + 1)).append("月").append(CommonUtils.pad(day)).append("日").toString();
        }
    };

    private void update(String key, String value) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("carId", AppConfig.userInfoBean.getCarId() + "");
        map.put(key, value);
        RequestParams params = DRequestParamsUtils.getRequestParams_Header(HttpConstants.getUpdateCarUrl(), map);
        DHttpUtils.post_String(this, true, params, new DCommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                ResponseBean<CarInfoBean> bean = new Gson().fromJson(result, new TypeToken<ResponseBean<CarInfoBean>>() {
                }.getType());
                if (bean.getCode() == 1) {
                    carInfoBean = bean.getData();
                    init();
                } else {
                    showShortText(bean.getErrmsg());
                }
            }
        });
    }
}
