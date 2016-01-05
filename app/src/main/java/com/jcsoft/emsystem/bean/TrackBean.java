package com.jcsoft.emsystem.bean;

/**
 * 轨迹信息
 * Created by jimmy on 16/1/5.
 */
public class TrackBean {

    /**
     * carId : 22093495
     * satelliteTime : Jan 4, 2016 7:07:34 PM
     * lon : 117135894
     * lat : 36690274
     * lonR : 117129768
     * latR : 36689840
     * speed : 0
     * heading : 211
     * ptType : 2
     * sourceType : 1
     */

    private int carId;
    private String satelliteTime;
    private int lon;
    private int lat;
    private int lonR;
    private int latR;
    private int speed;
    private int heading;
    private int ptType;
    private int sourceType;

    public TrackBean() {
    }

    public void setCarId(int carId) {
        this.carId = carId;
    }

    public void setSatelliteTime(String satelliteTime) {
        this.satelliteTime = satelliteTime;
    }

    public void setLon(int lon) {
        this.lon = lon;
    }

    public void setLat(int lat) {
        this.lat = lat;
    }

    public void setLonR(int lonR) {
        this.lonR = lonR;
    }

    public void setLatR(int latR) {
        this.latR = latR;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public void setHeading(int heading) {
        this.heading = heading;
    }

    public void setPtType(int ptType) {
        this.ptType = ptType;
    }

    public void setSourceType(int sourceType) {
        this.sourceType = sourceType;
    }

    public int getCarId() {
        return carId;
    }

    public String getSatelliteTime() {
        return satelliteTime;
    }

    public int getLon() {
        return lon;
    }

    public int getLat() {
        return lat;
    }

    public int getLonR() {
        return lonR;
    }

    public int getLatR() {
        return latR;
    }

    public int getSpeed() {
        return speed;
    }

    public int getHeading() {
        return heading;
    }

    public int getPtType() {
        return ptType;
    }

    public int getSourceType() {
        return sourceType;
    }
}
