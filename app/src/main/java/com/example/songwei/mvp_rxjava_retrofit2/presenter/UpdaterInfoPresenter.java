package com.example.songwei.mvp_rxjava_retrofit2.presenter;

import com.example.songwei.mvp_rxjava_retrofit2.model.UpdaterInfo;
import com.example.songwei.mvp_rxjava_retrofit2.net.LoadTasksCallBack;
import com.example.songwei.mvp_rxjava_retrofit2.net.NetTask;
import com.example.songwei.mvp_rxjava_retrofit2.view.UpdaterDataContract;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by songwei on 2019/3/18.
 */

public class UpdaterInfoPresenter implements UpdaterDataContract.Presenter, LoadTasksCallBack<UpdaterInfo> {

    private NetTask netTask;
    private UpdaterDataContract.View addTaskView;
    private CompositeSubscription mSubscriptions;
    private Subscription subscription;

    public UpdaterInfoPresenter(UpdaterDataContract.View addTaskView, NetTask netTask) {
        this.netTask = netTask;
        this.addTaskView = addTaskView;
        mSubscriptions = new CompositeSubscription();
    }

    void setPresenter() {
        addTaskView.setPresenter(this);
    }

    @Override
    public void getUpdaterInfo(String ip) {
        subscription = netTask.execute(ip, this);
        subscribe();
    }

    @Override
    public void subscribe() {
        if (subscription != null) {
            mSubscriptions.add(subscription);
        }
    }

    @Override
    public void unsubscribe() {
        if (mSubscriptions != null && mSubscriptions.hasSubscriptions()) {
            mSubscriptions.unsubscribe();
        }
    }

    @Override
    public void onSuccess(UpdaterInfo ipInfo) {
        addTaskView.setUpdaterInfo(ipInfo);
    }

    @Override
    public void onStart() {
        addTaskView.showLoading();
    }

    @Override
    public void onFailed() {
        addTaskView.showError();
        addTaskView.hideLoading();
    }

    @Override
    public void onFinish() {
        addTaskView.hideLoading();
    }
}
