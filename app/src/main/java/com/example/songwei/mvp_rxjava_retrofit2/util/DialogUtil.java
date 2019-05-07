package com.example.songwei.mvp_rxjava_retrofit2.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

import com.example.songwei.mvp_rxjava_retrofit2.App.AppManager;
import com.example.songwei.mvp_rxjava_retrofit2.R;
import com.example.songwei.mvp_rxjava_retrofit2.ui.widget.Dialog;

public class DialogUtil {

    private static Dialog dialog;

    public static void creatDatePicker(Activity act, final EditText et) {
        AlertDialog.Builder a = new AlertDialog.Builder(act);
        //        final DatePickerDialogView dateView = new DatePickerDialogView(act);
        //        a.setTitle("选择日期");
        //        a.setView(dateView);
        //        a.setPositiveButton("确定", new DialogInterface.OnClickListener() {
        //            public void onClick(DialogInterface dialog, int which) {
        //                et.setText(dateView.getDate());
        //            }
        //        });
        a.show();
    }

    /**
     * 创建通用提示弹出窗口,提示
     *  stateCode :  0 :代表关闭窗口, 1代表回到登录界面, -1 仅关闭弹窗,2home页面
     * @return
     */
    public static Dialog createCommonDialog(final Activity activity, final String title, final String content,
                                            final int stateCode) {
        if (dialog != null) {
            if (dialog.isShowing()) {
                dialog.cancel();
            }
        }
        dialog = new Dialog(activity);
        dialog.setContentView(R.layout.dialog_currency)
                .setTitle(title)
                .setContext(content)
                .setCancelable(false)
                .setPositiveButton("确定", new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.cancel();
                        if (stateCode == 0) {
                            activity.finish();
                        }else if (stateCode == 1) {
//                            ActivityNavigator.navigateTo(UserLoginActivity.class);
                        }else if (stateCode == 2 ){
//                            AppManager.getAppManager().finishAllButMainActivity();
//                            ActivityNavigator.navigateTo(MainActivity.class);
                        }
                    }
                }).show();
        return dialog;
    }

//    /**
//     * 创建自定义通用提示弹出窗口,提示
//     *  view :  自定义布局
//     *  title :  标题
//     *  stateCode : -1:仅关闭弹窗, 0:代表关闭窗口, 1:代表回到登录界面, 2:home页面
//     * @return
//     */
//    public static Dialog createCustomDialog(final Activity activity,int view,String title,
//                                            final int stateCode) {
//        if (dialog != null) {
//            if (dialog.isShowing()) {
//                dialog.cancel();
//            }
//        }
//        dialog = new Dialog(activity);
//        dialog.setContentView(view);
//                .setTitle(title)
//                .setCancelable(false)
//                .setPositiveButton("确定", new OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        dialog.cancel();
//                        if (stateCode == 0) {
//                            activity.finish();
//                        }else if (stateCode == 1) {
////                            ActivityNavigator.navigateTo(UserLoginActivity.class);
//                        }else if (stateCode == 2 ){
////                            AppManager.getAppManager().finishAllButMainActivity();
////                            ActivityNavigator.navigateTo(MainActivity.class);
//                        }
//                    }
//                }).show();
//        return dialog;
//    }

    /**
     * 创建确认窗口
     * @return
     */
    public static Dialog createConformDialog(final String title, final String content) {
        final Dialog dialog = new Dialog(AppManager.getAppManager().currentActivity());
        dialog.setContentView(R.layout.dialog_currency2)
                .setTitle(title)
                .setContext(content).setCancelable(false)
                .setPositiveButton("确定", new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.cancel();
                        AppManager.getAppManager().currentActivity().finish();
                    }
                }).setNegativeButton("取消", new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        }).show();
        return dialog;
    }

    /**
     * 创建提示能监听的弹出窗口
     *
     * @return
     */
    public static Dialog createCommonDialog(final String title, final String content,
                                            final boolean isClose, final OnClickListener listener) {
        final Dialog dialog = new Dialog(AppManager.getAppManager().currentActivity());
        dialog.setContentView(R.layout.dialog_currency)
                .setTitle(title)
                .setContext(content).setCancelable(false)
                .setPositiveButton("确定", new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.cancel();
                        TextView tv = new TextView(AppManager.getAppManager().currentActivity());
                        tv.setOnClickListener(listener);
                        tv.performClick();
                    }
                }).show();
        return dialog;
    }


}

