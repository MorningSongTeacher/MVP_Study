package com.example.songwei.mvp_rxjava_retrofit2.net;

import org.json.JSONObject;

import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

public interface GetRequestInterface {

    @POST(".")
    Observable<String> getCall(@Body JSONObject parms);

}
