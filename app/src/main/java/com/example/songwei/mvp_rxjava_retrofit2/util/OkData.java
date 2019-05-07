package com.example.songwei.mvp_rxjava_retrofit2.util;

import com.example.songwei.mvp_rxjava_retrofit2.net.GetRequestInterface;
import com.example.songwei.mvp_rxjava_retrofit2.net.HttpRequestUtils;

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by guoyan on 2017/6/1
 * 请求数据的封装类 post请求
 */
public class OkData {
    /**
     * 请求网络的方法
     * @param url 请求url
     * @param parmsJson 请求参数 fasejson的JSONObject对象
     * @param listener  请求回调 
     */
    public void requesData(String url, JSONObject parmsJson, final HttpRequestUtils.OnLoadDataListener listener) {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Interceptor.Chain chain) throws IOException {
                Request original = chain.request();
                Request request = original.newBuilder()
                        //                            .addHeader("key", <em>EncoderByMd5</em>())
                        .addHeader("key", "EncoderByMd5")
                        .addHeader("version", "1.5.1")
                        .addHeader("application/json", "charset=utf-8").method(original.method(), original.body()).build();
                return chain.proceed(request);
            }
        });

        OkHttpClient client = httpClient.build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())     //这个配置是将服务器返回的json字符串转化为对象。这个是可以自定义Converter来应对服务器返回的不同的数据的。博客: https://www.jianshu.com/p/5b8b1062866b
//                                .addConverterFactory(ScalarsConverterFactory.create())     //需要依赖String包
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())   //用来决定你的返回值是Observable还是Call,如果返回为Call那么可以不添加这个配置。如果使用Observable那就必须添加这个配置。否则就会请求的时候就会报错！博客: http://blog.csdn.net/new_abc/article/details/53021387
                .build();

        GetRequestInterface requst = retrofit.create(GetRequestInterface.class);
        Observable<String> call = requst.getCall(parmsJson);
        call.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    //                                @Override
                    //                                public void onSubscribe(Disposable d) {
                    //                                    //开始请求
                    //                                }

                    @Override
                    public void onNext(String value) {
                        //请求数据
                        if (listener != null) {
                            listener.onLoadCallBack(true, value);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        //请求失败
                        if (listener != null) {
                            listener.onError(e.toString());
                        }
                    }

                    @Override
                    public void onCompleted() {
                    }
                });

    }
}
