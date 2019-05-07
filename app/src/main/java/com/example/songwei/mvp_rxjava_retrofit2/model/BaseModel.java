package com.example.songwei.mvp_rxjava_retrofit2.model;

import java.io.Serializable;

public class BaseModel<T> extends BaseEntity implements Serializable {

    public BaseModel() {
        super();
    }

    private T bizBody;

    public T getResData() {
        return bizBody;
    }

    public void setResData(T resData) {
        this.bizBody = resData;
    }

}
