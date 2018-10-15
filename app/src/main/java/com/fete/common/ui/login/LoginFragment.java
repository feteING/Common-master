package com.fete.common.ui.login;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fete.basemodel.animation.AnimationTools;
import com.fete.basemodel.base.BaseFragment;
import com.fete.basemodel.button.PaperButton;
import com.fete.basemodel.edittext.EditTextWithDel;
import com.fete.basemodel.utils.CheckUtils;
import com.fete.basemodel.utils.LogTest;
import com.fete.common.MainActivity;
import com.fete.common.R;
import com.fete.common.okhttp.DbService;
import com.fete.common.okhttp.NetService;
import com.fete.common.okhttp.callback.BaseCallBack;
import com.fete.common.tools.event.NetStatusEvent;
import com.feteing.sdklib.umeng.UmengHelper;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import butterknife.Bind;

/**
 * Created by llf on 2017/3/15.
 * 我的
 */

public class LoginFragment extends BaseFragment {
    Context context;

    @Bind(R.id.userph)
    EditTextWithDel userphone;
    @Bind(R.id.userpass)
    EditTextWithDel userpass;
    @Bind(R.id.bt_login)
    PaperButton bt_login;

    @Bind(R.id.tv_forgetcode)
    TextView tv_forgetcode;
    @Bind(R.id.loginusericon)
    ImageView loginusericon;
    @Bind(R.id.codeicon)
    ImageView codeicon;
    @Bind(R.id.rela_name)
    RelativeLayout rela_name;
    @Bind(R.id.rela_pass)
    RelativeLayout rela_pass;
    private Handler handler = new Handler() {
    };


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(NetStatusEvent netStatusEvent) {
        LogTest.e("testFragment:" + netStatusEvent.toString());
    }


    public static LoginFragment getInstance() {
        LoginFragment mineFragment = new LoginFragment();
        return mineFragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_login;
    }

    @Override
    protected void initView() {
        context = getActivity();
        initLogin();
        textListener();


    }

    @Override
    protected void lazyFetchData() {

    }

    private void textListener() {
        userphone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String text = userphone.getText().toString();
                if (CheckUtils.judgePhoneNums(text)) {
                    //抖动
                    rela_name.setBackground(getResources().getDrawable(R.drawable.bg_border_color_black));

                }

            }
        });
        userpass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                rela_pass.setBackground(getResources().getDrawable(R.drawable.bg_border_color_black));
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                //rela_pass.setBackground(getResources().getDrawable(R.drawable.bg_border_color_black));

            }
        });
    }

    private void initLogin() {
        SharedPreferences userinfo = getActivity().getSharedPreferences("userinfo", 0);
        ArrayList<String> userNamePwd = DbService.getUserNamePwd(context);
        userphone.setText(userNamePwd.get(0));
        userpass.setText(userNamePwd.get(1));
        bt_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String phone = userphone.getText().toString();
                final String passwords = userpass.getText().toString();
                final View view = v;

                if (TextUtils.isEmpty(phone)) {
                    rela_name.setBackground(getResources().getDrawable(R.drawable.bg_border_color_cutmaincolor));
                    loginusericon.setAnimation(AnimationTools.shakeAnimation(2));
                    showSnackar(v, "IYO提示：请输入手机号码");
                    return;
                }
                if (!CheckUtils.judgePhoneNums(phone)) {
                    //抖动
                    rela_name.setBackground(getResources().getDrawable(R.drawable.bg_border_color_cutmaincolor));
                    loginusericon.setAnimation(AnimationTools.shakeAnimation(2));
                    showSnackar(v, "IYO提示：用户名不正确");


                    return;
                }
                if (TextUtils.isEmpty(passwords)) {
                    rela_pass.setBackground(getResources().getDrawable(R.drawable.bg_border_color_cutmaincolor));
                    codeicon.setAnimation(AnimationTools.shakeAnimation(2));
                    showSnackar(v, "IYO提示：请输入密码");

                    return;
                }
                showLoading(context);

                NetService.loginPwd(phone, passwords, new BaseCallBack() {
                    @Override
                    public void onError(Exception e, int id) {
                       /* login_progress.setVisibility(View.GONE);
                        rela_pass.setBackground(getResources().getDrawable(R.drawable.bg_border_color_cutmaincolor));
                        codeicon.setAnimation(AnimationTools.shakeAnimation(2));
                        showSnackar(view, "IYO提示：登陆失败");*/

                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                hideLoading();
                                startThenKill(MainActivity.class);
                                getActivity().overridePendingTransition(R.anim.alpha_in, R.anim.my_alpha_action);


                            }
                        }, 1500);
                        DbService.setUserNamePwd(context, phone, passwords);
                        UmengHelper.umengUserLogin(phone);//登录统计

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        if (response != null) {
                            rela_name.setBackground(getResources().getDrawable(R.drawable.bg_border_color_black));
                            rela_name.setBackground(getResources().getDrawable(R.drawable.bg_border_color_black));
                            showSnackar(view, "IYO提示：登陆成功");
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    hideLoading();
                                    startThenKill(MainActivity.class);
                                    getActivity().overridePendingTransition(R.anim.alpha_in, R.anim.my_alpha_action);


                                }
                            }, 1500);

                        } else {
                            hideLoading();
                            rela_pass.setBackground(getResources().getDrawable(R.drawable.bg_border_color_cutmaincolor));
                            codeicon.setAnimation(AnimationTools.shakeAnimation(2));
                            showSnackar(view, "IYO提示：登陆失败");
                        }

                    }
                });


            }
        });


    }


}
