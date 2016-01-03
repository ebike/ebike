package com.jcsoft.emsystem.base;

/**
 * Created by huguangwen on 16/1/3.
 */
public class LocInfoBean {

    /**
     * carId : 22093495
     * lon : 117134842
     * lat : 36690208
     * speed : 0
     * heading : 0
     * sourceType : 2
     * satelliteTime : 2016-01-03 21:58
     * address : 暂时无法获取
     * isOnline : 1
     * acc : 0
     * power : 1
     * loc : 0
     * lock : 0
     * isOpenVf : false
     */

    private int carId;
    private int lon;
    private int lat;
    private int speed;
    private int heading;
    private int sourceType;
    private String satelliteTime;
    private String address;
    private String isOnline;
    private String acc;
    private String power;
    private String loc;
    private String lock;
    private boolean isOpenVf;

    public LocInfoBean() {
    }

    public int getCarId() {
        return carId;
    }

    public void setCarId(int carId) {
        this.carId = carId;
    }

    public int getLon() {
        return lon;
    }

    public void setLon(int lon) {
        this.lon = lon;
    }

    public int getLat() {
        return lat;
    }

    public void setLat(int lat) {
        this.lat = lat;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getHeading() {
        return heading;
    }

    public void setHeading(int heading) {
        this.heading = heading;
    }

    public int getSourceType() {
        return sourceType;
    }

    public void setSourceType(int sourceType) {
        this.sourceType = sourceType;
    }

    public String getSatelliteTime() {
        return satelliteTime;
    }

    public void setSatelliteTime(String satelliteTime) {
        this.satelliteTime = satelliteTime;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getIsOnline() {
        return isOnline;
    }

    public void setIsOnline(String isOnline) {
        this.isOnline = isOnline;
    }

    public String getAcc() {
        return acc;
    }

    public void setAcc(String acc) {
        this.acc = acc;
    }

    public String getPower() {
        return power;
    }

    public void setPower(String power) {
        this.power = power;
    }

    public String getLoc() {
        return loc;
    }

    public void setLoc(String loc) {
        this.loc = loc;
    }

    public String getLock() {
        return lock;
    }

    public void setLock(String lock) {
        this.lock = lock;
    }

    public boolean isOpenVf() {
        return isOpenVf;
    }

    public void setIsOpenVf(boolean isOpenVf) {
        this.isOpenVf = isOpenVf;
    }
}
