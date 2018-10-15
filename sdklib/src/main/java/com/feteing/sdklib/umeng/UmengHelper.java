package com.feteing.sdklib.umeng;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.feteing.sdklib.R;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;
import com.umeng.socialize.shareboard.SnsPlatform;
import com.umeng.socialize.utils.ShareBoardlistener;
import com.umeng.socialize.utils.SocializeUtils;

import java.lang.ref.WeakReference;
import java.util.Map;

/**
 * 1.标题栏自己设置颜色  标题栏与状态栏都是蓝色
 * 2.fragment 在activity中改动比如 mainactivity 切换fragment时改变颜色
 * ToolbarHelper.getInstance().setBarColor(MainActivity.this,R.color.Green);
 */
public class UmengHelper {


    private static UmengHelper umengHelper = null;

    private UmengHelper() {
        // TODO Auto-generated constructor stub
    }


    public static UmengHelper getInstance() {
        if (umengHelper == null) {
            synchronized (UmengHelper.class) {
                if (umengHelper == null) {
                    umengHelper = new UmengHelper();
                }
            }
        }
        return umengHelper;
    }

    public static void umengOnResume(Context context) {
        MobclickAgent.onResume(context);
    }

    public static void umengOnPause(Context context) {
        MobclickAgent.onPause(context);
    }

    /**
     * 账号统计登录
     *
     * @param userId
     */
    public static void umengUserLogin(String userId) {
        MobclickAgent.onProfileSignIn(userId);
    }

    /**
     * 账号统计三方登录
     *
     * @param type
     * @param userId
     */
    public static void umengUser3Login(int type, String userId) {
        MobclickAgent.onProfileSignIn(userId);
        switch (type) {
            case 1:
                //微博
                MobclickAgent.onProfileSignIn("WB", userId);
                break;
            case 2:
                //微信
                MobclickAgent.onProfileSignIn("WX", userId);
                break;
            case 3:
                //qq
                MobclickAgent.onProfileSignIn("qq", userId);
                break;
            default:
                //其他
                MobclickAgent.onProfileSignIn("other", userId);
                break;
        }

    }

    /**
     * 账号统计退出
     */
    public static void umengUserExit() {
        MobclickAgent.onProfileSignOff();
    }

    private UMShareListener mShareListener;
    private ShareAction mShareAction;
    public String shareUrl = "http://mobile.umeng.com/social";
    public int shareLogo = R.drawable.logo;

    /**
     * 友盟分享
     * 注意
     * 1.wxapi 在主包中
     * 2.修改 <data android:scheme="tencent100424468" /> 这个与application中注册的qqid要一致
     * PlatformConfig.setQQZone("100424468", "c7394704798a158208a74ab60104f0ba");
     * 3.application
     * //     {
     * //     PlatformConfig.setWeixin("wxcc653fe774b6e168", "c321015f332c874d0fbb1b716df1668e");
     * //     //豆瓣RENREN平台目前只能在服务器端配置
     * //     PlatformConfig.setSinaWeibo("3921700954", "04b48b094faeb16683c32669824ebdad", "http://sns.whalecloud.com");
     * //     PlatformConfig.setQQZone("100424468", "c7394704798a158208a74ab60104f0ba");
     * //     }
     * <p>
     * 4.微信分享要包名签名对上才能分享
     *
     * @param context
     */
    public void umengShare(final Activity context) {
        mShareListener = new CustomShareListener(context);
        /*增加自定义按钮的分享面板*/
        mShareAction = new ShareAction(context).setDisplayList(
                SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE,
                SHARE_MEDIA.SINA, SHARE_MEDIA.QQ)
//                .addButton("复制文本", "复制文本", "umeng_socialize_copy", "umeng_socialize_copy")
//                .addButton("复制链接", "复制链接", "umeng_socialize_copyurl", "umeng_socialize_copyurl")
                .setShareboardclickCallback(new ShareBoardlistener() {
                    @Override
                    public void onclick(SnsPlatform snsPlatform, SHARE_MEDIA share_media) {
                        if (snsPlatform.mShowWord.equals("复制文本")) {
                            Toast.makeText(context, "复制文本按钮", Toast.LENGTH_LONG).show();
                        } else if (snsPlatform.mShowWord.equals("复制链接")) {
                            Toast.makeText(context, "复制链接按钮", Toast.LENGTH_LONG).show();

                        } else {
                            UMWeb web = new UMWeb(shareUrl);
                            web.setTitle("来自分享面板标题");
                            web.setDescription("来自分享面板内容");
                            web.setThumb(new UMImage(context, shareLogo));
                            new ShareAction(context).withMedia(web)
                                    .setPlatform(share_media)
                                    .setCallback(mShareListener)
                                    .share();
                        }
                    }
                });
        mShareAction.open();
//        ShareBoardConfig config = new ShareBoardConfig();
//        config.setMenuItemBackgroundShape(ShareBoardConfig.BG_SHAPE_NONE);
//        mShareAction.open(config);
    }


