package com.example.songwei.mvp_rxjava_retrofit2.util;


import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2016/11/23.
 */

public class ToolAll {

    /**
     * 读取assets中的json文件
     */
    public static String getJson(Context context, String fileName) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            AssetManager assetManager = context.getAssets();
            InputStream inputStream = assetManager.open(fileName);
            BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
            byte[] buffer = new byte[1024];
            int len;
            while ((len = bufferedInputStream.read(buffer)) != -1) {
                baos.write(buffer, 0, len);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return baos.toString();
    }

    /**
     * 得到某一路径下所有文件,返回路径集合
     */
    public static List<String> getFiles(String dir) {
        List<String> headAllFile = new ArrayList<String>();
        headAllFile.clear();
        File ss = new File(dir);
        File[] fs = ss.listFiles();
        if(fs == null || fs.length == 0) {
            return headAllFile;
        }
        for (int i = 0; i < fs.length; i++) {
            if (fs[i].isDirectory()) {
            } else {
                headAllFile.add(fs[i].getAbsolutePath());
            }
        }
        return headAllFile;
    }

    /**
     * 获取文件夹大小
     * @param file File实例
     * @return long
     */
    public static long getFolderSize(File file){
        long size = 0;
        try {
            File[] fileList = file.listFiles();
            for (int i = 0; i < fileList.length; i++)
            {
                if (fileList[i].isDirectory())
                {
                    size = size + getFolderSize(fileList[i]);

                }else{
                    size = size + fileList[i].length();

                }
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        //return size/1048576;
        return size;
    }

    /**
     * 删除指定目录下文件及目录
     */
    public static void deleteFolderFile(String filePath, boolean deleteThisPath) {
        if (!TextUtils.isEmpty(filePath)) {
            try {
                File file = new File(filePath);
                if (file.isDirectory()) {// 处理目录
                    File files[] = file.listFiles();
                    for (int i = 0; i < files.length; i++) {
                        deleteFolderFile(files[i].getAbsolutePath(), true);
                    }
                }
                if (deleteThisPath) {
                    if (!file.isDirectory()) {// 如果是文件，删除
                        file.delete();
                    } else {// 目录
//                        if (file.listFiles().length == 0) {// 目录下没有文件或者目录，删除
//                            file.delete();
//                        }
                    }
                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }



    /**
     * MD5加密
     */
    public static String getMd5(String value) {
        try {
            MessageDigest bmd5 = MessageDigest.getInstance("MD5");
            bmd5.update(value.getBytes());
            int i;
            StringBuffer buf = new StringBuffer();
            byte[] b = bmd5.digest();// 加密
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }
            return buf.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     *  获取版本号
     *  @return 当前应用的版本号
     */
    public static double getVersionName(Context context) {
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(),
                    0);
            String version = info.versionName.substring(0, info.versionName.lastIndexOf("."));
            return  Double.parseDouble(version);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static String get2UpdateApk(Context context, String appfolder, String packageName, String curVersion, int curVersionCode, boolean returnSameVersion) {
        Log.w("版本信息",appfolder + " " + packageName + " " + curVersion + " " + curVersionCode + " " + returnSameVersion);

        try {
            File dirFile = new File(appfolder + File.separator);
            if(!dirFile.exists()) {
                dirFile.mkdirs();
            }
            List fs = getFiles(appfolder, "apk");
            fs.addAll(getFiles(appfolder, "APK"));
            PackageManager pm = context.getPackageManager();
            File installVersion = null;
            String maxVer = curVersion + "." + String.format("%06d", new Object[]{Integer.valueOf(curVersionCode)});
            Iterator var11 = fs.iterator();

            while(var11.hasNext()) {
                File f = (File)var11.next();
                try {
//                    PackageInfo e = pm.getPackageArchiveInfo(f.getAbsolutePath(), 1);
                    PackageInfo e = pm.getPackageArchiveInfo(f.getAbsolutePath(), PackageManager.GET_ACTIVITIES);
                    Log.i("包名:",e.packageName);
                    if(packageName.equalsIgnoreCase(e.packageName)) {
                        String ver = e.versionName + "." + String.format("%06d", new Object[]{Integer.valueOf(e.versionCode)});
                        int v = ver.compareToIgnoreCase(maxVer);
                        if(returnSameVersion && v >= 0 || !returnSameVersion && v > 0) {
                            maxVer = ver;
                            installVersion = f;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return installVersion == null?null:installVersion.getAbsolutePath();
        } catch (Exception var16) {
            return null;
        }
    }


    public static List<File> getFiles(String fullpath, String ext) {
        ArrayList tReFiles = new ArrayList();
        try {
            File tFileDir = new File(fullpath);
            File[] tFiles = tFileDir.listFiles();
            File tFile;
            int var6;
            int var7;
            File[] var8;
            if(ext != null && !ext.equals("")) {
                var8 = tFiles;
                var7 = tFiles.length;
                for(var6 = 0; var6 < var7; ++var6) {
                    tFile = var8[var6];
                    if(tFile.isFile() && tFile.getName().endsWith(ext)) {
                        tReFiles.add(tFile);
                    }
                }
            } else {
                var8 = tFiles;
                var7 = tFiles.length;
                for(var6 = 0; var6 < var7; ++var6) {
                    tFile = var8[var6];
                    tReFiles.add(tFile);
                }
            }
        } catch (Exception var9) {
        }
        return tReFiles;
    }


    public static int getVersionCode(Context context) {
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            int versioncode = info.versionCode;
            return versioncode;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 检查是否存在SDCard
     * @return
     */
    public static boolean hasSdcard() {
        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 获取不带扩展名的文件名
     */
    public static String getFileNameNoEx(String filename) {
        if ((filename != null) && (filename.length() > 0)) {
            int dot = filename.lastIndexOf('.');
            if ((dot > -1) && (dot < (filename.length()))) {
                return filename.substring(0, dot);
            }
        }
        return filename;
    }

    public static List<Bitmap> getReturnBitmap(File imageFile) {
        List<Bitmap> returnBitmap = new ArrayList<Bitmap>();    //图片转化为bitmap对象
        List<String> imageList = new ArrayList<String>();   //文件夹内图片的集合
        returnBitmap.clear();
        imageList.clear();
        imageList = (ArrayList<String>) ToolAll.getAllFiles(imageFile);
        //将每个图片路径转化为bitmap对象
        if (imageList.size() != 0) {
            Bitmap image = ToolAll.getDiskBitmap(imageList.get(0));
            returnBitmap.add(image);
        }
        return returnBitmap;
    }

    public static Bitmap getDiskBitmap(String pathString) {
        Bitmap bitmap = null;
        try {
            File file = new File(pathString);
            if (file.exists()) {
                bitmap = BitmapFactory.decodeFile(pathString); //占用内存
            }
        } catch (Exception e) {
        }
        return bitmap;
    }

    public static List<String> getAllFiles(File root) {
        List<String> list = new ArrayList<>();
        list.clear();
        File files[] = root.listFiles();
        if (files != null) {
            for (int i = 0; i < files.length; i++) {
                File file = files[i];
                if (checkIsImageFile(file.getPath())) {
                    list.add(file.getPath());
                }
            }
        }
        return list;
    }

    public static boolean checkIsImageFile(String fName) {
        boolean isImageFile = false;
        // 获取扩展名
        String FileEnd = fName.substring(fName.lastIndexOf(".") + 1,
                fName.length()).toLowerCase();
        if (FileEnd.equals("jpg") || FileEnd.equals("png") || FileEnd.equals("gif")
                || FileEnd.equals("jpeg") || FileEnd.equals("bmp")) {
            isImageFile = true;
        } else {
            isImageFile = false;
        }
        return isImageFile;
    }


    public static void disableSubControls(ViewGroup viewGroup, Boolean isEnable) {
        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            View v = viewGroup.getChildAt(i);
            if (v instanceof ViewGroup) {
                if (v instanceof Spinner) {
                    Spinner spinner = (Spinner) v;
                    spinner.setClickable(isEnable);
                    spinner.setEnabled(isEnable);
                } else if (v instanceof ListView) {
                    v.setClickable(isEnable);
                    v.setEnabled(isEnable);
                } else if (v instanceof RecyclerView) {
                    v.setClickable(isEnable);
                    v.setEnabled(isEnable);
                } else {
                    disableSubControls((ViewGroup) v,isEnable);
                }
            } else if (v instanceof EditText) {
                v.setEnabled(isEnable);
                v.setClickable(isEnable);
            } else if (v instanceof Button) {
                v.setEnabled(isEnable);
                v.setClickable(isEnable);
            } else if (v instanceof ImageView) {
                v.setEnabled(isEnable);
                v.setClickable(isEnable);
            } else if (v instanceof TextView) {
               v.setEnabled(isEnable);
                v.setClickable(isEnable);
            }
        }
    }


//    /**
//     * SharedPreferences 取值
//     */
//    public static SharedPreferences readSharedPreferences(Context context) {
//        return context.getSharedPreferences(AndroidApplication.SHAREDPREFERENCES_NAME, Activity.MODE_PRIVATE);
//    }
//    /**
//     * SharedPreferences 存取值
//     */
//    public static SharedPreferences.Editor accessSharedPreferences(Context context) {
//        return context.getSharedPreferences(AndroidApplication.SHAREDPREFERENCES_NAME, Activity.MODE_PRIVATE).edit();
//    }

//    /**
//     * SharedPreferences 存map集合
//     */
//    public static void putHashMapData(Context context, String key, Map<String, String> datas) {
//        JSONArray mJsonArray = new JSONArray();
//        Iterator<Map.Entry<String, String>> iterator = datas.entrySet().iterator();
//        JSONObject object = new JSONObject();
//        while (iterator.hasNext()) {
//            Map.Entry<String, String> entry = iterator.next();
//            try {
//                object.put(entry.getKey(), entry.getValue());
//            } catch (JSONException e) {
//
//            }
//        }
//        mJsonArray.put(object);
//        SharedPreferences sp = context.getSharedPreferences(AndroidApplication.SHAREDPREFERENCES_NAME,
//                Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = sp.edit();
//        editor.putString(key, mJsonArray.toString());
//        editor.commit();
//    }
//
//    public static Map<String, String> getHashMapData(Context context, String key) {
//        Map<String, String> datas = new HashMap<>();
//        SharedPreferences sp = context.getSharedPreferences(AndroidApplication.SHAREDPREFERENCES_NAME,
//                Context.MODE_PRIVATE);
//        String result = sp.getString(key, "");
//        try {
//            JSONArray array = new JSONArray(result);
//            for (int i = 0; i < array.length(); i++) {
//                JSONObject itemObject = array.getJSONObject(i);
//                JSONArray names = itemObject.names();
//                if (names != null) {
//                    for (int j = 0; j < names.length(); j++) {
//                        String name = names.getString(j);
//                        String value = itemObject.getString(name);
//                        datas.put(name, value);
//                    }
//                }
//            }
//        } catch (JSONException e) {
//        }
//        return datas;
//    }


    public static <T> T parseJsonAllGson(String jsonData, Class<T> jsonType) {
        Gson gson = new Gson();
        T t = gson.fromJson(jsonData, jsonType);
        return t;
    }

    public static <T> List<T> parseJsonArrayGson(String jsonData, Class<T> type) {
        Gson gson = new Gson();
        List<T> result = gson.fromJson(jsonData, new TypeToken<List<T>>() {
        }.getType());
        return result;
    }

    /**
     * Base64解析
     * @param baseData 需要解析的数据
     * @return
     */
    public static String parseBaseAllJson(String baseData) {
        return new String(Base64.decode(baseData, Base64.DEFAULT));
    }

    /**
     * Base64解析
     * @param list 传入需要解析的List
     * @return
     */
    public static List<?> parseBaseAllJson(List list) {
        List<Object> objectList = new ArrayList<Object>();
        Object objectCopy = null;
        try {
            for (Object obj : list) {
                Class clazz = obj.getClass();
                objectCopy = clazz.getConstructor().newInstance();
                Field fds[] = clazz.getDeclaredFields();
                for (Field field : fds) {// 遍历该数组
                    String listfieldName = field.getName();// 得到字段名
                    String listfirstLetter = listfieldName.substring(0, 1).toUpperCase();
                    //获得和属性对应的getXXX()方法的名字
                    String listgetMethodName = "get" + listfirstLetter + listfieldName.substring(1);
                    //获得和属性对应的setXXX()方法的名字
                    String listsetMethodName = "set" + listfirstLetter + listfieldName.substring(1);
                    //获得和属性对应的getXXX()方法
                    Method listgetMethod = clazz.getMethod(listgetMethodName, new Class[]{});
                    //获得和属性对应的setXXX()方法
                    Method listsetMethod = clazz.getMethod(listsetMethodName, new Class[]{field.getType()});
                    // get方法
                    Object value = listgetMethod.invoke(obj, new Object[]{});
                    // set方法
                    listsetMethod.invoke(obj, new Object[]{parseBaseAllJson((String) value)});
                }
                objectList.add(obj);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return objectList;
    }

    /**
     * 转换 dp
     *
     * @param context
     * @param dp
     * @return
     */
    public static int dp2px(Context context, float dp) {
        return (int) (context.getResources().getDisplayMetrics().density * dp);
    }

    public static int pxToDp(float px) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, px, Resources.getSystem().getDisplayMetrics());
    }

    public static int dpToPx(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, Resources.getSystem().getDisplayMetrics());
    }


    //正则表达式(手机号)
    public static String phone = "^1[34578]\\d{9}$";
    //6-15位大小写字母，数字及下划线
    public static String password = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z_]{6,15}$";
    //验证密码有效性 8-16位 需包含字母数字下划线
    //public static String regEx = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z_]{8,16}$";

    public static boolean judge(String tv, String data) {
        boolean isValid = false;
        CharSequence inputStr = tv;
        Pattern pattern = Pattern.compile(data);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;
    }


    /**
     * 动态设置ListView的高度
     * @param listView
     */
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        if (listView == null) return;
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) return;
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

//    /**
//     * 判断ListView是否到底部
//     *
//     * @param listView
//     * @return
//     */
//    public static boolean isListViewReachBottomEdge(final ListView listView) {
//        boolean result = false;
//        if (listView.getLastVisiblePosition() == (listView.getCount() - 2)) {
//            final View bottomChildView = listView.getChildAt(listView.getLastVisiblePosition() - listView.getFirstVisiblePosition());
//            result = (listView.getHeight() >= bottomChildView.getBottom());
//        }
//        return result;
//    }


    private static InputMethodManager mInputMethodManager;

    /**
     * EditText 失去焦点
     */
    public static void radio_checked(EditText editText, Context context) {
        mInputMethodManager = (InputMethodManager) context.getSystemService(context.INPUT_METHOD_SERVICE);
        editText.setFocusable(false);
        if (mInputMethodManager.isActive()) {
            mInputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(), 0);// 隐藏输入法
        }
    }

    /**
     * EditText 获取焦点 -- 输入法管理器
     */
    public static void edit_onclick(EditText editText, Context context) {
        mInputMethodManager = (InputMethodManager) context.getSystemService(context.INPUT_METHOD_SERVICE);
        editText.setFocusable(true);//设置输入框可聚集
        editText.setFocusableInTouchMode(true);//设置触摸聚焦
        editText.requestFocus();//请求焦点
        editText.findFocus();//获取焦点
    }

    /**
     * 显示输入法
     */
    public static void showSoftInput(EditText editText, Context context) {
        mInputMethodManager = (InputMethodManager) context.getSystemService(context.INPUT_METHOD_SERVICE);
        mInputMethodManager.showSoftInput(editText, InputMethodManager.SHOW_FORCED);// 显示输入法
    }


    /**
     * 将二进制转换成16进制
     * @param buf
     * @return
     */
    public static String parseByte2HexStr(byte buf[]) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < buf.length; i++) {
            String hex = Integer.toHexString(buf[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            sb.append(hex.toUpperCase());
        }
        return sb.toString();
    }

    /**将16进制转换为二进制
     * @param hexStr
     * @return
     */
    public static byte[] parseHexStr2Byte(String hexStr) {
        if (hexStr.length() < 1)
            return null;
        byte[] result = new byte[hexStr.length()/2];
        for (int i = 0;i< hexStr.length()/2; i++) {
            int high = Integer.parseInt(hexStr.substring(i*2, i*2+1), 16);
            int low = Integer.parseInt(hexStr.substring(i*2+1, i*2+2), 16);
            result[i] = (byte) (high * 16 + low);
        }
        return result;
    }
}
