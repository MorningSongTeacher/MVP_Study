package com.example.songwei.mvp_rxjava_retrofit2.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * 只需要成功和失败时,使用
 * */
public class BaseEntity implements Serializable{

    private String respCode;
    private String respMsg;

    public BaseEntity() {
        super();
    }

    public String getRescode() {
        return respCode;
    }

    public void setRescode(String rescode) {
        this.respCode = rescode;
    }

    public String getResdesc() {
        return respMsg;
    }

    public void setResdesc(String resdesc) {
        this.respMsg = resdesc;
    }

}