    private static class CustomShareListener implements UMShareListener {

        private WeakReference<Activity> mActivity;

        private CustomShareListener(Activity activity) {
            mActivity = new WeakReference(activity);
        }

        @Override
        public void onStart(SHARE_MEDIA platform) {

        }

        @Override
        public void onResult(SHARE_MEDIA platform) {

            if (platform.name().equals("WEIXIN_FAVORITE")) {
                Toast.makeText(mActivity.get(), platform + " 收藏成功啦", Toast.LENGTH_SHORT).show();
            } else {
                if (platform != SHARE_MEDIA.MORE && platform != SHARE_MEDIA.SMS
                        && platform != SHARE_MEDIA.EMAIL
                        && platform != SHARE_MEDIA.FLICKR
                        && platform != SHARE_MEDIA.FOURSQUARE
                        && platform != SHARE_MEDIA.TUMBLR
                        && platform != SHARE_MEDIA.POCKET
                        && platform != SHARE_MEDIA.PINTEREST

                        && platform != SHARE_MEDIA.INSTAGRAM
                        && platform != SHARE_MEDIA.GOOGLEPLUS
                        && platform != SHARE_MEDIA.YNOTE
                        && platform != SHARE_MEDIA.EVERNOTE) {
                    Toast.makeText(mActivity.get(), platform + " 分享成功啦", Toast.LENGTH_SHORT).show();
                }

            }
        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            if (platform != SHARE_MEDIA.MORE && platform != SHARE_MEDIA.SMS
                    && platform != SHARE_MEDIA.EMAIL
                    && platform != SHARE_MEDIA.FLICKR
                    && platform != SHARE_MEDIA.FOURSQUARE
                    && platform != SHARE_MEDIA.TUMBLR
                    && platform != SHARE_MEDIA.POCKET
                    && platform != SHARE_MEDIA.PINTEREST
                    && platform != SHARE_MEDIA.INSTAGRAM
                    && platform != SHARE_MEDIA.GOOGLEPLUS
                    && platform != SHARE_MEDIA.YNOTE
                    && platform != SHARE_MEDIA.EVERNOTE) {
                Toast.makeText(mActivity.get(), platform + " 分享失败啦", Toast.LENGTH_SHORT).show();

            }

        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {

            Toast.makeText(mActivity.get(), platform + " 分享取消了", Toast.LENGTH_SHORT).show();
        }
    }


    /**
     * final boolean isauth = UMShareAPI.get(mContext).isAuthorize(mActivity, list.get(position).mPlatform);
     * UMShareAPI.get(mContext).deleteOauth(mActivity, list.get(position).mPlatform, authListener);
     * UMShareAPI.get(mContext).doOauthVerify(mActivity, list.get(position).mPlatform, authListener);
     *
     * @param context type 1 微信
     *                type 2 qq
     *                type 3 微博
     */
    public void ThreeLoginByType(Activity context, int type) {
        if (dialog == null) {
            dialog = new ProgressDialog(context);
        }
        switch (type) {
            case 1:
                UMShareAPI.get(context).doOauthVerify(context, SHARE_MEDIA.SINA, authListener);
                break;
            case 2:
                UMShareAPI.get(context).doOauthVerify(context, SHARE_MEDIA.WEIXIN, authListener);
                break;
            case 3:
                UMShareAPI.get(context).doOauthVerify(context, SHARE_MEDIA.QQ, authListener);
//                UMShareAPI.get(context).deleteOauth(context, SHARE_MEDIA.QQ, authListener);
                break;


        }


    }

    private ProgressDialog dialog;
    UMAuthListener authListener = new UMAuthListener() {
        @Override
        public void onStart(SHARE_MEDIA platform) {
            SocializeUtils.safeShowDialog(dialog);
        }

        @Override
        public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {
            SocializeUtils.safeCloseDialog(dialog);
            Log.e("====<", "三方登陆成功:" + data.toString());
        }

        @Override
        public void onError(SHARE_MEDIA platform, int action, Throwable t) {
            SocializeUtils.safeCloseDialog(dialog);
            Log.e("====<", "三方登陆失败:" + t.getMessage());
        }

        @Override
        public void onCancel(SHARE_MEDIA platform, int action) {
            SocializeUtils.safeCloseDialog(dialog);
            Log.e("====<", "三方登陆取消了");
        }
    };


}
