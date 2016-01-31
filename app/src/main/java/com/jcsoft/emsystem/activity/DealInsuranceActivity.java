package com.jcsoft.emsystem.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TimePicker;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jcsoft.emsystem.R;
import com.jcsoft.emsystem.base.BaseActivity;
import com.jcsoft.emsystem.base.LocationJson;
import com.jcsoft.emsystem.base.SendParamsBean;
import com.jcsoft.emsystem.bean.CarLikeImeiBean;
import com.jcsoft.emsystem.bean.ImageItem;
import com.jcsoft.emsystem.bean.ResponseBean;
import com.jcsoft.emsystem.callback.DCommonCallback;
import com.jcsoft.emsystem.callback.DSingleDialogCallback;
import com.jcsoft.emsystem.constants.AppConfig;
import com.jcsoft.emsystem.db.ProvinceInfoDao;
import com.jcsoft.emsystem.event.SelectPhotoEvent;
import com.jcsoft.emsystem.http.DHttpUtils;
import com.jcsoft.emsystem.http.DRequestParamsUtils;
import com.jcsoft.emsystem.http.HttpConstants;
import com.jcsoft.emsystem.utils.CommonUtils;
import com.jcsoft.emsystem.utils.ImageCompress;
import com.jcsoft.emsystem.view.ActionSheetDialog;
import com.jcsoft.emsystem.view.RowLabelEditView;
import com.jcsoft.emsystem.view.RowLabelValueView;
import com.jcsoft.emsystem.view.TopBarView;
import com.jcsoft.emsystem.view.wheel.AddressTwoWheelViewDialog;

import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * 办理保险
 */
public class DealInsuranceActivity extends BaseActivity implements RowLabelValueView.OnClickCallback {
    @ViewInject(R.id.top_bar)
    TopBarView topBarView;
    @ViewInject(R.id.rlvv_car_photo)
    RowLabelValueView carPhotoRowLabelValueView;
    @ViewInject(R.id.rlvv_id_card_positive)
    RowLabelValueView idCardPositiveRowLabelValueView;
    @ViewInject(R.id.rlvv_id_card_negative)
    RowLabelValueView idCardNegativeRowLabelValueView;
    @ViewInject(R.id.rlvv_certificate_photos)
    RowLabelValueView certificatePhotosRowLabelValueView;
    @ViewInject(R.id.rlvv_invoice_or_receipt)
    RowLabelValueView invoiceOrReceiptRowLabelValueView;
    @ViewInject(R.id.rlev_motor_number)
    RowLabelEditView motorNumberRowLabelValueView;
    @ViewInject(R.id.rlev_frame_number)
    RowLabelEditView frameNumberRowLabelValueView;
    @ViewInject(R.id.rlvv_coverage_area)
    RowLabelValueView coverageAreaRowLabelValueView;
    @ViewInject(R.id.rlev_car_brand)
    RowLabelEditView carBrandRowLabelValueView;
    @ViewInject(R.id.rlev_car_model)
    RowLabelEditView carModelRowLabelValueView;
    @ViewInject(R.id.rlvv_buy_date)
    RowLabelValueView buyDateRowLabelValueView;
    @ViewInject(R.id.rlev_buy_price)
    RowLabelEditView buyPriceRowLabelValueView;
    private AddressTwoWheelViewDialog dialog;
    private ProvinceInfoDao provinceDao;
    private List<LocationJson> mProvinceList;
    private int provinceId;
    private int cityId;
    private String provinceName;
    private String cityName;
    //拍照时间
    private long takePhotoTime;
    //当前操作的照片（当前操作照片的ID）
    private int photoType;
    //车辆照片
    private File carPic;
    //身份证正面
    private File idPic;
    //身份证反面
    private File idbPic;
    //合格证照片
    private File cerPic;
    //发票或收据
    private File billPic;
    //图片压缩类
    private ImageCompress compress;


