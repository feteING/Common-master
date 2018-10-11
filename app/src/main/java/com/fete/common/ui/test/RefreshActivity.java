package com.fete.common.ui.test;

import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.util.MultiTypeDelegate;
import com.fete.basemodel.bar.BaseToolbarHelper;
import com.fete.basemodel.base.BaseActivity;
import com.fete.basemodel.utils.ImageLoaderUtils;
import com.fete.common.R;
import com.fete.common.ui.mine.SettingActivity;
import com.gyf.barlibrary.ImmersionBar;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import common.feteing.commonutils.bannner.BGABanner;

public class RefreshActivity extends BaseActivity {

    @Bind(R.id.refreshLayout)
    RefreshLayout refreshLayout;
    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    MyAdapter myAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_refresh;
    }

    @Override
    protected void initView() {
        initToolBar();
        initTest();

    }

    private void initTest() {
        refreshLayout.setEnableFooterFollowWhenLoadFinished(true);
//        refreshLayout.setEnableHeaderTranslationContent(true);//随拉动布局移动
//        refreshLayout.setRefreshFooter(new ClassicsFooter(this).setSpinnerStyle(SpinnerStyle.Translate).setProgressResource(R.drawable.ic_progress_puzzle));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        myAdapter = new MyAdapter(loadModels());
        myAdapter.addHeaderView(getHeader());
//        myAdapter.addHeaderView(getHeader());
        recyclerView.setAdapter(myAdapter);
        refreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onRefresh(final RefreshLayout refreshLayout) {
                refreshLayout.finishRefresh();
                refreshLayout.setNoMoreData(false);//恢复上拉状态

            }

            @Override
            public void onLoadMore(final RefreshLayout refreshLayout) {
                refreshLayout.getLayout().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (myAdapter.getItemCount() > 52) {
                            refreshLayout.finishLoadMoreWithNoMoreData();//设置之后，将不会再触发加载事件
                        } else {
                            myAdapter.addData(loadModels());
                            refreshLayout.finishLoadMore();
                        }
                    }
                }, 1000);
            }
        });


    }

    public BGABanner bannerNormal;

    private View getHeader() {
        View tmpView = View.inflate(this, R.layout.refresh_header, null);
        bannerNormal = tmpView.findViewById(R.id.banner_normal);
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


        return tmpView;
    }

    private void initToolBar() {
        ViewGroup viewById = (ViewGroup) findViewById(android.R.id.content);
        BaseToolbarHelper.getInstance()
                .initToolbarHelper(viewById, this)
                .setCenterText("refresh")
                .setRightText("设置", new BaseToolbarHelper.ToolBarCallBack() {
                    @Override
                    public void onClick() {
                        startActivity(SettingActivity.class);
                    }
                })
                .initHeader(RefreshActivity.this, R.color.main_color);


    }

    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
        ImmersionBar.with(this)
                .fitsSystemWindows(true)  //使用该属性,必须指定状态栏颜色
                .statusBarColor(R.color.colorPrimary)
                .init();
    }

    private class Model {
        int type;
        String imageId;
        int avatarId;
        String name;
        String nickname;

        public String getImageId() {
            return imageId;
        }

        public void setImageId(String imageId) {
            this.imageId = imageId;
        }

        public int getAvatarId() {
            return avatarId;
        }

        public void setAvatarId(int avatarId) {
            this.avatarId = avatarId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }
    }

    /**
     * 模拟数据
     */
    private ArrayList<Model> loadModels() {
        ArrayList<Model> list = new ArrayList();
        Model model = new Model() {{
            this.type = 1;
            this.name = "但家香酥鸭";
            this.nickname = "爱过那张脸";
            this.imageId = "http://img3.imgtn.bdimg.com/it/u=323485060,4088130159&fm=26&gp=0.jpg";
            this.avatarId = R.mipmap.image_avatar_1;
        }};

        Model model1 = new Model() {{
            this.type = 1;
            this.name = "香菇蒸鸟蛋";
            this.nickname = "淑女算个鸟";
            this.imageId = "http://img4.imgtn.bdimg.com/it/u=2869658580,4194315062&fm=26&gp=0.jpg";
            this.avatarId = R.mipmap.image_avatar_2;
        }};
        Model model2 = new Model() {{
            this.type = 2;
            this.name = "花溪牛肉粉";
            this.nickname = "性感妩媚";
            this.imageId = "http://img1.imgtn.bdimg.com/it/u=3725027306,1956362631&fm=26&gp=0.jpg";
            this.avatarId = R.mipmap.image_avatar_3;
        }};

        Model model3 = new Model() {{
            this.type = 2;
            this.name = "破酥包";
            this.nickname = "一丝丝纯真";
            this.imageId = "http://img0.imgtn.bdimg.com/it/u=925844297,3903738581&fm=26&gp=0.jpg";
            this.avatarId = R.mipmap.image_avatar_4;
        }};
        Model model4 = new Model() {{
            this.type = 1;
            this.name = "盐菜饭";
            this.nickname = "等着你回来";
            this.imageId = "http://img1.imgtn.bdimg.com/it/u=1975208270,2539296753&fm=26&gp=0.jpg";
            this.avatarId = R.mipmap.image_avatar_5;
        }};
        Model model5 = new Model() {{
            this.type = 2;
            this.name = "米豆腐";
            this.nickname = "宝宝树人";
            this.imageId = "http://img1.imgtn.bdimg.com/it/u=3277523591,4184176422&fm=26&gp=0.jpg";
            this.avatarId = R.mipmap.image_avatar_6;
        }};

        list.add(model);
        list.add(model1);
        list.add(model2);
        list.add(model3);
        list.add(model4);
        list.add(model5);

        return list;
    }

    public class MyAdapter extends BaseQuickAdapter<Model, BaseViewHolder> {

        public MyAdapter(@Nullable List<Model> data) {
            super(data);
            setMultiTypeDelegate(new MultiTypeDelegate<Model>() {
                @Override
                protected int getItemType(Model model) {
                    return model.type;
                }
            });

            getMultiTypeDelegate()
                    .registerItemType(1, R.layout.refresh_item)
                    .registerItemType(2, R.layout.refresh_item2);
        }

        @Override
        protected void convert(BaseViewHolder helper, Model item) {

            switch (helper.getItemViewType()) {
                case 1:
                    helper.setText(R.id.name, item.getName());
                    helper.setText(R.id.nickname, item.getNickname());
                    helper.setImageResource(R.id.avatar, item.getAvatarId());
                    ImageView image = helper.getView(R.id.image);
                    ImageLoaderUtils.loadingImg(mContext, image, item.getImageId());

                    break;
                case 2:
                    helper.setText(R.id.name, item.getName());
                    helper.setText(R.id.nickname, item.getNickname());
                    helper.setImageResource(R.id.avatar, item.getAvatarId());
                    ImageView image2 = helper.getView(R.id.image);
                    ImageLoaderUtils.loadingImg(mContext, image2, item.getImageId());
                    break;

            }


        }
    }


}
