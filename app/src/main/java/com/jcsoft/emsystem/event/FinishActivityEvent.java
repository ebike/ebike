package com.jcsoft.emsystem.event;

/**
 * Created by jimmy on 15/12/8.
 */
public class FinishActivityEvent {
    //关闭当前活动
    private boolean finish;
    //所在的位置，用于判断，符合所在位置的数据才进行刷新
    private String target;

    public FinishActivityEvent(boolean finish) {
        this.finish = finish;
    }

    public FinishActivityEvent(boolean finish, String target) {
        this.finish = finish;
        this.target = target;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public boolean isFinish() {
        return finish;
    }

    public void setFinish(boolean finish) {
        this.finish = finish;
    }
}
