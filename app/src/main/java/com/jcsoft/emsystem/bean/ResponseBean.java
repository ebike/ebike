package com.jcsoft.emsystem.bean;

import java.io.Serializable;

/**
 * 响应状态信息
 * Created by jimmy on 2015/07/12.
 */
public class ResponseBean implements Serializable {

    private Integer code;
    private String errmsg;

    public ResponseBean(){

    }

    public String getErrmsg() {
        return errmsg;
    }

    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }
}
