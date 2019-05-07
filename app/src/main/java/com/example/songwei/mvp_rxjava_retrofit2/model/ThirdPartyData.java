package com.example.songwei.mvp_rxjava_retrofit2.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by 36570 on 2018/8/9.
 */

public class ThirdPartyData {

   private String authInfo;	    //是	String	授权请求参数	授权请求参数

    public String getAuthInfo() {
        return authInfo;
    }

    public void setAuthInfo(String authInfo) {
        this.authInfo = authInfo;
    }
}
