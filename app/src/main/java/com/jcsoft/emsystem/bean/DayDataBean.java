package com.jcsoft.emsystem.bean;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

import java.io.Serializable;

/**
 * 今日统计数据
 * Created by jimmy on 16/1/24.
 */
@Table(name = "t_day_data")
public class DayDataBean implements Serializable {
    @Column(name = "id", isId = true, autoGen = true)
    private int id;
    @Column(name = "date")
    private String date;
    @Column(name = "carId")
    private int carId;
    @Column(name = "maxSpeed")
    private int maxSpeed;
    @Column(name = "minSpeed")
    private int minSpeed;
    @Column(name = "avgSpeed")
    private double avgSpeed;
    @Column(name = "mileage")
    private double mileage;

    public DayDataBean() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setCarId(int carId) {
        this.carId = carId;
    }

    public void setMaxSpeed(int maxSpeed) {
        this.maxSpeed = maxSpeed;
    }

    public void setMinSpeed(int minSpeed) {
        this.minSpeed = minSpeed;
    }

    public void setAvgSpeed(double avgSpeed) {
        this.avgSpeed = avgSpeed;
    }

    public void setMileage(double mileage) {
        this.mileage = mileage;
    }

    public String getDate() {
        return date;
    }

    public int getCarId() {
        return carId;
    }

    public int getMaxSpeed() {
        return maxSpeed;
    }

    public int getMinSpeed() {
        return minSpeed;
    }

    public double getAvgSpeed() {
        return avgSpeed;
    }

    public double getMileage() {
        return mileage;
    }
}
