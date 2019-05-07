package com.example.songwei.mvp_rxjava_retrofit2.net;

import com.example.songwei.mvp_rxjava_retrofit2.model.IpInfo;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class IpInfoTask implements NetTask<String> {
    private static IpInfoTask INSTANCE = null;
//    private static final String HOST = API.myIp;
    private static final String HOST = "http://ip.taobao.com/service/getIpInfo.php/";
//    private static final String HOST = "http://fy.iciba.com/ajax.php";
//    private static final String HOST = "Https://api.douban.com/v2/movie/";   //有值
    private Retrofit retrofit;

    HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();

    private IpInfoTask() {
        createRetrofit();
    }

    private void createRetrofit(){

        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        /**
         * addInterceptor   设置拦截器
         * cookieJar    设置cook管理类
         * readTimeout   设置读取超时时间
         * writeTimeout  设置写的超时时间
         * connectTimeout  设置链接超时时间
         * retryOnConnectionFailure 设置是否重试链接
         */
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(new MyInterceptor())
//                .cookieJar(new CookiesManager())
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .connectTimeout(10, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .build();

        //retrofit在版本2.0.0-beta2之后，整个包名改为retrofit2.。 声明的baseUrl必须以/结尾,
        retrofit = new Retrofit.Builder()
                .baseUrl(HOST)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())     //设置数据解析器
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

        //Retrofit的设置
//    Retrofit retrofit = new Retrofit.Builder()
//            .baseUrl(""http://fanyi.youdao.com/"")
//            .addConverterFactory(ProtoConverterFactory.create()) // 支持Prototocobuff解析
//            .addConverterFactory(GsonConverterFactory.create()) // 支持Gson解析
//            .addCallAdapterFactory(RxJavaCallAdapterFactory.create()) // 支持RxJava
//            .build();
    }

    public static IpInfoTask getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new IpInfoTask();
        }
        return INSTANCE;
    }

    @Override
    public Subscription execute(String ip, final LoadTasksCallBack loadTasksCallBack) {
        ApiService ipSerVice = retrofit.create(ApiService.class);
        Subscription subscription = ipSerVice
                .getIpInfo(ip)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<IpInfo>() {

                    @Override
                    public void onStart() {
                        super.onStart();
                        loadTasksCallBack.onStart();
                    }

                    @Override
                    public void onCompleted() {
                        loadTasksCallBack.onFinish();
                    }

                    @Override
                    public void onError(Throwable e) {
                        loadTasksCallBack.onFailed();
                    }

                    @Override
                    public void onNext(IpInfo ipInfo) {
                        loadTasksCallBack.onSuccess(ipInfo);
                    }
                });
        return subscription;


//        RequestParams requestParams = new RequestParams();
//        requestParams.addFormDataPart("ip", ip);
//        HttpRequest.post(HOST, requestParams, new BaseHttpRequestCallback<IpInfo>() {
//            @Override
//            public void onStart() {
//                super.onStart();
//                loadTasksCallBack.onStart();
//            }
//
//            @Override
//            protected void onSuccess(IpInfo ipInfo) {
//                super.onSuccess(ipInfo);
//                loadTasksCallBack.onSuccess(ipInfo);
//            }
//
//            @Override
//            public void onFinish() {
//                super.onFinish();
//                loadTasksCallBack.onFinish();
//            }
//
//            @Override
//            public void onFailure(int errorCode, String msg) {
//                super.onFailure(errorCode, msg);
//                loadTasksCallBack.onFailed();
//            }
//        });
    }
}


