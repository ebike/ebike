package com.jcsoft.emsystem.event;

/**
 * 切换成定位模式
 * Created by jimmy on 16/2/3.
 */
public class ChangeLocationEvent {
    private boolean isChange;

    public ChangeLocationEvent(boolean isChange) {
        this.isChange = isChange;
    }

    public boolean isChange() {
        return isChange;
    }

    public void setIsChange(boolean isChange) {
        this.isChange = isChange;
    }
}
