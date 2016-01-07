package com.jcsoft.emsystem.bean;

import java.io.Serializable;

/**
 * Created by jimmy on 16/1/7.
 */
public class ReceiveExtraBean implements Serializable{
    public int eventType;

    public ReceiveExtraBean(int eventType) {
        this.eventType = eventType;
    }
}
