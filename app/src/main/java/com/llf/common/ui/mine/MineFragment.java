package com.llf.common.ui.mine;

import android.app.Dialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.AlarmClock;
import android.support.v7.app.AppCompatDelegate;
import android.view.View;
import android.widget.ImageView;

import com.jph.takephoto.app.TakePhoto;
import com.jph.takephoto.model.TResult;
import com.llf.basemodel.base.BaseFragment;
import com.llf.basemodel.dialog.ShareDialog;
import com.llf.basemodel.dialog.base.PromptDialog;
import com.llf.basemodel.dialog.ios.BottomMenuDialog;
import com.llf.basemodel.utils.AppInfoUtil;
import com.llf.basemodel.utils.DateUtil;
import com.llf.common.R;
import com.llf.common.okhttp.AppConfig;
import com.llf.common.okhttp.base.LogTest;
import com.llf.common.okhttp.business.BaseBusinessUtil;
import com.llf.common.tools.helper.PhotoHelper;
import com.tencent.connect.common.Constants;
import com.tencent.connect.share.QQShare;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.SendMessageToWX;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.mm.sdk.openapi.WXMediaMessage;
import com.tencent.mm.sdk.openapi.WXWebpageObject;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.tencent.mm.sdk.platformtools.Util.bmpToByteArray;

/**
 * Created by llf on 2017/3/15.
 * 我的
 */

public class MineFragment extends BaseFragment implements IUiListener, ShareDialog.OneShare {
    private static final String TAG = "MineFragment";

    @Bind(R.id.avatar)
    ImageView mAvatar;
    @Bind(R.id.img_attention)
    ImageView mImgAttention;
    @Bind(R.id.img_track)
    ImageView mImgTrack;
    @Bind(R.id.img_share)
    ImageView mImgShare;


    private Tencent mTencent;
    private IWXAPI iwxapi;

    public static MineFragment getInstance() {
        MineFragment mineFragment = new MineFragment();
        return mineFragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_mine;
    }

    @Override
    protected void initView() {
        /**
         * 推荐位，根据服务器传入的图标名字图标可动态配置
         */
        mImgAttention.setImageResource(getResources().getIdentifier("ic_mine_attention", "drawable", AppInfoUtil.getPackageName(getActivity())));
        mImgTrack.setImageResource(getResources().getIdentifier("ic_mine_track", "drawable", AppInfoUtil.getPackageName(getActivity())));
        mImgShare.setImageResource(getResources().getIdentifier("ic_mine_share", "drawable", AppInfoUtil.getPackageName(getActivity())));
        //分享
        mTencent = Tencent.createInstance(AppConfig.APP_ID_QQ, getActivity());
        iwxapi = WXAPIFactory.createWXAPI(getActivity(), AppConfig.APP_ID_WEIXIN, false);
        iwxapi.registerApp(AppConfig.APP_ID_WEIXIN);
    }

    @Override
    protected void lazyFetchData() {

    }

    @OnClick({R.id.setting, R.id.attention, R.id.track, R.id.share, R.id.night, R.id.service, R.id.update, R.id.reply, R.id.avatar, R.id.rbt_msg})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.setting:
                startActivity(SettingActivity.class);
                break;
            case R.id.attention:
                startActivity(AttentionActivity.class);
                break;
            case R.id.track:
                break;
            case R.id.share:
                ShareDialog.show(getActivity(), this);
                break;
            case R.id.night:
                int mode = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
                if (mode == Configuration.UI_MODE_NIGHT_YES) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                } else if (mode == Configuration.UI_MODE_NIGHT_NO) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                }
                getActivity().recreate();
                break;
            case R.id.service:
                showToast("客服中心");
                break;
            case R.id.update:
                //检查是否更新
                BaseBusinessUtil.getInstance().checkUpdate("http://api.fir.im/apps/latest/58f87d50959d6904280005a3?api_token=9f2408863ff25abccca986e5d4d9d6ba", getActivity());
                break;
            case R.id.reply:
                Intent intent = new Intent(AlarmClock.ACTION_SET_ALARM);
                intent.putExtra(AlarmClock.EXTRA_HOUR, DateUtil.getCurrentHour());
                intent.putExtra(AlarmClock.EXTRA_MINUTES, 0);
                intent.putExtra(AlarmClock.EXTRA_MESSAGE, "设置计划");
                intent.putExtra(AlarmClock.EXTRA_VIBRATE, true);
                if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                    startActivity(intent);
                }
                break;
            case R.id.avatar:
