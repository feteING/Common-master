package com.fete.common.ui.mine;

import android.content.Intent;
import android.content.res.Configuration;
import android.provider.AlarmClock;
import android.support.v7.app.AppCompatDelegate;
import android.view.View;
import android.widget.ImageView;

import com.fete.basemodel.base.BaseFragment;
import com.fete.basemodel.dialog.ShareDialog;
import com.fete.basemodel.utils.AppInfoUtil;
import com.fete.basemodel.utils.DateUtil;
import com.fete.common.R;
import com.fete.common.okhttp.business.BaseBusinessUtil;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by llf on 2017/3/15.
 * 我的
 */

public class MineFragment extends BaseFragment {
    private static final String TAG = "MineFragment";

    @Bind(R.id.avatar)
    ImageView mAvatar;
    @Bind(R.id.img_attention)
    ImageView mImgAttention;
    @Bind(R.id.img_track)
    ImageView mImgTrack;
    @Bind(R.id.img_share)
    ImageView mImgShare;


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

                break;
            case R.id.track:
                break;
            case R.id.share:
                ShareDialog.show(getActivity(), new ShareDialog.OneShare() {
                    @Override
                    public void weixinShare() {
                        
                    }

                    @Override
                    public void qqShare() {

                    }
                });
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

                break;
            case R.id.rbt_msg:


                break;
            default:
                break;
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {


        super.onActivityResult(requestCode, resultCode, data);
    }


    private String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

}
