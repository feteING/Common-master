package com.fete.common.ui.login;

import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

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

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.Bind;

/**
 * Created by llf on 2017/3/15.
 * 我的
 */

public class RegistFragment extends BaseFragment {
    Context context;


    @Bind(R.id.next)
    PaperButton nextBt;
    @Bind(R.id.userpassword)
    EditTextWithDel userpassword;
    @Bind(R.id.send_smscode)
    PaperButton sendsmscode;
    @Bind(R.id.userphone)
    EditTextWithDel userphone;
    @Bind(R.id.smscode)
    EditTextWithDel smscode;
    @Bind(R.id.fg_regist)
    LinearLayout fg_regist;
    @Bind(R.id.rela_rephone)
    RelativeLayout rela_rephone;
    @Bind(R.id.rela_recode)
    RelativeLayout rela_recode;
    @Bind(R.id.rela_repass)
    RelativeLayout rela_repass;
    @Bind(R.id.usericon)
    ImageView phoneIv;
    @Bind(R.id.keyicon)
    ImageView keyIv;
    @Bind(R.id.codeicon)
    ImageView passIv;
    MyCountTimer timer;


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(NetStatusEvent netStatusEvent) {
        LogTest.e("testFragment:" + netStatusEvent.toString());
    }


    public static RegistFragment getInstance() {
        RegistFragment mineFragment = new RegistFragment();
        return mineFragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_regist;
    }

    @Override
    protected void initView() {
        context = getActivity();
        TextListener();
        TextOnClick();

    }

    private void TextOnClick() {
        //发送验证码点击事件
        sendsmscode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final View view = v;
                String phone = userphone.getText().toString();
                boolean mobile = CheckUtils.judgePhoneNums(phone);
                if (!TextUtils.isEmpty(phone)) {
                    if (mobile) {
                        timer = new MyCountTimer(60000, 1000);
                        timer.start();
                        NetService.sendCode(phone, new BaseCallBack() {
                            @Override
                            public void onError(Exception e, int id) {
                                showSnackar(view, "IYO提示：短信发送成功");
                            }

                            @Override
                            public void onResponse(String response, int id) {
                                if (response == null) {//验证码发送成功
                                    Log.i("smile", "短信id：" + response);
                                    showSnackar(view, "IYO提示：短信发送成功");
                                } else {
                                    showSnackar(view, "IYO提示：短信发送失败" + "错误码" + response);

                                }
                            }
                        });
                    } else {
                        rela_rephone.setBackground(getResources().getDrawable(R.drawable.bg_border_color_cutmaincolor));
                        phoneIv.setAnimation(AnimationTools.shakeAnimation(2));
                        showSnackar(view, "IYO提示：输入手机号码");
                    }


                } else {
                    rela_rephone.setBackground(getResources().getDrawable(R.drawable.bg_border_color_cutmaincolor));
                    phoneIv.setAnimation(AnimationTools.shakeAnimation(2));
                    showSnackar(view, "IYO提示：手机号码不正确");
                }

            }
        });
        //下一步的点击事件
        nextBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final View view = v;
                final String password = userpassword.getText().toString();
                String code = smscode.getText().toString();
                final String phone = userphone.getText().toString();

                if (TextUtils.isEmpty(phone)) {
                    // fg_regist.setBackgroundResource(R.color.colorAccent);
                    rela_rephone.setBackground(getResources().getDrawable(R.drawable.bg_border_color_cutmaincolor));
                    phoneIv.setAnimation(AnimationTools.shakeAnimation(2));
                    showSnackar(view, "IYO提示：请输入手机号码");

                    return;
                }
                if (!CheckUtils.judgePhoneNums(phone)) {
                    rela_rephone.setBackground(getResources().getDrawable(R.drawable.bg_border_color_cutmaincolor));
                    phoneIv.setAnimation(AnimationTools.shakeAnimation(2));
                    showSnackar(view, "IYO提示：手机号不正确");
                    // fg_regist.setBackgroundResource(R.color.colorAccent);
                    return;
                }
                if (TextUtils.isEmpty(code)) {
                    rela_recode.setBackground(getResources().getDrawable(R.drawable.bg_border_color_cutmaincolor));
                    keyIv.setAnimation(AnimationTools.shakeAnimation(2));
                    // fg_regist.setBackgroundResource(R.color.colorAccent);
                    showSnackar(view, "IYO提示：请输入验证码");
                    return;

                }
                if (TextUtils.isEmpty(password)) {
                    rela_repass.setBackground(getResources().getDrawable(R.drawable.bg_border_color_cutmaincolor));
                    passIv.setAnimation(AnimationTools.shakeAnimation(2));
                    // fg_regist.setBackgroundResource(R.color.colorAccent);
                    showSnackar(view, "IYO提示：请输入密码");
                    return;
                }
                NetService.registPwd(phone, password, code, new BaseCallBack() {
                    @Override
                    public void onError(Exception e, int id) {
             /*           rela_recode.setBackground(getResources().getDrawable(R.drawable.bg_border_color_cutmaincolor));
                        keyIv.setAnimation(AnimationTools.shakeAnimation(2));
                        showSnackar(view, "IYO提示：验证码错误");*/

                        DbService.setUserNamePwd(context, phone, password);
                        Intent intent = new Intent(getActivity(), MainActivity.class);
                        startActivity(intent);
                        getActivity().overridePendingTransition(R.anim.fade,
                                R.anim.my_alpha_action);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        DbService.setUserNamePwd(context, phone, password);
                        Intent intent = new Intent(getActivity(), MainActivity.class);
                        startActivity(intent);
                        getActivity().overridePendingTransition(R.anim.fade,
                                R.anim.my_alpha_action);

                    }
                });

            }
        });


    }

    private void TextListener() {
        //用户名改变背景变
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
                    rela_rephone.setBackground(getResources().getDrawable(R.drawable.bg_border_color_black));

                }

            }
        });
        //验证码改变背景变
        smscode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                rela_recode.setBackground(getResources().getDrawable(R.drawable.bg_border_color_black));


            }
        });
        //密码改变背景变
        userpassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                rela_repass.setBackground(getResources().getDrawable(R.drawable.bg_border_color_black));


            }
        });


    }

    @Override
    protected void lazyFetchData() {

    }


    //事件定时器
    class MyCountTimer extends CountDownTimer {

        public MyCountTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            sendsmscode.setText((millisUntilFinished / 1000) + "秒后重发");
            sendsmscode.setClickable(false);
        }

        @Override
        public void onFinish() {
            sendsmscode.setText("重新发送");
            sendsmscode.setClickable(true);
        }
    }

    //回收timer
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (timer != null) {
            timer.cancel();
        }
    }


}
