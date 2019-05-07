package com.example.songwei.mvp_rxjava_retrofit2.net;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by songwei on 2019/3/18.
 */

public class NetWorkManager {

    public static final String BASE_URL = "http://gank.io/api/";
    public final static MediaType TYPE_IMAGE = MediaType.parse("image/*");
    public static final int DEFAULT_TIMEOUT = 30;
    public Retrofit retrofit;
    public ApiService service;
    private static NetWorkManager mInstance;

    public static NetWorkManager getInstance() {
        if (mInstance == null) {
            synchronized (NetWorkManager.class) {
                if (mInstance == null) {
                    mInstance = new NetWorkManager();
                }
            }
        }
        return mInstance;
    }

    //构造方法私有
    private NetWorkManager() {
        //对OkHttp添加Log写法/启用Log日志
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BASIC);

        //初始化OkHttpClient
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .addInterceptor(logging)    //添加日志打印,还有很多的用法，比如：封装一些公共的参数等等。参考如下博客：http://blog.csdn.net/jdsjlzx/article/details/52063950
                .build();

        //初始化retrofit
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").serializeNulls().create();
        retrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))     //这个配置是将服务器返回的json字符串转化为对象。这个是可以自定义Converter来应对服务器返回的不同的数据的。博客: https://www.jianshu.com/p/5b8b1062866b
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())   //用来决定你的返回值是Observable还是Call,如果返回为Call那么可以不添加这个配置。如果使用Observable那就必须添加这个配置。否则就会请求的时候就会报错！博客: http://blog.csdn.net/new_abc/article/details/53021387
                .baseUrl(BASE_URL)
                .build();

        //初始化Request,Request是定义的请求的服务器的API封装类。里面通过注解的方式声明所需要请求的接口
        service = retrofit.create(ApiService.class);
    }

}
