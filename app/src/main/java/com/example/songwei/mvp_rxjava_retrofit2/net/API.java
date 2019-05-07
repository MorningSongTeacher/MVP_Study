package com.example.songwei.mvp_rxjava_retrofit2.net;

/**
 * Created by Administrator on 2016/11/23.
 */

public class API {

    public static String updater = "http://更新网址/";  //注意以"/" 号为结尾
    public static String zhifubaoDate = "http://获取支付宝登录的数据参数/";  //注意以"/" 号为结尾

    // Get请求URL模板
//    http://fy.iciba.com/ajax.php
//    URL实例
//    http://fy.iciba.com/ajax.php?a=fy&f=auto&t=auto&w=hello%20world

    // POST请求URl模版
// URL
//    http://fanyi.youdao.com/translate
// URL实例
//    http://fanyi.youdao.com/translate?doctype=json&jsonversion=&type=&keyfrom=&model=&mid=&imei=&vendor=&screen=&ssid=&network=&abtest=
// 参数说明
// doctype：json 或 xml
// jsonversion：如果 doctype 值是 xml，则去除该值，若 doctype 值是 json，该值为空即可
// xmlVersion：如果 doctype 值是 json，则去除该值，若 doctype 值是 xml，该值为空即可
// type：语言自动检测时为 null，为 null 时可为空。英译中为 EN2ZH_CN，中译英为 ZH_CN2EN，日译中为 JA2ZH_CN，中译日为 ZH_CN2JA，韩译中为 KR2ZH_CN，中译韩为 ZH_CN2KR，中译法为 ZH_CN2FR，法译中为 FR2ZH_CN
// keyform：mdict. + 版本号 + .手机平台。可为空
// model：手机型号。可为空
// mid：平台版本。可为空
// imei：???。可为空
// vendor：应用下载平台。可为空
// screen：屏幕宽高。可为空
// ssid：用户名。可为空
// abtest：???。可为空

// 请求方式说明
// 请求方式：POST
// 请求体：i
// 请求格式：x-www-form-urlencoded



    private static String url = "http://ip.taobao.com";
//    private static String url = "http://fy.iciba.com/ajax.php";
    //https://www.xxx.com/app_v5/  这个比较吓人,哈啊哈哈哈,请在无人的角落观看

    public static String myIp = url + "/service/getIpInfo.php";

}
