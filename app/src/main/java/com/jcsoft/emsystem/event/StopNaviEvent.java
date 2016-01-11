package com.jcsoft.emsystem.event;

/**
 * Created by huguangwen on 16/1/11.
 */
public class StopNaviEvent {
    //停止导航
    private boolean stopNavi;

    public StopNaviEvent(boolean stopNavi) {
        this.stopNavi = stopNavi;
    }

    public boolean isStopNavi() {
        return stopNavi;
    }

    public void setStopNavi(boolean stopNavi) {
        this.stopNavi = stopNavi;
    }
}
