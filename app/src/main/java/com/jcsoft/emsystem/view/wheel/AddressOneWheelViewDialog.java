package com.jcsoft.emsystem.view.wheel;

import android.app.Activity;
import android.view.View;
import android.widget.Button;

import com.jcsoft.emsystem.R;
import com.jcsoft.emsystem.adapter.WheelProvinceAdapter;
import com.jcsoft.emsystem.base.LocationJson;

import java.util.List;


public class AddressOneWheelViewDialog extends AbsDialog implements View.OnClickListener {
	private Activity mContext;

	private WheelView wheelView_view1;
	private WheelProvinceAdapter adapter;
	private Button button_ok;
	private Button button_cancel;

    private List<LocationJson> provinceList;

    private int wheelProvinceIndex = 0;

	public AddressOneWheelViewDialog(Activity context) {
		super(context, R.style.dialog_menu);
		mContext = context;
		setContentView(R.layout.dialog_wheelview);
		setProperty(1,1);
	}
	
	@Override
	protected void initView() {
		wheelView_view1 = (WheelView) findViewById(R.id.wheelView_view1);
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


			}
		});

		button_ok.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (mAction != null && getProvinceList() != null) {
                    LocationJson bean1 = null;
					if(adapter!=null){
						bean1 = adapter.getBean(wheelView_view1.getCurrentItem());
                        wheelProvinceIndex = wheelView_view1.getCurrentItem();
					}

					mAction.doAction(bean1);
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
		public void doAction(LocationJson root);
	}

    public List<LocationJson> getProvinceList() {
        return provinceList;
    }

    public void setProvinceList(List<LocationJson> provinceList) {
        this.provinceList = provinceList;
    }

    public void setCurrentItem(int provinceIndex){
        wheelView_view1.setCurrentItem(provinceIndex);
        wheelProvinceIndex = provinceIndex;
    }
}
