package com.example.songwei.mvp_rxjava_retrofit2.ui.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.TextView;
import android.widget.Toast;

import com.example.songwei.mvp_rxjava_retrofit2.R;
import com.example.songwei.mvp_rxjava_retrofit2.model.BaseModel;
import com.example.songwei.mvp_rxjava_retrofit2.model.IpInfo;
import com.example.songwei.mvp_rxjava_retrofit2.model.ThirdPartyData;
import com.example.songwei.mvp_rxjava_retrofit2.model.UpdaterInfo;
import com.example.songwei.mvp_rxjava_retrofit2.net.API;
import com.example.songwei.mvp_rxjava_retrofit2.net.ApiService;
import com.example.songwei.mvp_rxjava_retrofit2.net.HttpRequestUtils;
import com.example.songwei.mvp_rxjava_retrofit2.ui.widget.CommonProgressDialog;
import com.example.songwei.mvp_rxjava_retrofit2.util.DialogUtil;
import com.example.songwei.mvp_rxjava_retrofit2.util.L;
import com.example.songwei.mvp_rxjava_retrofit2.util.OkData;
import com.example.songwei.mvp_rxjava_retrofit2.util.SharedPreferencesHelper;
import com.example.songwei.mvp_rxjava_retrofit2.util.SharedPreferencesNameFile;
import com.example.songwei.mvp_rxjava_retrofit2.util.ToolAll;
import com.example.songwei.mvp_rxjava_retrofit2.view.IpInfoContract;
import com.example.songwei.mvp_rxjava_retrofit2.view.UpdaterDataContract;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import butterknife.Bind;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.RuntimePermissions;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

@RuntimePermissions
public class SplashActivity extends BaseActivity implements UpdaterDataContract.View{

    SharedPreferencesHelper sharedPreferencesHelper;

    private static final int GO_HOME = 1000;
    private static final int GO_LOGIN = 1001;
    private static final int GO_NAVIGATION = 1002;
    private static final int GO_UPDATER = 1003;
    // 延迟2秒
    private static final long SPLASH_DELAY_MILLIS = 500;
    @Bind(R.id.tv_version)
    TextView tvVersion;

    //更新相关
    private CommonProgressDialog pBar;
    private UpdaterDataContract.Presenter updaterPresenter;
    //    String url = "http://openbox.mobilem.360.cn/index/d/sid/3429345";//安装包下载地址

    // 下载存储的文件名
    private static final String DOWNLOAD_NAME = "yifutong.apk";
    private static final String DOWNHEADPORTRAIT = "";      //下载用户头像


    @Override
    protected int getContentViewLayoutResources() {
        return R.layout.act_splash;
    }

