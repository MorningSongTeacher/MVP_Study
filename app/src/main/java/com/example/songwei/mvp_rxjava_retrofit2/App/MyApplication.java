package com.example.songwei.mvp_rxjava_retrofit2.App;

import android.app.Application;

import com.bugtags.library.Bugtags;
import com.example.songwei.mvp_rxjava_retrofit2.BuildConfig;
import com.example.songwei.mvp_rxjava_retrofit2.net.NetWorkManager;

import me.jessyan.autosize.AutoSizeConfig;
import me.jessyan.autosize.unit.Subunits;

/**
 * Created by Administrator on 2016/12/25 0025.
 */
public class MyApplication extends Application {

    private static MyApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        //初始化网络请求配置
        NetWorkManager.getInstance();

        //        Bugtags.start("642d13c7337ff3cf6cfd6c263adcffa6", this, Bugtags.BTGInvocationEventNone);
        //        Bugtags.start("642d13c7337ff3cf6cfd6c263adcffa6", this, Bugtags.BTGInvocationEventBubble);    //带上传圆球
        Bugtags.start(BuildConfig.DEBUG ? "642d13c7337ff3cf6cfd6c263adcffa6"
                : "800467175ca9b5f24097b17a178b2ef7", this, Bugtags.BTGInvocationEventNone); //内侧和线上App Key

        initAutoSize();     //今日头条自动适配
    }

    public static MyApplication getInstance() {
        if (null == instance) {
            instance = new MyApplication();
        }
        return instance;
    }

    private void initAutoSize() {
        AutoSizeConfig.getInstance()
                .getUnitsManager()
                .setSupportDP(false)
                .setSupportSP(false)
                .setSupportSubunits(Subunits.MM);
    }
}
