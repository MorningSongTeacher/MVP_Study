package com.example.songwei.mvp_rxjava_retrofit2.net;

import rx.Subscription;

public interface NetTask<T> {
    Subscription execute(T data, LoadTasksCallBack callBack);
}
