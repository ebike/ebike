package com.jcsoft.emsystem.bean;

/**
 * Created by jimmy on 16/1/10.
 */
public class CarInfoBean{

    /**
     * carId : 22093495
     * carBrand :
     * carModel :
     * carType : 轻型踏板
     * carPic : http://img.gnets.cn/no_pic.png
     * frameNum :
     * motorNum :
     */

    private int carId;
    private String carBrand;
    private String carModel;
    private String carType;
    private String carPic;
    private String frameNum;
    private String motorNum;

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
}
