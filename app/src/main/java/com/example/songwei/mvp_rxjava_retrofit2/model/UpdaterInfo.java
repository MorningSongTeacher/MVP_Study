package com.example.songwei.mvp_rxjava_retrofit2.model;

import java.io.Serializable;

/**
 * Created by songwei on 2019/3/18.
 */

public class UpdaterInfo implements Serializable {

    private String version;
    private String details;
    private String url;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }


}
