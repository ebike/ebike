package com.jcsoft.emsystem.bean;

/**
 * Created by huguangwen on 16/1/3.
 */
public class UserInfoBean {

    /**
     * carId : 22093495
     * imei : 865328022093495
     * telNum : 1064820003159
     * userName : 吴鹏
     * province : 山东省
     * city : 济南市
     * area : 高新区
     * address :
     * idNum : 411322199001071357
     * phone : 18615601816
     * activeDate : Jun 7, 2015 10:27:54 AM
     * expireDate : Jun 7, 2016 12:00:00 AM
     * activeType : 1
     * salesman : 张加成
     */

    private int carId;
    private long imei;
    private String telNum;
    private String userName;
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

    public String getExpireDate() {
        return expireDate;
    }

    public int getActiveType() {
        return activeType;
    }

    public String getSalesman() {
        return salesman;
    }
}
