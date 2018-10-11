package com.fete.basemodel.bar;

import android.app.Activity;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fete.basemodel.R;
import com.gyf.barlibrary.ImmersionBar;
import com.scwang.smartrefresh.layout.util.DensityUtil;

import static android.view.View.GONE;


public class BaseToolbarHelper {

/*

<include layout="@layout/title_commom_layout" />

private void initToolBar() {
        ViewGroup viewById = (ViewGroup) findViewById(android.R.id.content);
        ToolbarHelper.getInstance()
                .initToolbarHelper(viewById, this)
                .setCenterText("测试")
                .setRightText("设置", new ToolbarHelper.ToolBarCallBack() {
                    @Override
                    public void onClick() {
                        startActivity(SettingActivity.class);
                    }
                });


    }

    */

    private static BaseToolbarHelper toolbarHelper = null;

    private BaseToolbarHelper() {
        // TODO Auto-generated constructor stub
    }


    public static BaseToolbarHelper getInstance() {
        if (toolbarHelper == null) {
            synchronized (BaseToolbarHelper.class) {
                if (toolbarHelper == null) {
                    toolbarHelper = new BaseToolbarHelper();
                }
            }
        }
        return toolbarHelper;
    }


    ImageView leftImageBack;
    ImageView leftImageClose;
    TextView centerText;
    ImageView rightImage;
    TextView rightText;
    RelativeLayout rlTitle;
    LinearLayout llTitle;


    public BaseToolbarHelper initToolbarHelper(ViewGroup rootView, final Activity context) {
        leftImageBack = rootView.findViewById(R.id.left_image_back);
        leftImageClose = rootView.findViewById(R.id.left_image_close);
        centerText = rootView.findViewById(R.id.center_text);
        rightImage = rootView.findViewById(R.id.right_image);
        rightText = rootView.findViewById(R.id.right_text);
        rlTitle = rootView.findViewById(R.id.rl_title);
        llTitle = rootView.findViewById(R.id.ll_title);
        leftImageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.finish();
            }
        });

        return toolbarHelper;

    }

    public BaseToolbarHelper initWebviewToolbarHelper(ViewGroup rootView, final Activity context, final ToolBarCallBack toolBarCallBack) {
        leftImageBack = rootView.findViewById(R.id.left_image_back);
        leftImageClose = rootView.findViewById(R.id.left_image_close);
        centerText = rootView.findViewById(R.id.center_text);
        rightImage = rootView.findViewById(R.id.right_image);
        rightText = rootView.findViewById(R.id.right_text);
        rlTitle = rootView.findViewById(R.id.rl_title);
        leftImageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toolBarCallBack.onClick();
            }
        });
        leftImageClose.setVisibility(View.VISIBLE);
        leftImageClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.finish();
            }
        });
        return toolbarHelper;
    }


    public BaseToolbarHelper setToolBarHide(boolean isHide) {
        if (isHide) {
            rlTitle.setVisibility(View.GONE);
        } else {
            rlTitle.setVisibility(View.VISIBLE);
        }
        return toolbarHelper;
    }

    public BaseToolbarHelper setToolBarAlpha(int alpha) {
        rlTitle.setAlpha(alpha);
        return toolbarHelper;
    }


    public BaseToolbarHelper setRightImage(int id, final ToolBarCallBack toolBarCallBack) {
        if (rightImage != null) {
            rightImage.setImageResource(id);
            rightImage.setVisibility(View.VISIBLE);
            rightImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    toolBarCallBack.onClick();
                }
            });
        }
        return toolbarHelper;
    }

    public BaseToolbarHelper setCenterText(String text) {
        if (centerText != null) {
            centerText.setText(text);
            centerText.setVisibility(View.VISIBLE);
        }
        return toolbarHelper;
    }


    public BaseToolbarHelper setRightText(String text, final ToolBarCallBack toolBarCallBack) {
        if (rightText != null) {
            rightText.setText(text);
            rightText.setVisibility(View.VISIBLE);
            rightText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    toolBarCallBack.onClick();
                }
            });
        }
        return toolbarHelper;
    }


    public interface ToolBarCallBack {
        void onClick();
    }


    private int mScrollY = 0;

    public void initHeader(Activity context, int color) {
        ImmersionBar.with(context)
                .fitsSystemWindows(true)  //使用该属性,必须指定状态栏颜色
                .statusBarColor(color)
                .init();
    }

    /**
     * @param context
     * @param scrollView
     * @param mImmersionBar
     */
    public void initScollViewHeader(final Activity context, NestedScrollView scrollView, final ImmersionBar mImmersionBar) {
        //状态栏透明和间距处理
        StatusBarUtil.immersive(context);
        StatusBarUtil.setPaddingSmart(context, llTitle);

        scrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            private int lastScrollY = 0;
            private int h = DensityUtil.dp2px(170);
            private int color = ContextCompat.getColor(context, R.color.main_color) & 0x00ffffff;

            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                llTitle.setVisibility(View.VISIBLE);

                if (lastScrollY < h) {
                    scrollY = Math.min(h, scrollY);
                    mScrollY = scrollY > h ? h : scrollY;
                    rlTitle.setAlpha(1f * mScrollY / h);
                    llTitle.setBackgroundColor(((255 * mScrollY / h) << 24) | color);
                    mImmersionBar.statusBarColorInt(((255 * mScrollY / h) << 24) | color).init();
                }
                float alpha = rlTitle.getAlpha();
                if (alpha == 0) {
                    llTitle.setVisibility(GONE);
                }
                lastScrollY = scrollY;
            }
        });
        llTitle.setVisibility(View.GONE);
        llTitle.setBackgroundColor(0);


    }


}
