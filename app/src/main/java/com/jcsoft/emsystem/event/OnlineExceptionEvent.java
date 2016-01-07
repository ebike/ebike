package com.jcsoft.emsystem.event;

/**
 * 下线事件
 * Created by jimmy on 2015/11/16.
 */
public class OnlineExceptionEvent {
    private boolean flag;

    private String message;

    public OnlineExceptionEvent() {
    }

    public OnlineExceptionEvent(boolean flag, String message) {
        this.flag = flag;
        this.message = message;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