//                PickPhotoActivity.startActivity(this, new ImgSelConfig.Builder().multiSelect(false).build(), CHANGE_AVATAIR);
                final TakePhoto takePhoto = getTakePhoto();
                BottomMenuDialog dialog = new BottomMenuDialog.BottomMenuBuilder()
                        .addItem("拍照", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                PhotoHelper.getInstance().doPhoto(0, takePhoto);
                            }
                        })
                        .addItem("相册中选择", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                PhotoHelper.getInstance().doPhoto(1, takePhoto);
                            }
                        })
                        .addItem("取消", null).build();
                dialog.show(getFragmentManager());
                break;
            case R.id.rbt_msg:
//                new PromptDialog.Builder(getActivity()).hintShow("我试试");

                new PromptDialog.Builder(getActivity()).setMessage("确认删除么!").setTitle("提示")
                        .setButton2("取消", new PromptDialog.OnClickListener() {

                            @Override
                            public void onClick(Dialog dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .setButton1("确定", new PromptDialog.OnClickListener() {
                            @Override
                            public void onClick(Dialog dialog, int which) {
                                dialog.dismiss();
                            }
                        }).show();

                break;
            default:
                break;
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {


        if (requestCode == Constants.REQUEST_QQ_SHARE) {
            Tencent.onActivityResultData(requestCode, resultCode, data, this);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public void onComplete(Object o) {
        showToast("qq分享成功");
    }

    @Override
    public void onError(UiError uiError) {
        showToast("qq分享出错" + uiError.errorMessage);
    }

    @Override
    public void onCancel() {
        showToast("qq分享取消");
    }

    @Override
    public void weixinShare() {
        if (!AppInfoUtil.isWeixinAvilible(getActivity())) {
            showToast("请先安装微信");
            return;
        }
        WXWebpageObject webpageObject = new WXWebpageObject();
        webpageObject.webpageUrl = "https://fir.im/6s7z";
        WXMediaMessage msg = new WXMediaMessage(webpageObject);
        msg.title = "出大事了";
        msg.description = "这里有个好强大的app";
        Bitmap thumb = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        msg.thumbData = bmpToByteArray(thumb, true);
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("webpage");
        req.message = msg;
        req.scene = SendMessageToWX.Req.WXSceneSession;
        iwxapi.sendReq(req);
    }

    @Override
    public void qqShare() {
        Bundle params = new Bundle();
        params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
        params.putString(QQShare.SHARE_TO_QQ_TITLE, "出大事了");
        params.putString(QQShare.SHARE_TO_QQ_SUMMARY, "这里有个好强大的app");
        params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, "https://fir.im/6s7z");
        params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, "http://avatar.csdn.net/B/0/1/1_new_one_object.jpg");
        params.putString(QQShare.SHARE_TO_QQ_APP_NAME, "秀趣");
        params.putInt(QQShare.SHARE_TO_QQ_EXT_INT, QQShare.SHARE_TO_QQ_FLAG_QZONE_AUTO_OPEN);
        mTencent.shareToQQ(getActivity(), params, this);
    }

    private String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }


    @Override
    public void takeCancel() {
        super.takeCancel();
    }

    @Override
    public void takeFail(TResult result, String msg) {
        super.takeFail(result, msg);
    }

    @Override
    public void takeSuccess(TResult result) {
        super.takeSuccess(result);
        LogTest.e(result.getImage().getOriginalPath() + "");
    }
}
