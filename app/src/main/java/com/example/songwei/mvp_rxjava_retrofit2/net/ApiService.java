package com.example.songwei.mvp_rxjava_retrofit2.net;

import com.example.songwei.mvp_rxjava_retrofit2.model.IpInfo;
import com.example.songwei.mvp_rxjava_retrofit2.model.ThirdPartyData;
import com.example.songwei.mvp_rxjava_retrofit2.model.Translation1;
import com.example.songwei.mvp_rxjava_retrofit2.model.UpdaterInfo;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by songwei on 2019/3/13.
 */

public interface ApiService {
    //模版
    //采用@Post表示Post方法进行请求（传入部分url地址）
    // 采用@FormUrlEncoded注解的原因:API规定采用请求格式x-www-form-urlencoded,即表单形式
    // 需要配合@Field使用
    @POST("translate?doctype=json&jsonversion=&type=&keyfrom=&model=&mid=&imei=&vendor=&screen=&ssid=&network=&abtest=")
    @FormUrlEncoded
    Call<Translation1> getCall(@Field("i") String targetSentence);

    @POST("translate?doctype=json&jsonversion=&type=&keyfrom=&model=&mid=&imei=&vendor=&screen=&ssid=&network=&abtest=")
    @FormUrlEncoded
    Observable<Translation1> getCallData(@Field("i") String targetSentence);


    @FormUrlEncoded
    @POST("getIpInfo.php")
    Observable<IpInfo> getIpInfo(@Field("ip") String ip);

    /***
     * 直接访问，不带任何参数，此处注解@GET必须加一个“/”，否则会报错
     */
    @GET("/")
    Observable<UpdaterInfo> getUpdaterInfo();


    @POST(".")
    Observable<ThirdPartyData> getThirdPartyData(@Body JSONObject parms);


//    @FormUrlEncoded
//    @POST("getUpdaterInfo.php")
//    Observable<UpdaterInfo> getUpdaterInfo(@Field("update") String update);



//    @FormUrlEncoded
//    @POST("getUpdaterInfo.php")
//    Observable<Response<T>> getUpdaterInfo(@Field("update") String update);


//    /**
//     * 每日一Gank
//     **/
//    @GET("day/{year}/{month}/{day}")
//    Call/*Observable*/<GankData> getDayData(@Path("year") int year, @Path("month") int month, @Path("day") int day);
//
//    /**
//     * 获取IP信息
//     **/
//    @FormUrlEncoded
//    @POST("service/getIpInfo.php")
//    Call<IpInfo> getIpInfo(@Field("ip") String ip);
//
//    /**
//     * 上传头像
//     **/
//    @Multipart
//    @POST("uptUserHeadImg")
//    Call/*Observable*/<UserBean> uploadHead(@Part MultipartBody.Part photo, @Part("uid") RequestBody userId);
//
//    /**
//     * 下载图片
//     */
//    @GET
//    Call/*Observable*/<ResponseBody> downloadPicFromNet(@Url String fileUrl);
//
//    介绍一下retrofit的注解(*注释部分是配合rx使用。)
//1. get请求
//* @GET 申明get请求方式，括号里面是请求路径。
//            * @Query
//    请求参数对应的键值，括号内为key，参数为value。
//            * @QueryMap
//    如果Query参数比较多，那么可以通过@QueryMap方式将所 有的参数集成在一个Map。
//            * @Path 会把参数填充到路径上，如上面的@Path("year") @Path("month") @Path("day") 会填充@GET("day/{year}/{month}/{day}") 中的year、month、day。@Path可用于任何请求方式。
//            * @Url 不使用baseUrl。
//            2. post请求
//* @POST 申明post请求方式，括号里面是请求路径。
//            * @FormUrlEncoded 自动将请求参数的类型调整为application/x-www-form-urlencoded表单类型，FormUrlEncoded不能用于Get请求。
//            * @Field 请求参数对应的键值。
//            * @FieldMap
//    如果Field参数比较多，那么可以通过@FieldMap方式将所有的参数集成在一个Map。
//            * @Body
//    请求参数有多个，那么统一封装到类中应该会更好，这样维护起来会非常方便。
//            3. 上传文件
//* @Multipart 申明为上传文件方式。
//            * @Part 参数列表 MultipartBody.Part为文件类型，RequestBody为一般参数。
//            * @PartMap
//    如果Part参数比较多，那么可以通过@PartMap方式将所有的参数集成在一个Map。@PartMap
//    Map<String, RequestBody> params可以是多个文件，也可是文件与参数混合。


}
