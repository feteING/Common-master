package com.fete.basemodel.dialog.base;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.fete.basemodel.R;
import com.fete.basemodel.loading.SlackLoadingView;

/**
 * Created by star on 2015/10/20.
 */
public class LoaderDialog {

    private static LoaderDialog loaderDialog = null;

    private LoaderDialog() {
    }
/*
    public static LoaderDialog getInstance() {
        if (loaderDialog == null) {
            synchronized (LoaderDialog.class) {
                if (loaderDialog == null) {
                    loaderDialog = new LoaderDialog();
                }
            }
        }
        return loaderDialog;
    }*/

    static Dialog dialog;

    public static void show(Context context) {
        if (dialog != null) {
            dialog.dismiss();
            dialog = null;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.PromptDialogStyle2);
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.loader_dialog, null);
        SlackLoadingView mLoadingView = (SlackLoadingView) v.findViewById(R.id.loading_view);
//        SpinnerLoader spinnerLoader = (SpinnerLoader) v.findViewById(R.id.loader);
        //builer.setView(v);//这里如果使用builer.setView(v)，自定义布局只会覆盖title和button之间的那部分
        dialog = builder.create();
        mLoadingView.start();
        dialog.show();
        Window dialogWindow = dialog.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        DisplayMetrics d = context.getResources().getDisplayMetrics(); // 获取屏幕宽、高用
        lp.width = (int) (d.widthPixels * 0.8); // 宽度设置为屏幕的0.8
        dialogWindow.setAttributes(lp);
        dialog.getWindow().setContentView(v);//自定义布局应该在这里添加，要在dialog.show()的后面

    }

    public static void hide() {
        try {
            if (dialog != null) {
                dialog.dismiss();
                dialog = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
