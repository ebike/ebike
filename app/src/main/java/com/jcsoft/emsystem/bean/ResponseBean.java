package com.jcsoft.emsystem.bean;

import java.io.Serializable;

/**
 * 响应状态信息
 * Created by jimmy on 2015/07/12.
 */
public class ResponseBean implements Serializable {

    private Integer code;
    private String message;

    public ResponseBean(){

    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }
}
