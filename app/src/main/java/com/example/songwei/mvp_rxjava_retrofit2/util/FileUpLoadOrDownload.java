package com.example.songwei.mvp_rxjava_retrofit2.util;


import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by 36570 on 2018/5/17.
 */

public class FileUpLoadOrDownload {
    public static final String CONTENT_TYPE_LABEL = "Content-Type";
    public static final String CONTENT_TYPE_VALUE_JSON = "application/json; charset=utf-8";

    //okhttp文件上传
    public static String getResult(String imagePath, String url) {
        OkHttpClient mOkHttpClient = new OkHttpClient();
        String result = "error";
        try {
            MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
            JSONObject jb = new JSONObject();
            JSONObject jb2 = new JSONObject();
            jb2.put("bizBody", jb);
            File file = new File(imagePath);
            RequestBody body = RequestBody.create(MediaType.parse("image/jpeg; charset=utf-8"), file);
            builder.addFormDataPart("file", file.getName(), body);
            builder.addFormDataPart("params", jb2.toString());

            Request request = new Request.Builder()
                    .url(url)
                    .post(builder.build())
                    .build();

            Response response = mOkHttpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                String resultValue = response.body().string();
                return resultValue;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    //文件下载
    public static void download(final String url, final String saveDir, final Map<String, String> map, final FileUpLoadOrDownload.OnDownloadListener listener) {
        OkHttpClient okHttpClient = new OkHttpClient();
//        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);

        //MediaType.parse("text/x-markdown; charset=utf-8");
        //RequestBody.create(MEDIA_TYPE_MARKDOWN, param)

        JSONObject jb;
        JSONObject jb2 = new JSONObject();
        try {
            jb = new JSONObject();
            jb.put("id", map.get("id"));
            jb2 = new JSONObject();
            jb2.put("bizBody", jb);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Request request = new Request.Builder()
                .url(url)
                .addHeader(CONTENT_TYPE_LABEL, CONTENT_TYPE_VALUE_JSON)
                .post(RequestBody.create(MediaType.parse("text/x-markdown; charset=utf-8"), jb2.toString()))
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // 下载失败
                listener.onDownloadFailed();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                InputStream is = null;
                byte[] buf = new byte[2048];
                int len = 0;
                FileOutputStream fos = null;
                //判断文件夹是否存在
                String savePath = FileUpLoadOrDownload.isExistDir(saveDir);
                try {
                    is = response.body().byteStream();
                    long total = response.body().contentLength();
//                    File file = new File(savePath, FileUpLoadOrDownload.getNameFromUrl(url)+".jpg");
                    File file = new File(savePath, map.get("id"));
                    fos = new FileOutputStream(file);
                    long sum = 0;
                    while ((len = is.read(buf)) != -1) {
                        fos.write(buf, 0, len);
                        sum += len;
                        int progress = (int) (sum * 1.0f / total * 100);
                        // 下载中
                        listener.onDownloading(progress);
                    }
                    fos.flush();
                    // 下载完成
                    listener.onDownloadSuccess();
                } catch (Exception e) {
                    listener.onDownloadFailed();
                } finally {
                    try {
                        if (is != null)
                            is.close();
                    } catch (IOException e) {
                    }
                    try {
                        if (fos != null)
                            fos.close();
                    } catch (IOException e) {
                    }
                }
            }
        });
    }

    /**
     * @throws IOException 判断下载目录是否存在
     */
    public static String isExistDir(String saveDir) throws IOException {
        // 下载位置
//        File downloadFile = new File(Environment.getExternalStorageDirectory(), saveDir);
        File downloadFile = new File(saveDir);
        if (!downloadFile.exists()) {
            downloadFile.mkdirs();
        }
//        if (!downloadFile.mkdirs()) {
//            downloadFile.createNewFile();
//        }
        String savePath = downloadFile.getAbsolutePath();
        return savePath;
    }

    /**
     * 从下载连接中解析出文件名
     */
    public static String getNameFromUrl(String url) {
        return url.substring(url.lastIndexOf("/") + 1);
    }


    public interface OnDownloadListener {
        /**
         * 下载成功
         */
        void onDownloadSuccess();

        /**
         * @param progress 下载进度
         */
        void onDownloading(int progress);

        /**
         * 下载失败
         */
        void onDownloadFailed();
    }
}
