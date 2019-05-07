package com.example.songwei.mvp_rxjava_retrofit2.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.songwei.mvp_rxjava_retrofit2.R;
import com.example.songwei.mvp_rxjava_retrofit2.model.Translation1;
import com.example.songwei.mvp_rxjava_retrofit2.net.ApiService;
import com.example.songwei.mvp_rxjava_retrofit2.net.NetWorkManager;
import com.example.songwei.mvp_rxjava_retrofit2.util.L;

import java.util.Calendar;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by songwei on 2019/3/20.
 */

public class PostRequest extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_splash);

        request();
    }
    public void request() {

        Calendar calender = Calendar.getInstance();
        calender.setTime(new Date());
//        //Rx使用方式
//        NetWorkManager.getInstance().service.getDayData(calender.get(Calendar.YEAR), calender.get(Calendar.MONTH) + 1, calender.get(Calendar.DAY_OF_MONTH))
//                .subscribeOn(Schedulers.io())
//                .map(new Func1<GankData, GankData.ResultsBean>() {
//                    @Override
//                    public GankData.ResultsBean call(GankData gankData) {
//                        return gankData.getResults();
//                    }
//                })
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Subscriber<GankData.ResultsBean>() {
//
//                    @Override
//                    public void onCompleted() {
//                        Log.v("success:", "onCompleted");
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        Log.v("failure:", "onError");
//                    }
//
//                    @Override
//                    public void onNext(GankData.ResultsBean resultsBean) {
//
//                    }
//
//                });
//
//        //call使用方式
//        NetWorkManager.getInstance().service.getDayData(calender.get(Calendar.YEAR), calender.get(Calendar.MONTH) + 1, calender.get(Calendar.DAY_OF_MONTH))
//                .enqueue(new Callback<GankData>() {
//                    @Override
//                    public void onResponse(Call<GankData> call, Response<GankData> response) {
//
//                    }
//
//                    @Override
//                    public void onFailure(Call<GankData> call, Throwable t) {
//
//                    }
//                });


        //步骤4:创建Retrofit对象
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://fanyi.youdao.com/") // 设置 网络请求 Url
                .addConverterFactory(GsonConverterFactory.create()) //设置使用Gson解析(记得加入依赖)
                .build();
        // 步骤5:创建 网络请求接口 的实例
        ApiService request = retrofit.create(ApiService.class);
        //对 发送请求 进行封装(设置需要翻译的内容)
        Call<Translation1> call = request.getCall("I love you");
        //步骤6:发送网络请求(异步)
        call.enqueue(new Callback<Translation1>() {
            //请求成功时回调
            @Override
            public void onResponse(Call<Translation1> call, Response<Translation1> response) {
                // 请求处理,输出结果
                // 输出翻译的内容
                System.out.println("翻译是："+ response.body().getTranslateResult().get(0).get(0).getTgt());
            }
            //请求失败时回调
            @Override
            public void onFailure(Call<Translation1> call, Throwable throwable) {
                System.out.println("请求失败");
                System.out.println(throwable.getMessage());
            }
        });


//        //RxJava组合使用
//        NetWorkManager.getInstance().service.getCallData("Call dad")
//                .subscribeOn(Schedulers.io())
//                .map(new Func1<GankData, GankData.ResultsBean>() {
//                    @Override
//                    public GankData.ResultsBean call(GankData gankData) {
//                        return gankData.getResults();
//                    }
//                })
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Subscriber<Translation1>() {
//
//                    @Override
//                    public void onCompleted() {
//                        L.e("完成:"+"onCompleted");
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        L.e("错误:onError");
//                        e.printStackTrace();
//                    }
//
//                    @Override
//                    public void onNext(Translation1 resultsBean) {
//                        L.e("得到的值是:" + resultsBean.getElapsedTime());
//                    }
//
//                });
    }


}
