package com.fete.common.ui.test;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.fete.basemodel.bannner.BGABanner;
import com.fete.basemodel.base.BaseFragment;
import com.fete.basemodel.dialog.base.LoaderDialog;
import com.fete.basemodel.dialog.base.PromptDialog;
import com.fete.basemodel.dialog.ios.BottomMenuDialog;
import com.fete.basemodel.emptyview.EmptyLayout;
import com.fete.basemodel.fragmenthelper.FragmentStack;
import com.fete.basemodel.location.LocationHelper;
import com.fete.basemodel.utils.LogTest;
import com.fete.common.R;
import com.fete.common.okhttp.business.BaseBusinessUtil;
import com.fete.common.tools.event.NetStatusEvent;
import com.fete.common.tools.helper.PhotoHelper;
import com.fete.common.ui.mine.MineFragment;
import com.jph.takephoto.app.TakePhoto;
import com.jph.takephoto.model.TResult;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * Created by llf on 2017/3/15.
 * 我的
 */

public class TestFragment extends BaseFragment {
    private static final String TAG = "MineFragment";
    Context context;
    @Bind(R.id.banner_normal)
    public BGABanner bannerNormal;

    @Bind(R.id.dialog1)
    public Button dialog1;
    @Bind(R.id.dialog2)
    public Button dialog2;
    @Bind(R.id.dialog3)
    public Button dialog3;
    @Bind(R.id.dialog4)
    public Button dialog4;
    //空布局
    @Bind(R.id.llCenter)
    public LinearLayout mLlCenter; //根布局
    private EmptyLayout mEmptyLayout;

    @Bind(R.id.show_empty)
    public Button showEmpty;
    @Bind(R.id.show_error)
    public Button showError;
    @Bind(R.id.show_load)
    public Button showLoad;
    @Bind(R.id.show_nonet)
    public Button showNoNet;

    @Bind(R.id.fragment_push)
    public Button fragmentPush;
    @Bind(R.id.fragment_pop)
    public Button fragmentPop;
    @Bind(R.id.fragment_replace)
    public Button fragmentReplace;
    @Bind(R.id.fragment_clear)
    public Button fragmentClear;
    private FragmentStack mFragmentStack;


    @Bind(R.id.loading)
    public Button btnLoading;

    @Bind(R.id.refresh_layout)
    public Button refreshLayout;

    @Bind(R.id.scrollview)
    public Button scrollview;

    @Bind(R.id.location)
    public Button location;

    @Bind(R.id.refresh)
    public Button refresh;


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(NetStatusEvent netStatusEvent) {
        LogTest.e("testFragment:" + netStatusEvent.toString());
    }


    public static TestFragment getInstance() {
        TestFragment mineFragment = new TestFragment();
        return mineFragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_test;
    }

    @Override
    protected void initView() {
        context = getActivity();
        initDialog();
        initBanner();
        initEmptyView();
        initFragment();
        initLoading();
        initRefreshLayout();
        initScrollView();
        initLocation();
        initRefresh();

    }

