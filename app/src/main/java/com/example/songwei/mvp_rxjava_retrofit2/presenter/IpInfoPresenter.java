package com.example.songwei.mvp_rxjava_retrofit2.presenter;

import com.example.songwei.mvp_rxjava_retrofit2.model.IpInfo;
import com.example.songwei.mvp_rxjava_retrofit2.net.LoadTasksCallBack;
import com.example.songwei.mvp_rxjava_retrofit2.net.NetTask;
import com.example.songwei.mvp_rxjava_retrofit2.view.IpInfoContract;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

public class IpInfoPresenter implements IpInfoContract.Presenter, LoadTasksCallBack<IpInfo> {
    private NetTask netTask;
    private IpInfoContract.View addTaskView;
    private CompositeSubscription mSubscriptions;
    private Subscription subscription;

    public IpInfoPresenter(IpInfoContract.View addTaskView, NetTask netTask) {
        this.netTask = netTask;
        this.addTaskView = addTaskView;
        mSubscriptions = new CompositeSubscription();
    }

    void setPresenter() {
        addTaskView.setPresenter(this);
    }

    @Override
    public void getIpInfo(String ip) {
        subscription = netTask.execute(ip, this);
        subscribe();
    }

    @Override
    public void subscribe() {
        if(subscription!=null) {
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
    public void onSuccess(IpInfo ipInfo) {
        if (addTaskView.isActive()) {
            addTaskView.setIpInfo(ipInfo);
        }
    }

    @Override
    public void onStart() {
        if (addTaskView.isActive()) {
            addTaskView.showLoading();
        }
    }

    @Override
    public void onFailed() {
        if (addTaskView.isActive()) {
            addTaskView.showError();
            addTaskView.hideLoading();
        }
    }

    @Override
    public void onFinish() {
        if (addTaskView.isActive()) {
            addTaskView.hideLoading();
        }
    }

}
