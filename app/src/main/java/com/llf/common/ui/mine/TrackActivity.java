package com.llf.common.ui.mine;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.llf.basemodel.base.BaseActivity;
import com.llf.basemodel.commonactivity.WebViewActivity;
import com.llf.basemodel.commonwidget.EmptyLayout;
import com.llf.basemodel.recycleview.DefaultItemDecoration;
import com.llf.common.App;
import com.llf.common.R;
import com.llf.common.WelcomeActivity;
import com.llf.common.entity.JcodeEntity;
import com.llf.common.ui.girl.adapter.GirlAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by llf on 2017/3/31.
 * 影迹
 */

public class TrackActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener {
    private static final String TAG = "TrackActivity";

    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @Bind(R.id.refreshLayout)
    SwipeRefreshLayout mRefreshLayout;
    @Bind(R.id.emptyLayout)
    EmptyLayout mEmptyLayout;

    private GirlAdapter mAdapter;
    private List<JcodeEntity> jcodes = new ArrayList<>();

    private int position;
    private static final String HOST = "http://www.jcodecraeer.com";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //是被强杀的
        if (App.mAppStatus == -1) {
            startActivity(WelcomeActivity.class);
        }
        mEmptyLayout.showSuccess();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_track;
    }

    @Override
    protected void initView() {
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        mRefreshLayout.setColorSchemeResources(R.color.colorPrimary, android.R.color.holo_red_light, android.R.color.holo_orange_light, android.R.color.holo_green_light);
        mRefreshLayout.setOnRefreshListener(this);
        LinearLayoutManager manager = new LinearLayoutManager(TrackActivity.this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addItemDecoration(new DefaultItemDecoration(TrackActivity.this));
        mAdapter = new GirlAdapter(jcodes, this);
        mAdapter.setOnItemClickLitener(new GirlAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                WebViewActivity.lanuch(TrackActivity.this, HOST + jcodes.get(position).getDetailUrl());
            }
        });

        mRecyclerView.setAdapter(mAdapter);
        mRefreshLayout.setRefreshing(false);

    }

    @OnClick(R.id.floatBtn)
    public void onViewClicked() {
        mRecyclerView.smoothScrollToPosition(0);
    }

    @Override
    public void onRefresh() {

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        jcodes.clear();
        jcodes = null;
    }
}
