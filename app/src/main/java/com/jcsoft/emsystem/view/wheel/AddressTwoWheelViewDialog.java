package com.jcsoft.emsystem.view.wheel;

import android.app.Activity;
import android.view.View;
import android.widget.Button;

import com.jcsoft.emsystem.R;
import com.jcsoft.emsystem.adapter.WheelProvinceAdapter;
import com.jcsoft.emsystem.base.LocationJson;
import com.jcsoft.emsystem.db.CityInfoDao;
import com.jcsoft.emsystem.utils.CommonUtils;

import java.util.List;


public class AddressTwoWheelViewDialog extends AbsDialog implements View.OnClickListener {
    private Activity mContext;

    private WheelView wheelView_view1;
    private WheelView wheelView_view2;
    private WheelProvinceAdapter adapter;
    private WheelProvinceAdapter adapter2;
    private Button button_ok;
    private Button button_cancel;

    private List<LocationJson> provinceList;

    private int wheelProvinceIndex = 0;
    private int wheelCityIndex = 0;

    public AddressTwoWheelViewDialog(Activity context) {
        super(context, R.style.dialog_menu);
        mContext = context;
        setContentView(R.layout.dialog_wheelview);
        setProperty(1, 1);
    }

    @Override
    protected void initView() {
        wheelView_view1 = (WheelView) findViewById(R.id.wheelView_view1);
        wheelView_view2 = (WheelView) findViewById(R.id.wheelView_view2);
        wheelView_view2.setVisibility(View.VISIBLE);
        button_cancel = (Button) findViewById(R.id.wheel_button_cancel);
        button_ok = (Button) findViewById(R.id.wheel_button_ok);
    }

    @Override
    protected void initData() {
        wheelView_view1.setScaleItem(true);
    }

    @Override
    protected void setListener() {
        wheelView_view1.addChangingListener(new OnWheelChangedListener() {
            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                if (oldValue != newValue) {
                    LocationJson locationJson = adapter.getBean(wheelView_view1.getCurrentItem());
                    CityInfoDao cityDao = new CityInfoDao(getContext());
                    List<LocationJson> stationList = cityDao.queryByProvinceId(locationJson.getId());
                    adapter2 = new WheelProvinceAdapter(mContext, stationList);
                    wheelView_view2.setViewAdapter(adapter2);
                    wheelView_view2.setCurrentItem(0);
                }

            }
        });

        button_ok.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mAction != null && getProvinceList() != null) {
                    LocationJson bean1 = null;
                    LocationJson bean2 = null;
                    if (adapter != null) {
                        bean1 = adapter.getBean(wheelView_view1.getCurrentItem());
                        wheelProvinceIndex = wheelView_view1.getCurrentItem();
                    }
                    if (adapter2 != null) {
                        bean2 = adapter2.getBean(wheelView_view2.getCurrentItem());
                        wheelCityIndex = wheelView_view2.getCurrentItem();
                    }
                    mAction.doAction(bean1, bean2);
                }
                dismiss();
            }
        });

        button_cancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    public void setData(List<LocationJson> beanList, String provinceName, String cityName) {
        setProvinceList(beanList);
        if (!CommonUtils.strIsEmpty(provinceName)) {
            wheelProvinceIndex = getWheelProvinceIndex(beanList, provinceName);
        }
        adapter = new WheelProvinceAdapter(mContext, beanList);
        wheelView_view1.setViewAdapter(adapter);
        wheelView_view1.setCurrentItem(wheelProvinceIndex);
        LocationJson areaBean = beanList.get(wheelView_view1.getCurrentItem());
        CityInfoDao cityDao = new CityInfoDao(getContext());
        List<LocationJson> stationList = cityDao.queryByProvinceId(areaBean.getId());
        if (!CommonUtils.strIsEmpty(cityName)) {
            wheelCityIndex = getWheelCityIndex(stationList, cityName);
        }
        adapter2 = new WheelProvinceAdapter(mContext, stationList);
        wheelView_view2.setViewAdapter(adapter2);
        wheelView_view2.setCurrentItem(wheelCityIndex);
    }

    private int getWheelProvinceIndex(List<LocationJson> beanList, String provinceName) {
        for (int i = 0; i < beanList.size(); i++) {
            LocationJson locationJson = beanList.get(i);
            if (locationJson.getName().equals(provinceName)) {
                return i;
            }
        }
        return 0;
    }


    private int getWheelCityIndex(List<LocationJson> stationList, String cityName) {
        for (int i = 0; i < stationList.size(); i++) {
            LocationJson locationJson = stationList.get(i);
            if (locationJson.getName().equals(cityName)) {
                return i;
            }
        }
        return 0;
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void show() {
        super.show();
    }

    public void show(ConfirmAction action) {
        setConfirmAction(action);
        show();
    }

    private ConfirmAction mAction;

    public void setConfirmAction(ConfirmAction action) {
        mAction = action;

    }

    public interface ConfirmAction {
        public void doAction(LocationJson root, LocationJson child);
    }

    public List<LocationJson> getProvinceList() {
        return provinceList;
    }

    public void setProvinceList(List<LocationJson> provinceList) {
        this.provinceList = provinceList;
    }
}
