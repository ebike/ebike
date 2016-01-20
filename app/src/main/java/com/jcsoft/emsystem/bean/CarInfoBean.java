package com.jcsoft.emsystem.bean;

import java.io.Serializable;

/**
 * 车辆信息
 * Created by jimmy on 16/1/10.
 */
public class CarInfoBean implements Serializable{

    private int carId;
    private String carBrand;
    private String carModel;
    private String carType;
    private String carPic;
    private String frameNum;
    private String motorNum;
    private int carPrice;
    private String carDate;

    public CarInfoBean() {
    }

    public void setCarId(int carId) {
        this.carId = carId;
    }

    public void setCarBrand(String carBrand) {
        this.carBrand = carBrand;
    }

    public void setCarModel(String carModel) {
        this.carModel = carModel;
    }

    public void setCarType(String carType) {
        this.carType = carType;
    }

    public void setCarPic(String carPic) {
        this.carPic = carPic;
    }

    public void setFrameNum(String frameNum) {
        this.frameNum = frameNum;
    }

    public void setMotorNum(String motorNum) {
        this.motorNum = motorNum;
    }

    public int getCarId() {
        return carId;
    }

    public String getCarBrand() {
        return carBrand;
    }

    public String getCarModel() {
        return carModel;
    }

    public String getCarType() {
        return carType;
    }

    public String getCarPic() {
        return carPic;
    }

    public String getFrameNum() {
        return frameNum;
    }

    public String getMotorNum() {
        return motorNum;
    }

    public void setCarPrice(int carPrice) {
        this.carPrice = carPrice;
    }

    public void setCarDate(String carDate) {
        this.carDate = carDate;
    }

    public int getCarPrice() {
        return carPrice;
    }

    public String getCarDate() {
        return carDate;
    }
}
