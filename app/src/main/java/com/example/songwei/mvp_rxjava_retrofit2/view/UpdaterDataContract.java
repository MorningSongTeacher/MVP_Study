package com.example.songwei.mvp_rxjava_retrofit2.view;

import com.example.songwei.mvp_rxjava_retrofit2.model.UpdaterInfo;
import com.example.songwei.mvp_rxjava_retrofit2.presenter.BasePresenter;

/**
 * Created by songwei on 2019/3/17.
 */

public interface UpdaterDataContract {

    interface Presenter extends BasePresenter {
        void getUpdaterInfo(String ip);
    }

    interface View extends BaseView<Presenter> {
        void setUpdaterInfo(UpdaterInfo ipInfo);
        void showLoading();
        void hideLoading();
        void showError();
    }
}
