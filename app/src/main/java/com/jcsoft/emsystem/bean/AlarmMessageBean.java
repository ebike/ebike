package com.jcsoft.emsystem.bean;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

import java.io.Serializable;

/**
 * 报警消息
 * Created by jimmy on 16/1/16.
 */
@Table(name = "t_alarm_message")
public class AlarmMessageBean implements Serializable {
    @Column(name = "id", isId = true, autoGen = true)
    private int id;
    @Column(name = "eventId")
    private int eventId;
    @Column(name = "carId")
    private int carId;
    @Column(name = "eventType")
    private int eventType;
    @Column(name = "sourceId")
    private int sourceId;
    @Column(name = "status")
    private int status;
    @Column(name = "statusDate")
    private String statusDate;
    @Column(name = "createDate")
    private String createDate;
    @Column(name = "lon")
    private int lon;
    @Column(name = "lat")
    private int lat;
    @Column(name = "speed")
    private int speed;
    @Column(name = "heading")
    private int heading;
    @Column(name = "msg")
    private String msg;
    //是否展开
    private boolean isExpand;

    public AlarmMessageBean() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public void setCarId(int carId) {
        this.carId = carId;
    }

    public void setEventType(int eventType) {
        this.eventType = eventType;
    }

    public void setSourceId(int sourceId) {
        this.sourceId = sourceId;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setStatusDate(String statusDate) {
        this.statusDate = statusDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public void setLon(int lon) {
        this.lon = lon;
    }

    public void setLat(int lat) {
        this.lat = lat;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public void setHeading(int heading) {
        this.heading = heading;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCarId() {
        return carId;
    }

    public int getEventType() {
        return eventType;
    }

    public int getSourceId() {
        return sourceId;
    }

    public int getStatus() {
        return status;
    }

    public String getStatusDate() {
        return statusDate;
    }

    public String getCreateDate() {
        return createDate;
    }

    public int getLon() {
        return lon;
    }

    public int getLat() {
        return lat;
    }

    public int getSpeed() {
        return speed;
    }

    public int getHeading() {
        return heading;
    }

    public String getMsg() {
        return msg;
    }

    public boolean isExpand() {
        return isExpand;
    }

    public void setIsExpand(boolean isExpand) {
        this.isExpand = isExpand;
    }
}
