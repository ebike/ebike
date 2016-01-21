package com.jcsoft.emsystem.bean;

import java.io.Serializable;

/**
 * 保险条款
 * Created by jimmy on 16/1/21.
 */
public class InsurInfoBean implements Serializable{

    private int carId;
    private String userName;
    private String phone;
    private String idNum;
    private String insurNum;
    private String startDate;
    private String endDate;
    private String province;
    private String city;
    private int notify;

    public InsurInfoBean() {
    }

    public void setCarId(int carId) {
        this.carId = carId;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setIdNum(String idNum) {
        this.idNum = idNum;
    }

    public void setInsurNum(String insurNum) {
        this.insurNum = insurNum;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public void setNotify(int notify) {
        this.notify = notify;
    }

    public int getCarId() {
        return carId;
    }

    public String getUserName() {
        return userName;
    }

    public String getPhone() {
        return phone;
    }

    public String getIdNum() {
        return idNum;
    }

    public String getInsurNum() {
        return insurNum;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public int getNotify() {
        return notify;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