    @Override
    public void loadXml() {
        setContentView(R.layout.activity_deal_insurance);
        x.view().inject(this);
    }

    @Override
    public void getIntentData(Bundle savedInstanceState) {

    }

    @Override
    public void init() {
        buyPriceRowLabelValueView.setEditInteger();
        if (AppConfig.userInfoBean != null) {
            provinceName = AppConfig.userInfoBean.getProvince();
            cityName = AppConfig.userInfoBean.getCity();
            coverageAreaRowLabelValueView.setValue(provinceName + " " + cityName);
        }
        dialog = new AddressTwoWheelViewDialog(this);
        provinceDao = new ProvinceInfoDao(this);
        mProvinceList = provinceDao.queryAll();
    }

    @Override
    public void setListener() {
        carPhotoRowLabelValueView.setOnClickCallback(this);
        idCardPositiveRowLabelValueView.setOnClickCallback(this);
        idCardNegativeRowLabelValueView.setOnClickCallback(this);
        certificatePhotosRowLabelValueView.setOnClickCallback(this);
        invoiceOrReceiptRowLabelValueView.setOnClickCallback(this);
        buyDateRowLabelValueView.setOnClickCallback(this);
        coverageAreaRowLabelValueView.setOnClickCallback(this);
        topBarView.setRightCallback(new TopBarView.TopBarRightCallback() {
            @Override
            public void setRightOnClickListener() {
                //验证必填
                if (carPic == null) {
                    carPhotoRowLabelValueView.setValue("请选择照片");
                    carPhotoRowLabelValueView.setValueColor(R.color.orange_dark);
                    return;
                }
                if (idPic == null) {
                    idCardPositiveRowLabelValueView.setValue("请选择照片");
                    idCardPositiveRowLabelValueView.setValueColor(R.color.orange_dark);
                    return;
                }
                if (idbPic == null) {
                    idCardNegativeRowLabelValueView.setValue("请选择照片");
                    idCardNegativeRowLabelValueView.setValueColor(R.color.orange_dark);
                    return;
                }
                if (cerPic == null) {
                    certificatePhotosRowLabelValueView.setValue("请选择照片");
                    certificatePhotosRowLabelValueView.setValueColor(R.color.orange_dark);
                    return;
                }
                if (billPic == null) {
                    invoiceOrReceiptRowLabelValueView.setValue("请选择照片");
                    invoiceOrReceiptRowLabelValueView.setValueColor(R.color.orange_dark);
                    return;
                }
                if (CommonUtils.strIsEmpty(motorNumberRowLabelValueView.getValue())) {
                    motorNumberRowLabelValueView.setHint(R.string.app_require_input);
                    motorNumberRowLabelValueView.setHintColor(R.color.orange_dark);
                    return;
                }
                if (CommonUtils.strIsEmpty(frameNumberRowLabelValueView.getValue())) {
                    frameNumberRowLabelValueView.setHint(R.string.app_require_input);
                    frameNumberRowLabelValueView.setHintColor(R.color.orange_dark);
                    return;
                }
                if (CommonUtils.strIsEmpty(provinceName)) {
                    coverageAreaRowLabelValueView.setValueColor(R.color.orange_dark);
                    return;
                }
                if (CommonUtils.strIsEmpty(carBrandRowLabelValueView.getValue())) {
                    carBrandRowLabelValueView.setHint(R.string.app_require_input);
                    carBrandRowLabelValueView.setHintColor(R.color.orange_dark);
                    return;
                }
                if (CommonUtils.strIsEmpty(carModelRowLabelValueView.getValue())) {
                    carModelRowLabelValueView.setHint(R.string.app_require_input);
                    carModelRowLabelValueView.setHintColor(R.color.orange_dark);
                    return;
                }
                if (CommonUtils.strIsEmpty(buyDateRowLabelValueView.getValue()) || buyDateRowLabelValueView.getValue().equals("必选")) {
                    buyDateRowLabelValueView.setValueColor(R.color.orange_dark);
                    return;
                }
                if (CommonUtils.strIsEmpty(buyPriceRowLabelValueView.getValue())) {
                    buyPriceRowLabelValueView.setHint(R.string.app_require_input);
                    buyPriceRowLabelValueView.setHintColor(R.color.orange_dark);
                    return;
                }
                //整理参数
                List<SendParamsBean> sendParamsBeans = new ArrayList<SendParamsBean>();
                sendParamsBeans.add(new SendParamsBean("carId", AppConfig.userInfoBean.getCarId() + "", false));
                sendParamsBeans.add(new SendParamsBean("motorNum", motorNumberRowLabelValueView.getValue(), false));
                sendParamsBeans.add(new SendParamsBean("frameNum", frameNumberRowLabelValueView.getValue(), false));
                sendParamsBeans.add(new SendParamsBean("carBrand", carBrandRowLabelValueView.getValue(), false));
                sendParamsBeans.add(new SendParamsBean("carModel", carModelRowLabelValueView.getValue(), false));
                sendParamsBeans.add(new SendParamsBean("carDate", buyDateRowLabelValueView.getValue(), false));
                sendParamsBeans.add(new SendParamsBean("carPrice", buyPriceRowLabelValueView.getValue(), false));
                sendParamsBeans.add(new SendParamsBean("idPic", idPic, true));
                sendParamsBeans.add(new SendParamsBean("idbPic", idbPic, true));
                sendParamsBeans.add(new SendParamsBean("carPic", carPic, true));
                sendParamsBeans.add(new SendParamsBean("billPic", billPic, true));
                sendParamsBeans.add(new SendParamsBean("cerPic", cerPic, true));
                sendParamsBeans.add(new SendParamsBean("underwritePro", provinceName, false));
                sendParamsBeans.add(new SendParamsBean("underwriteCity", cityName, false));
                //提交
                RequestParams params = DRequestParamsUtils.getRequestParamsHasFile(HttpConstants.uploadInsurInfoUrl(), sendParamsBeans);
                DHttpUtils.post_String(DealInsuranceActivity.this, true, params, new DCommonCallback<String>() {
                    @Override
                    public void onSuccess(String result) {
                        ResponseBean<Object> bean = new Gson().fromJson(result, new TypeToken<ResponseBean<CarLikeImeiBean>>() {
                        }.getType());
                        if (bean.getCode() == 1) {
                            CommonUtils.showCustomDialogSignle2(DealInsuranceActivity.this, "", bean.getErrmsg(), new DSingleDialogCallback() {
                                @Override
                                public void onPositiveButtonClick(String editText) {
                                    DealInsuranceActivity.this.finish();
                                }
                            });
                        } else {
                            showShortText(bean.getErrmsg());
                        }
                    }
                });

            }
        });
    }