    private void initRefresh() {
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(RefreshActivity.class);
            }
        });
    }

    private void initLocation() {
        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LocationHelper.getInstance().locationStart(context);
            }
        });
    }

    private void initScrollView() {
        scrollview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(ScrollViewActivity.class);
            }
        });
    }

    private void initRefreshLayout() {
        refreshLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(ToolBarActivity.class);
            }
        });

    }

    private void initLoading() {
        btnLoading.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoaderDialog.show(context);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        LoaderDialog.hide();
                    }
                }, 4000);
            }
        });


    }

    private void initFragment() {
        mFragmentStack = FragmentStack.create(getFragmentManager(), R.id.fl_container);
        fragmentPush.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mFragmentStack.push(MineFragment.getInstance(), null);
            }
        });
        fragmentPop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mFragmentStack.pop(true);
            }
        });
        fragmentReplace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mFragmentStack.replace(TestFragment.getInstance(), null);
            }
        });
        fragmentClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mFragmentStack.clear();
            }
        });

    }

    private void initEmptyView() {
        mEmptyLayout = new EmptyLayout(context, mLlCenter, EmptyLayout.RELATIVEPARENT);
        mEmptyLayout.setIsLoadingTransparent(false);
        mEmptyLayout.showContent();
        mEmptyLayout.setmEmptyListener(new EmptyLayout.onEmptyListener() {
            @Override
            public void onClickEmpty(View v) {
                showErrorHint("empty返回");
            }
        });
        mEmptyLayout.setmErrorListener(new EmptyLayout.onErrorListener() {
            @Override
            public void onClickError(View v) {
                showToast("error返回");
            }
        });
        mEmptyLayout.setmNoNetListener(new EmptyLayout.onNoNetListener() {
            @Override
            public void onClickNoNet(View v) {
                showToast("nonet返回");
            }
        });

        showEmpty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mEmptyLayout.showEmpty();
                clearEmptyView();
            }
        });
        showError.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mEmptyLayout.showError();
                clearEmptyView();
            }
        });
        showLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mEmptyLayout.showLoading();
                clearEmptyView();
            }
        });
        showNoNet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mEmptyLayout.showNoNet();
                clearEmptyView();
            }
        });

    }

    public void clearEmptyView() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mEmptyLayout.showContent();
            }
        }, 4000);
    }

    private void initDialog() {
        dialog1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new PromptDialog.Builder(getActivity()).hintShow("我试试");
            }
        });
        dialog2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
            }
        });
        dialog3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
            }
        });
        dialog4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BaseBusinessUtil.getInstance().checkUpdate("http://api.fir.im/apps/latest/58f87d50959d6904280005a3?api_token=9f2408863ff25abccca986e5d4d9d6ba", context);
            }
        });

    }

    private void initBanner() {
        bannerNormal.setVisibility(View.VISIBLE);
        // 设置数据
        bannerNormal.setAdapter(new BGABanner.Adapter<ImageView, String>() {
            @Override
            public void fillBannerItem(BGABanner banner, ImageView itemView, String model, int position) {
                itemView.setScaleType(ImageView.ScaleType.FIT_XY);
                Glide.with(context)
                        .load(model)
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .dontAnimate()
                        .into(itemView);
            }
        });
        List<String> urls = new ArrayList<>();
        List<String> texts = new ArrayList<>();
        urls.add("http://e.hiphotos.baidu.com/zhidao/pic/item/9358d109b3de9c82c5c25e216d81800a18d843fc.jpg");
        urls.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1538027334495&di=265f588ad1feab9f11f50fb101fd12c6&imgtype=0&src=http%3A%2F%2Fa.hiphotos.baidu.com%2Fbaike%2Fc0%253Dbaike60%252C5%252C5%252C60%252C20%253Bt%253Dgif%2Fsign%3D077996308326cffc7d27b7e0d86821f5%2Fb8389b504fc2d56235093044e61190ef76c66c12.jpg");
        urls.add("http://e.hiphotos.baidu.com/zhidao/pic/item/0823dd54564e9258a55f83eb9c82d158ccbf4e4f.jpg");
        urls.add("http://c.hiphotos.baidu.com/zhidao/wh%3D450%2C600/sign=3b76c41d9d82d158bbd751b5b53a35ee/342ac65c10385343118dc5d29213b07ecb808887.jpg");
        texts.add("test1");
        texts.add("test2");
        texts.add("test3");
        texts.add("test4");

        bannerNormal.setData(urls, texts);
        bannerNormal.setDelegate(new BGABanner.Delegate<ImageView, String>() {
            @Override
            public void onBannerItemClick(BGABanner banner, ImageView itemView, String model, int position) {
                Toast.makeText(banner.getContext(), "点击了" + position, Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    protected void lazyFetchData() {

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    //相册返回结果
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
