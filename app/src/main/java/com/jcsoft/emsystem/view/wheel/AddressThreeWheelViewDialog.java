package com.jcsoft.emsystem.view.wheel;

import android.app.Activity;
import android.view.View;
import android.widget.Button;

import com.jcsoft.emsystem.R;
import com.jcsoft.emsystem.adapter.WheelProvinceAdapter;
import com.jcsoft.emsystem.base.LocationJson;
import com.jcsoft.emsystem.db.CityInfoDao;
import com.jcsoft.emsystem.db.DistrictInfoDao;

import java.util.List;


public class AddressThreeWheelViewDialog extends AbsDialog implements View.OnClickListener {
    private Activity mContext;

    private WheelView wheelView_view1;
    private WheelView wheelView_view2;
    private WheelView wheelView_view3;
    private WheelProvinceAdapter adapter;
    private WheelProvinceAdapter adapter2;
    private WheelProvinceAdapter adapter3;
    private Button button_ok;
    private Button button_cancel;

    private List<LocationJson> provinceList;

    private int wheelProvinceIndex = 0;
    private int wheelCityIndex = 0;
    private int wheelDistrictIndex = 0;

    public AddressThreeWheelViewDialog(Activity context) {
        super(context, R.style.dialog_menu);
        mContext = context;
        setContentView(R.layout.dialog_wheelview);
        setProperty(1, 1);
    }

    @Override
    protected void initView() {
        wheelView_view1 = (WheelView) findViewById(R.id.wheelView_view1);
        wheelView_view2 = (WheelView) findViewById(R.id.wheelView_view2);
        wheelView_view3 = (WheelView) findViewById(R.id.wheelView_view3);
        wheelView_view2.setVisibility(View.VISIBLE);
        wheelView_view3.setVisibility(View.VISIBLE);
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

                    DistrictInfoDao districtDao = new DistrictInfoDao(getContext());
                    List<LocationJson> districtList = districtDao.queryByCityId(stationList.get(wheelCityIndex).getId());
                    adapter3 = new WheelProvinceAdapter(mContext, districtList);
                    wheelView_view3.setViewAdapter(adapter3);
                    wheelView_view3.setCurrentItem(0);
                }

            }
        });
        wheelView_view2.addChangingListener(new OnWheelChangedListener() {
            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                if (oldValue != newValue) {
                    LocationJson cityJson = adapter2.getBean(wheelView_view2.getCurrentItem());
                    DistrictInfoDao districtDao = new DistrictInfoDao(getContext());
                    List<LocationJson> stationList = districtDao.queryByCityId(cityJson.getId());
                    adapter3 = new WheelProvinceAdapter(mContext, stationList);
                    wheelView_view3.setViewAdapter(adapter3);
                    wheelView_view3.setCurrentItem(0);
                }

            }
        });

        button_ok.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mAction != null && getProvinceList() != null) {
                    LocationJson bean1 = null;
                    LocationJson bean2 = null;
                    LocationJson bean3 = null;
                    if (adapter != null) {
                        bean1 = adapter.getBean(wheelView_view1.getCurrentItem());
                        wheelProvinceIndex = wheelView_view1.getCurrentItem();
                    }
                    if (adapter2 != null) {
                        bean2 = adapter2.getBean(wheelView_view2.getCurrentItem());
                        wheelCityIndex = wheelView_view2.getCurrentItem();
                    }
                    if (adapter3 != null) {
                        bean3 = adapter3.getBean(wheelView_view3.getCurrentItem());
                        wheelDistrictIndex = wheelView_view3.getCurrentItem();
                    }
                    mAction.doAction(bean1, bean2, bean3);
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

    public void setData(List<LocationJson> beanList) {
        setProvinceList(beanList);
        adapter = new WheelProvinceAdapter(mContext, beanList);
        wheelView_view1.setViewAdapter(adapter);
        wheelView_view1.setCurrentItem(wheelProvinceIndex);
        LocationJson areaBean = beanList.get(wheelView_view1.getCurrentItem());
        CityInfoDao cityDao = new CityInfoDao(getContext());
        List<LocationJson> stationList = cityDao.queryByProvinceId(areaBean.getId());
        adapter2 = new WheelProvinceAdapter(mContext, stationList);
        wheelView_view2.setViewAdapter(adapter2);
        wheelView_view2.setCurrentItem(wheelCityIndex);

        DistrictInfoDao districtDao = new DistrictInfoDao(getContext());
        List<LocationJson> districtList = districtDao.queryByCityId(stationList.get(wheelCityIndex).getId());
        adapter3 = new WheelProvinceAdapter(mContext, districtList);
        wheelView_view3.setViewAdapter(adapter3);
        wheelView_view3.setCurrentItem(wheelDistrictIndex);
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
        public void doAction(LocationJson root, LocationJson child, LocationJson child2);
    }

    public List<LocationJson> getProvinceList() {
        return provinceList;
    }

    public void setProvinceList(List<LocationJson> provinceList) {
        this.provinceList = provinceList;
    }
}
