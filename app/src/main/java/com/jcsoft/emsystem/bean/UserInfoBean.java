package com.jcsoft.emsystem.bean;

import java.io.Serializable;

/**
 * 用户信息
 * Created by jimmy on 16/1/3.
 */
public class UserInfoBean implements Serializable{
    private int carId;
    private long imei;
    private String telNum;
    private String userName;
    private int sex;
    private String province;
    private String city;
    private String area;
    private String address;
    private String idNum;
    private String phone;
    private String activeDate;
    private String expireDate;
    private int activeType;
    private String salesman;
    private String insurNum;
    private String insurUpdateTime;
    private String userToken;

    public UserInfoBean() {
    }

    public void setCarId(int carId) {
        this.carId = carId;
    }

    public void setImei(long imei) {
        this.imei = imei;
    }

    public void setTelNum(String telNum) {
        this.telNum = telNum;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setIdNum(String idNum) {
        this.idNum = idNum;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setActiveDate(String activeDate) {
        this.activeDate = activeDate;
    }

    public void setExpireDate(String expireDate) {
        this.expireDate = expireDate;
    }

    public void setActiveType(int activeType) {
        this.activeType = activeType;
    }

    public void setSalesman(String salesman) {
        this.salesman = salesman;
    }

    public int getCarId() {
        return carId;
    }

    public long getImei() {
        return imei;
    }

    public String getTelNum() {
        return telNum;
    }

    public String getUserName() {
        return userName;
    }

    public String getProvince() {
        return province;
    }

    public String getCity() {
        return city;
    }

    public String getArea() {
        return area;
    }

    public String getAddress() {
        return address;
    }

    public String getIdNum() {
        return idNum;
    }

    public String getPhone() {
        return phone;
    }

    public String getActiveDate() {
        return activeDate;
    }

    public String getInsurNum() {
        return insurNum;
    }

    public void setInsurNum(String insurNum) {
        this.insurNum = insurNum;
    }

    public String getExpireDate() {
        return expireDate;
    }

    public int getActiveType() {
        return activeType;
    }

    public String getSalesman() {
        return salesman;
    }

    public int getSex() {
        return sex;
    }

    public String getInsurUpdateTime() {
        return insurUpdateTime;
    }

    public void setInsurUpdateTime(String insurUpdateTime) {
        this.insurUpdateTime = insurUpdateTime;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getUserToken() {
        return userToken;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }
}
