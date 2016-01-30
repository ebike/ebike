package com.jcsoft.emsystem.base;

/**
 * 封装参数
 * Created by jimmy on 16/1/30.
 */
public class SendParamsBean {
    //服务器参数字段名
    private String key;
    //客户端传的值
    private Object value;
    //客户端传的值是否文件类型
    private boolean isFile;

    public SendParamsBean(String key, Object value, boolean isFile) {
        this.key = key;
        this.value = value;
        this.isFile = isFile;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public boolean isFile() {
        return isFile;
    }

    public void setIsFile(boolean isFile) {
        this.isFile = isFile;
    }
}
