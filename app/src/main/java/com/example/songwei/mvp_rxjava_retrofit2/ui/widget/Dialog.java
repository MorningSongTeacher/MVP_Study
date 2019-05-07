package com.example.songwei.mvp_rxjava_retrofit2.ui.widget;

import android.app.AlertDialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.songwei.mvp_rxjava_retrofit2.R;


public class Dialog {

    private AlertDialog dialog;

    public Dialog(Context act) {
        try {
            dialog = new AlertDialog.Builder(act).create();
            dialog.show();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }


    public Dialog setCancelable(boolean cancelable) {
        dialog.setCancelable(cancelable);
        return this;
    }

    public Dialog setContentView(int layout_id) {
        Window window = dialog.getWindow();
        window.setContentView(layout_id);
        window.setBackgroundDrawableResource(R.color.transparent);
        TextView tv_title = (TextView) window.findViewById(R.id.tv_title);
        tv_title.setVisibility(View.GONE);
        TextView tv_context = (TextView) window.findViewById(R.id.tv_context);
        tv_context.setVisibility(View.GONE);
        if (R.layout.dialog_currency2 == layout_id) {
            Button btn_negative = (Button) dialog.getWindow().findViewById(R.id.btn_cancel);
            btn_negative.setVisibility(View.GONE);
            View cut_line = (View) dialog.getWindow().findViewById(R.id.cut_line);
            cut_line.setVisibility(View.GONE);
        }
        if (R.layout.dialog_icon_title == layout_id) {
            ImageView iv_title = (ImageView) dialog.getWindow().findViewById(R.id.iv_title);
            iv_title.setVisibility(View.GONE);
        }
        return this;
    }

    public Dialog setBackground(int resId) {
        RelativeLayout background = (RelativeLayout) dialog.getWindow().findViewById(R.id.background);
        background.setBackgroundResource(resId);
        return this;
    }

    public Dialog setTitle(String title) {
        TextView tv_title = (TextView) dialog.getWindow().findViewById(R.id.tv_title);
        tv_title.setVisibility(View.VISIBLE);
        tv_title.setText(title);
        return this;
    }


    public Dialog setTitleIcon(int icon_id) {
        ImageView iv_title = (ImageView) dialog.getWindow().findViewById(R.id.iv_title);
        iv_title.setVisibility(View.VISIBLE);
        iv_title.setBackgroundResource(icon_id);
        return this;
    }


    public Dialog setContext(String context) {
        if ("".equals(context)) {
            TextView tv_context = (TextView) dialog.getWindow().findViewById(R.id.tv_context);
            tv_context.setVisibility(View.GONE);
        } else {
            TextView tv_context = (TextView) dialog.getWindow().findViewById(R.id.tv_context);
            tv_context.setVisibility(View.VISIBLE);
            tv_context.setText(context);
        }
        return this;
    }

    public Dialog setContext(CharSequence context) {
        if ("".equals(context)) {
            TextView tv_context = (TextView) dialog.getWindow().findViewById(R.id.tv_context);
            tv_context.setVisibility(View.GONE);
        } else {
            TextView tv_context = (TextView) dialog.getWindow().findViewById(R.id.tv_context);
            tv_context.setVisibility(View.VISIBLE);
            tv_context.setText(context);
        }
        return this;
    }

    public Dialog setPositiveButton(String positvieStr, View.OnClickListener positvieOClickListener) {
        Button btn_positvie = (Button) dialog.getWindow().findViewById(R.id.btn_ok);
        btn_positvie.setText(positvieStr);
        btn_positvie.setOnClickListener(positvieOClickListener);
        return this;
    }


    public Dialog setNegativeButton(String negativeStr, View.OnClickListener negativeOClickListener) {
        View cut_line = (View) dialog.getWindow().findViewById(R.id.cut_line);
        cut_line.setVisibility(View.VISIBLE);
        Button btn_negative = (Button) dialog.getWindow().findViewById(R.id.btn_cancel);
        btn_negative.setText(negativeStr);
        btn_negative.setOnClickListener(negativeOClickListener);
        btn_negative.setVisibility(View.VISIBLE);
        return this;
    }


    public void show() {
        dialog.show();
    }

    public void cancel() {
        dialog.cancel();
    }

    public boolean isShowing() {
        return dialog.isShowing();
    }

}
