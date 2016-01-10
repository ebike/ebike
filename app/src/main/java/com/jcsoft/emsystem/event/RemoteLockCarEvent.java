package com.jcsoft.emsystem.event;

/**
 * Created by jimmy on 16/1/10.
 */
public class RemoteLockCarEvent {
    //锁车状态 1:locked,0:unlocked
    private String isLock;
    //提示信息
    private String msg;

    public RemoteLockCarEvent(String isLock, String msg) {
        this.isLock = isLock;
        this.msg = msg;
    }

    public String getIsLock() {
        return isLock;
    }

    public void setIsLock(String isLock) {
        this.isLock = isLock;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
