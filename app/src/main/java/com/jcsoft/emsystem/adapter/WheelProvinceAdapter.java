package com.jcsoft.emsystem.adapter;

import android.content.Context;

import com.jcsoft.emsystem.R;
import com.jcsoft.emsystem.base.LocationJson;
import com.jcsoft.emsystem.view.wheel.AbstractWheelTextAdapter;

import java.util.ArrayList;
import java.util.List;

public class WheelProvinceAdapter extends AbstractWheelTextAdapter {
    private ArrayList<LocationJson> list;

    public WheelProvinceAdapter(Context context, List<LocationJson> areaList) {
        super(context, R.layout.layout_station_wheel_text);
        list = new ArrayList<LocationJson>();
        list.addAll(areaList);
        setItemTextResource(R.id.station_wheel_textView);
    }

    @Override
    public int getItemsCount() {
        return list.size();
    }

    @Override
    protected CharSequence getItemText(int index) {
        return list.get(index).getName();
    }

    public LocationJson getBean(int position) {
        if (position >= 0 && position < list.size() + 1) {
            return list.get(position);
        }
        return null;
    }

}
