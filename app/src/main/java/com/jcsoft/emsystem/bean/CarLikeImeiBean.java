package com.jcsoft.emsystem.bean;

import java.io.Serializable;

/**
 * Created by jimmy on 16/1/27.
 */
public class CarLikeImeiBean implements Serializable {

    private int carId;
    private long imei;

    public CarLikeImeiBean() {
    }

    public void setCarId(int carId) {
        this.carId = carId;
    }

    public void setImei(long imei) {
        this.imei = imei;
    }

    public int getCarId() {
        return carId;
    }

    public long getImei() {
        return imei;
    }
}