    @Override
    public void setData() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rlvv_car_photo://车辆照片
                photoType = R.id.rlvv_car_photo;
                takePhotoTime = System.currentTimeMillis();
                showPhotoDialog(DealInsuranceActivity.this, takePhotoTime);
                break;
            case R.id.rlvv_id_card_positive://身份证正面
                photoType = R.id.rlvv_id_card_positive;
                takePhotoTime = System.currentTimeMillis();
                showPhotoDialog(DealInsuranceActivity.this, takePhotoTime);
                break;
            case R.id.rlvv_id_card_negative://身份证反面
                photoType = R.id.rlvv_id_card_negative;
                takePhotoTime = System.currentTimeMillis();
                showPhotoDialog(DealInsuranceActivity.this, takePhotoTime);
                break;
            case R.id.rlvv_certificate_photos://合格证照片
                photoType = R.id.rlvv_certificate_photos;
                takePhotoTime = System.currentTimeMillis();
                showPhotoDialog(DealInsuranceActivity.this, takePhotoTime);
                break;
            case R.id.rlvv_invoice_or_receipt://发票或收据
                photoType = R.id.rlvv_invoice_or_receipt;
                takePhotoTime = System.currentTimeMillis();
                showPhotoDialog(DealInsuranceActivity.this, takePhotoTime);
                break;
            case R.id.rlvv_buy_date://购买日期
                showDataCardelar();
                break;
            case R.id.rlvv_coverage_area://承保区域
                dialog.setData(mProvinceList);
                dialog.show(new AddressTwoWheelViewDialog.ConfirmAction() {
                    @Override
                    public void doAction(LocationJson root, LocationJson child) {
                        coverageAreaRowLabelValueView.setValue(root.getName() + " " + child.getName());
                        coverageAreaRowLabelValueView.setValueColor(R.color.gray_6);
                        provinceId = root.getId();
                        provinceName = root.getName();
                        cityId = child.getId();
                        cityName = child.getName();
                    }
                });
                break;
            default:
                break;
        }
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
                    setPhotoByType(item);
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    //从相册选择
    public void onEvent(SelectPhotoEvent event) {
        if (event != null && event.getItem() != null) {
            setPhotoByType(event.getItem());
        }
    }

    //根据当前操作的照片进行赋值
    private void setPhotoByType(ImageItem imageItem) {
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
            switch (photoType) {
                case R.id.rlvv_car_photo://车辆照片
                    carPic = file;
                    carPhotoRowLabelValueView.setValue("已选中照片");
                    carPhotoRowLabelValueView.setValueColor(R.color.orange_dark);
                    break;
                case R.id.rlvv_id_card_positive://身份证正面
                    idPic = file;
                    idCardPositiveRowLabelValueView.setValue("已选中照片");
                    idCardPositiveRowLabelValueView.setValueColor(R.color.orange_dark);
                    break;
                case R.id.rlvv_id_card_negative://身份证反面
                    idbPic = file;
                    idCardNegativeRowLabelValueView.setValue("已选中照片");
                    idCardNegativeRowLabelValueView.setValueColor(R.color.orange_dark);
                    break;
                case R.id.rlvv_certificate_photos://合格证照片
                    cerPic = file;
                    certificatePhotosRowLabelValueView.setValue("已选中照片");
                    certificatePhotosRowLabelValueView.setValueColor(R.color.orange_dark);
                    break;
                case R.id.rlvv_invoice_or_receipt://发票或收据
                    billPic = file;
                    invoiceOrReceiptRowLabelValueView.setValue("已选中照片");
                    invoiceOrReceiptRowLabelValueView.setValueColor(R.color.orange_dark);
                    break;
                default:
                    break;
            }
        }
    }

    private int mYear;
    private int mMonth;
    private int mDay;
    private String mDate;
    private Date mDatetime;
    private Date nowDate;

    private void showDataCardelar() {
        String buyDate = buyDateRowLabelValueView.getValue();
        SimpleDateFormat sdft1 = new SimpleDateFormat("yyyy-MM-dd");
        if (!CommonUtils.strIsEmpty(buyDate) && !buyDate.equals("必选")) {
            String[] sub = buyDate.split("-");
            mYear = Integer.parseInt(sub[0]);
            mMonth = Integer.parseInt(sub[1]) - 1;
            mDay = Integer.parseInt(sub[2]);
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
        builder.setTitle("设置购买日期");
        builder.setView(mTargetView);
        mDatePicker.init(mYear, mMonth, mDay, datePickerChangeListener);
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.setPositiveButton("完成", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                buyDateRowLabelValueView.setValue(mDate);
            }
        });
        builder.show();
    }

    private DatePicker.OnDateChangedListener datePickerChangeListener = new DatePicker.OnDateChangedListener() {
        @Override
        public void onDateChanged(DatePicker datePicker, int year, int month, int day) {
            mDate = new StringBuilder().append(year).append("-").append(CommonUtils.pad(month + 1)).append("-").append(CommonUtils.pad(day)).toString();
        }
    };
}