    @Override
    protected void initResource(Bundle savedInstanceState) {
        //如果帐号密码本地保存了(一般不本地不保存,与敏感数据脱敏),那么可以登录之前,取出账号密码解密,进行登录
        sharedPreferencesHelper = new SharedPreferencesHelper(this);
        //1秒钟之后再执行, 期间可以加载动画
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                isFirstIn();
                //   getThirdPartyData();  //初始化时,加载需要的数据,如支付宝登录相关的数据

                //直接跳到home页
//                mHandler.sendEmptyMessageDelayed(GO_HOME, SPLASH_DELAY_MILLIS);
            }
        }, 1000);
    }

    private void isFirstIn() {
        //是否用户第一次登录
        boolean isFirstIn = (boolean) sharedPreferencesHelper.getSharedPreference(SharedPreferencesNameFile.IS_FIRST_IN, true);
        if (isFirstIn) {
            //第一次的登录可以进入导航页面,当用户查看完了并设置sp里就可以存入isFirstIn = true 和 isNavigationPage = true
            boolean isNavigationPage = (boolean) sharedPreferencesHelper.getSharedPreference(SharedPreferencesNameFile.NAVIGATION_PAGE, false);
            if (isNavigationPage) { //进入登录页面
                mHandler.sendEmptyMessageDelayed(GO_HOME, SPLASH_DELAY_MILLIS);
            } else {     //进入导航页面
                mHandler.sendEmptyMessageDelayed(GO_NAVIGATION, SPLASH_DELAY_MILLIS);
            }
        } else {
            init();
        }
    }

    private void init() {
        //检查更新
        int visionCode = ToolAll.getVersionCode(this);
        double visionName = ToolAll.getVersionName(this);
        tvVersion.setText("" + visionName);
        //检查版本是否需要更新
        SplashActivityPermissionsDispatcher.getVersionWithCheck(this, visionCode, visionName);
    }

    @NeedsPermission({Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE})
    public void getVersion(final int visionCode, final double visionName) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API.updater)
                .addConverterFactory(GsonConverterFactory.create())//设置 Json 转换器
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())//RxJava 适配器
                .build();
        ApiService rxjavaService = retrofit.create(ApiService.class);
        rxjavaService.getUpdaterInfo()
                .subscribeOn(Schedulers.io())//IO线程加载数据
                .observeOn(AndroidSchedulers.mainThread())//主线程显示数据
                .subscribe(new Subscriber<UpdaterInfo>() {
                    @Override
                    public void onCompleted() { }

                    @Override
                    public void onError(Throwable e) { }

                    @Override
                    public void onNext(UpdaterInfo updaterInfo) {
                        L.e("版本号:" + updaterInfo.getVersion());
                        L.e("版本详情:" + updaterInfo.getDetails());
                        L.e("版本地址:" + updaterInfo.getUrl());
                        try {
                            String newversion = updaterInfo.getVersion();
                            String content = updaterInfo.getDetails();
                            String url = updaterInfo.getUrl();
                            //这里主要分为了大版本更新和小版本(补丁),"."号后面可以在作为补丁的差异版本.
                            String appVersion = newversion.substring(0, newversion.lastIndexOf("."));
                            String patchVersion = newversion.substring(newversion.lastIndexOf(".") + 1, newversion.length());

                            String AppVersion = appVersion;
                            //   String AppVersion = "2.0";
                            double dAppVersion = Double.parseDouble(AppVersion);
                            if (visionName < dAppVersion) {      //更新版本
                                ShowDialog(content, url);
                                return;
                            } else {    //比较补丁,补丁更新
                                int patchVersionNuberr = (int) sharedPreferencesHelper.getSharedPreference(SharedPreferencesNameFile.PATCH_VERSION, 0);
                                if (patchVersionNuberr < Integer.parseInt(patchVersion)) {
                                    Toast.makeText(SplashActivity.this, "开始补丁更新", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                            }
                            //获取支付宝相关数据, 支付宝登录时, 为了脱敏, 登录的数据要从后端获取
                            getThirdPartyData();
                        } catch (Exception e) {
                            //现在不上线, 暂时不用强制重启
                            DialogUtil.createCommonDialog(SplashActivity.this, "提示", "都是后台的错", -1);
                        }
                    }
                });
    }

    /**
     * 获取支付宝登录相关的数据
     */
    private void getThirdPartyData() {
        try {
            JSONObject parmsJson = new JSONObject();
            parmsJson.put("bizBody", "");
            parmsJson.put("blackBox", "");
            parmsJson.put("method", "");
            parmsJson.put("sign", "");
            OkData data = new OkData();
            data.requesData(API.zhifubaoDate, parmsJson, new HttpRequestUtils.OnLoadDataListener() {
                @Override
                public void onLoadCallBack(boolean flag, String result) {
                    L.e("请求成功: " + result);
                    try {
                        JSONObject jo = new JSONObject(result);
                        if (jo.has("code") && jo.getInt("code") == 200) {
                            try {
                                Gson gson = new Gson();
                                //                    Type type = new TypeToken<BaseModel<ThirdPartyData>>(){}.getType();
                                //                    ThirdPartyData thirdPartyData = gson.fromJson(result,type);   //下面也能解析
                                ThirdPartyData thirdPartyData = gson.fromJson(result,ThirdPartyData.class);

                                String authInfo = thirdPartyData.getAuthInfo();
                                String newstr = authInfo.replace("=", ":\"");
                                String stringObj = "{" + newstr.replace("&", "\",") + "\"}";

                                JSONObject jb = new JSONObject(stringObj);
                                SharedPreferencesHelper sph = new SharedPreferencesHelper(SplashActivity.this);
                                sph.put("zhifubao_app_id", jb.getString("app_id"));
                                sph.put("zhifubao_pid", jb.getString("pid"));
                                sph.put("zhifubao_target_id", jb.getString("target_id"));
                                sph.put("zhifubao_sign", jb.getString("sign"));
                                boolean autoLogin = (boolean) sharedPreferencesHelper.getSharedPreference(SharedPreferencesNameFile.AUTO_LOGIN, false);
                                if (autoLogin) {
                                    mHandler.sendEmptyMessageDelayed(GO_HOME, SPLASH_DELAY_MILLIS);
                                } else {
                                    mHandler.sendEmptyMessageDelayed(GO_LOGIN, SPLASH_DELAY_MILLIS);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }else {
                            DialogUtil.createCommonDialog(SplashActivity.this,"提示", "都是后台的错", -1);
                        }
                    }catch (Exception e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void onError(String errorStr) {
                    L.e("请求失败: " + errorStr);
//                    ErrorMessageFactory.create(SplashActivity.this, e);
                }
            });
        }catch (Exception e) {
            e.printStackTrace();
        }

    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case GO_HOME:
                    refreshToken();
                    break;
                case GO_LOGIN:
                    goLogin();
                    break;
                case GO_NAVIGATION:
                    // goNavigation();
                    break;
                case GO_UPDATER:
                    // goUpdater();
                    break;
            }
            super.handleMessage(msg);
        }
    };


    private void goLogin() {
        //进入账号密码页
//        startActivity(IpInfoActivity.class);
        finish();
    }

    private void refreshToken() {
        //登录之后,刷新旧Token,以便快速登录
        String refreshToken = (String) sharedPreferencesHelper.getSharedPreference(SharedPreferencesNameFile.RefreshTOKEN, "");
        if (TextUtils.isEmpty(refreshToken)) {
            goLogin();
            return;
        }

        //        if (refreshTokenUtil == null) {
        //            refreshTokenUtil = new GetRefreshToken("", AndroidApplication.getContext());
        //        }
        //        try {
        //            JSONObject jb = new JSONObject();
        //            jb.put("refresh_token", refreshToken);
        //            JSONObject jb2 = new JSONObject();
        //            jb2.put("bizBody", jb);
        //            refreshTokenUtil.setParams(jb2.toString());
        //        } catch (Exception e) {
        //            e.printStackTrace();
        //        }
        //        refreshTokenUtil.execute(new DefaultSubscriber<BaseModel<LoginRes>>() {
        //            @Override
        //            public void onNext(BaseModel<LoginRes> response) {
        //                if ("2000".equals(response.getRescode())) {
        //                    ApiConnection.AUTH_TOKEN_VALUE = response.getResData().getToken();
        //                    sharedPreferencesHelper.put(SharedPreferencesNameFile.TOKEN, response.getResData().getToken());
        //                    sharedPreferencesHelper.put(SharedPreferencesNameFile.RefreshTOKEN, response.getResData().getRefreshToken());
        //                    startActivity(HomeActivity.class);
        //                    finish();
        //                } else {
        //                    goLogin();
        //                }
        //            }
        //
        //            @Override
        //            public void onError(Throwable e) {
        //                ErrorMessageFactory.create(SplashActivity.this, e);
        //                goLogin();
        //            }
        //        });
    }


    /**
     * 升级系统
     */
    private void ShowDialog(String content, final String url) {
        //            SplashActivityPermissionsDispatcher.showCameraWithCheck(this);

        new AlertDialog.Builder(this)
                .setTitle("版本更新")
                .setMessage(content)
                .setCancelable(false)
                .setPositiveButton("更新最新版本", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        pBar = new CommonProgressDialog(SplashActivity.this);
                        pBar.setCanceledOnTouchOutside(false);
                        pBar.setTitle("正在下载");
                        pBar.setCustomTitle(LayoutInflater.from(SplashActivity.this).inflate(R.layout.dialog_updater_title, null));
                        pBar.setMessage("正在下载");
                        pBar.setIndeterminate(true);
                        pBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                        pBar.setCancelable(false);
                        final DownloadTask downloadTask = new DownloadTask(
                                SplashActivity.this);
                        downloadTask.execute(url);
                        pBar.setOnCancelListener(new DialogInterface.OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialog) {
                                downloadTask.cancel(true);
                            }
                        });
                    }
                })
                .setNegativeButton("下次再说", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //不更新直接退出
                        //  AppManager.getAppManager().AppExit(SplashActivity.this, false);
                        //                        AppManager.getAppManager().AppExit(SplashActivity.this, false);
                        getThirdPartyData();
                        dialog.dismiss();
                    }
                })
                .show();
    }


    @OnPermissionDenied({Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    public void showDenied() {
        Toast.makeText(this, "没有内存卡权限,不能更新", Toast.LENGTH_SHORT).show();
    }

    @OnNeverAskAgain({Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    public void showNotAsk() {
        new AlertDialog.Builder(this)
                .setMessage("该功能需要访问内存卡的权限,不开启将无法正常工作!")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        SplashActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }


    /**
     * 下载应用
     */
    class DownloadTask extends AsyncTask<String, Integer, String> {

        private Context context;
        private PowerManager.WakeLock mWakeLock;

        public DownloadTask(Context context) {
            this.context = context;
        }

        @Override
        protected String doInBackground(String... sUrl) {
            InputStream input = null;
            OutputStream output = null;
            HttpURLConnection connection = null;
            File file = null;
            try {
                URL url = new URL(sUrl[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    return "服务器返回http "
                            + connection.getResponseCode() + " "
                            + connection.getResponseMessage();
                }
                // 可能是1,服务器不报告长度
                int fileLength = connection.getContentLength();
                if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                    file = new File(Environment.getExternalStorageDirectory(), DOWNLOAD_NAME);
                    if (!file.exists()) {
                        if (!file.getParentFile().exists()) {
                            file.getParentFile().mkdirs();
                        }
                    }
                } else {
                    Toast.makeText(SplashActivity.this, "sd卡未挂载", Toast.LENGTH_LONG).show();
                }
                input = connection.getInputStream();
                output = new FileOutputStream(file);
                byte data[] = new byte[4096];
                long total = 0;
                int count;
                while ((count = input.read(data)) != -1) {
                    //如果有取消按钮可以取消下载
                    if (isCancelled()) {
                        input.close();
                        return null;
                    }
                    total += count;
                    if (fileLength > 0) {
                        publishProgress((int) (total * 100 / fileLength));
                    }
                    output.write(data, 0, count);
                }
            } catch (Exception e) {
                System.out.println(e.toString());
                return e.toString();
            } finally {
                try {
                    if (output != null)
                        output.close();
                    if (input != null)
                        input.close();
                } catch (IOException ignored) {
                }
                if (connection != null)
                    connection.disconnect();
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
            mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, getClass().getName());
            mWakeLock.acquire();
            pBar.show();
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            super.onProgressUpdate(progress);
            pBar.setIndeterminate(false);
            pBar.setMax(100);
            pBar.setProgress(progress[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            mWakeLock.release();
            pBar.dismiss();
            if (result != null) {
                //                Toast.makeText(context, "您未打开SD卡权限" + result, Toast.LENGTH_LONG).show();
                Toast.makeText(context, "sokeact连接超时" + result, Toast.LENGTH_LONG).show();
            } else {
                update();
            }
        }
    }

    private void update() {
        File file = new File(Environment.getExternalStorageDirectory(), DOWNLOAD_NAME);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= 24) {
            Uri apkUri = FileProvider.getUriForFile(SplashActivity.this, "com.transfar.its_android.fileProvider", file);
            //添加这一句表示对目标应用临时授权该Uri所代表的文件
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
        } else {
            intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        startActivity(intent);
    }

    @Override
    public void setUpdaterInfo(UpdaterInfo ipInfo) {
        //        if(ipInfo != null && ipInfo.getData() != null){
        //            IpData ipData = ipInfo.getData();
        //            tv_country.setText(ipData.getCountry());
        //            tv_area.setText(ipData.getArea());
        //            tv_city.setText(ipData.getCity());
        //        }
    }


    @Override
    public void showLoading() {
        //        mDialog.show();
    }

    @Override
    public void hideLoading() {
        //        if(mDialog.isShowing()) {
        //            mDialog.dismiss();
        //        }
    }

    @Override
    public void showError() {
        Toast.makeText(this, "网络出错", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setPresenter(UpdaterDataContract.Presenter presenter) {
        updaterPresenter = presenter;
    }

    @Override
    public void onPause() {
        super.onPause();
        updaterPresenter.unsubscribe();
    }
}
