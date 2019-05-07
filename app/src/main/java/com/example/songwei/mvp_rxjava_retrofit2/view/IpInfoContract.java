package com.example.songwei.mvp_rxjava_retrofit2.view;

import com.example.songwei.mvp_rxjava_retrofit2.model.IpInfo;
import com.example.songwei.mvp_rxjava_retrofit2.presenter.BasePresenter;

public interface IpInfoContract {
    interface Presenter extends BasePresenter {
        void getIpInfo(String ip);
    }

    interface View extends BaseView<Presenter> {
        void setIpInfo(IpInfo ipInfo);
        void showLoading();
        void hideLoading();
        void showError();
        boolean isActive();
    }
}
