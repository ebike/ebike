package com.jcsoft.emsystem.event;

/**
 * Created by jimmy on 16/1/10.
 */
public class RemoteVFEvent {
    //电子围栏状态 1:open,0:close
    private String isOpen;
    //提示信息
    private String msg;

    public RemoteVFEvent(String isOpen, String msg) {
        this.isOpen = isOpen;
        this.msg = msg;
    }

    public String getIsOpen() {
        return isOpen;
    }

    public void setIsOpen(String isOpen) {
        this.isOpen = isOpen;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
