package com.example.songwei.mvp_rxjava_retrofit2.net;

import android.content.Context;

import org.json.JSONObject;

public interface HttpRequestUtils {
    /**
     * post 方式请求数据
     * @param mContext 上下文
     * @param url 地址
     * @param parmsJson  params参数
     * @param listener
     */
    void loadingDataPost(Context mContext, String url, JSONObject parmsJson, OnLoadDataListener listener);

    public interface OnLoadDataListener {

        /**
         * 数据正常回调 
         * @param flag   是否请求成功，这里的成功是在我们项目接口里面的成功和失败
         * @param result 成功则返回数据，失败则返回原因
         */
        void onLoadCallBack(boolean flag, String result);

        /**
         *  出现异常时候的回调，
         *  效果和上面的接口种的flag=false类似，但是这个失败是属于访问层次的失败
         *  @param errorStr 失败的原因
         */
        void onError(String errorStr);

    }
}
