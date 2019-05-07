package com.example.songwei.mvp_rxjava_retrofit2.net;

public interface LoadTasksCallBack<T> {
    void onSuccess(T t);
    void onStart();
    void onFailed();
    void onFinish();
}
