package com.example.songwei.mvp_rxjava_retrofit2.ui.activity;

import android.os.Bundle;

import com.example.songwei.mvp_rxjava_retrofit2.R;
import com.example.songwei.mvp_rxjava_retrofit2.net.IpInfoTask;
import com.example.songwei.mvp_rxjava_retrofit2.presenter.IpInfoPresenter;
import com.example.songwei.mvp_rxjava_retrofit2.ui.fragment.IpInfoFragment;
import com.example.songwei.mvp_rxjava_retrofit2.util.ActivityUtils;

public class IpInfoActivity extends BaseActivity {

    private IpInfoPresenter ipInfoPresenter;

    @Override
    protected int getContentViewLayoutResources() {
        return R.layout.act_ipinfo;
    }

    @Override
    protected void initResource(Bundle savedInstanceState) {
        IpInfoFragment ipInfoFragment = (IpInfoFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);
        if (ipInfoFragment == null) {
            ipInfoFragment = IpInfoFragment.newInstance();
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),
                    ipInfoFragment, R.id.contentFrame);
        }
        IpInfoTask ipInfoTask = IpInfoTask.getInstance();
        ipInfoPresenter = new IpInfoPresenter(ipInfoFragment, ipInfoTask);
        ipInfoFragment.setPresenter(ipInfoPresenter);
    }
}
