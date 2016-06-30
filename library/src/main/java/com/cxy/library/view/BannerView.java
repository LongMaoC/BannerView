package com.cxy.library.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.cxy.library.R;
import com.cxy.library.adapter.ViewPagerAdapter;
import com.cxy.library.iutils.IBannerBaseAdapter;
import com.cxy.library.iutils.IBannerView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by CXY on 2016/6/16.
 */
public class BannerView extends FrameLayout implements IBannerView, View.OnTouchListener {
    private Context context;

    private final ViewPager vp_viewPage;
    private final View view_select_point;
    private final LinearLayout ll_point_group;
    private final RelativeLayout rl_point_group;

    private float indicator_border_distance = 15.0f;//边界距离(边距)
    private float indicator_bottom_distance = 2.0f;//边界距离(底边)
    private int radii = 17;//半径
    private int pointSpacing;
    private static int loop_time = 2000;//图片循环时间
    private static int scroll_time = 2200;//viewpage滚动时间
    private int indicator_positions = -1; //选择器的位置
    private int indicator_color_no_select = Color.BLACK;//未选择的颜色
    private int indicator_color_select = Color.WHITE;//选择的颜色

    private int[] positions = new int[]{0, 1, 2};
    private int mPointLeftAndRight;
    private List<ImageView> imageViewList = new ArrayList<>();
    private int currentItem = 0;// 当前的索引
    private static boolean isAutoPlay = true;//自动轮播启用开关


    public BannerView(Context context) {
        this(context, null);
    }

    public BannerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BannerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.BannerView, defStyleAttr, 0);
        int n = a.getIndexCount();
        for (int i = 0; i < n; i++) {
            int attr = a.getIndex(i);
            if (attr == R.styleable.BannerView_indicator_color_select) {
                indicator_color_select = a.getColor(attr, Color.WHITE);
            } else if (attr == R.styleable.BannerView_indicator_color_no_select) {
                indicator_color_no_select = a.getColor(attr, Color.BLACK);
            } else if (attr == R.styleable.BannerView_indicator_positions) {
                indicator_positions = a.getInt(attr, positions[1]);
            } else if (attr == R.styleable.BannerView_indicator_radii) {
                radii = (int) a.getDimension(attr, 8);
            } else if (attr == R.styleable.BannerView_loop_time) {
                loop_time = a.getInt(attr, 1000);
            } else if (attr == R.styleable.BannerView_scroll_time) {
                scroll_time = a.getInt(attr, 2200);
            } else if (attr == R.styleable.BannerView_indicator_border_distance) {
                indicator_border_distance = a.getDimension(attr, 15.0f);
            } else if (attr == R.styleable.BannerView_indicator_bottom_distance) {
                indicator_bottom_distance = a.getDimension(attr, 2.0f);
            }
        }
        a.recycle();

        initView();

        View.inflate(context, R.layout.include_fragement_loop_photos, this);

        vp_viewPage = (ViewPager) findViewById(R.id.vp_viewPage);
        view_select_point = (View) findViewById(R.id.view_select_point);
        ll_point_group = (LinearLayout) findViewById(R.id.ll_point_group);
        rl_point_group = (RelativeLayout) findViewById(R.id.rl_point_group);
        this.context = context;

        ViewPagerScroller viewPagerScroller = new ViewPagerScroller(context, scroll_time);
        viewPagerScroller.initViewPagerScroll(vp_viewPage);
    }

    private void initView() {
        if (indicator_positions == -1) {
            indicator_positions = positions[1];
        }
        mPointLeftAndRight = (int) (radii * 0.75);//园左右两边的距离
    }

    @Override
    public void setAdapter(final IBannerBaseAdapter adapter) {
        int itemCount = adapter.setItemCount();
        for (int i = 0; i < itemCount; i++) {
            ImageView iv = getDefaultImageView();
            adapter.loadImage(adapter.setData().get(i).getUrl(), iv);
            iv.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    adapter.onItemClick(currentItem);
                }
            });
            imageViewList.add(iv);
        }

        ll_point_group.removeAllViews();
        for (int i = 0; i < itemCount; i++) {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(radii, radii);
            if (i > 0) {
                params.leftMargin = mPointLeftAndRight;
            }
            View vIndicatorSelect = new View(context);
            vIndicatorSelect.setBackgroundResource(R.drawable.shape_point);
            GradientDrawable bgShape = (GradientDrawable) vIndicatorSelect.getBackground();
            bgShape.setColor(indicator_color_no_select);
            vIndicatorSelect.setLayoutParams(params);
            ll_point_group.addView(vIndicatorSelect);
        }
        this.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    // 当layout执行结束后回调此方法
                    @Override
                    public void onGlobalLayout() {
                        ll_point_group.getViewTreeObserver()
                                .removeGlobalOnLayoutListener(this);
                        if (ll_point_group.getChildCount() > 1) {
                            pointSpacing = ll_point_group.getChildAt(1).getLeft() - ll_point_group.getChildAt(0).getLeft();
                        }
                        setRelativeLayoutProperty();
                    }
                });

        vp_viewPage.setAdapter(new ViewPagerAdapter(imageViewList));
        vp_viewPage.setOnPageChangeListener(new GuidePageListener());
        vp_viewPage.setOnTouchListener(this);
        new LoopThread().start();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                isAutoPlay = false;
                break;
            case MotionEvent.ACTION_MOVE:
                isAutoPlay = false;
                break;
            case MotionEvent.ACTION_UP:
                isAutoPlay = true;
                break;
        }
        return false;
    }

    private void setRelativeLayoutProperty() {
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) rl_point_group.getLayoutParams();
        rl_point_group.setPadding(rl_point_group.getPaddingLeft(), rl_point_group.getPaddingTop(),
                rl_point_group.getPaddingRight(), (int) indicator_bottom_distance);

        if (indicator_positions == positions[0]) {
            params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            rl_point_group.setPadding((int) indicator_border_distance, rl_point_group.getPaddingTop(),
                    rl_point_group.getPaddingRight(), rl_point_group.getPaddingBottom());
        } else if (indicator_positions == positions[1]) {
            params.addRule(RelativeLayout.CENTER_HORIZONTAL);
        } else if (indicator_positions == positions[2]) {
            params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            rl_point_group.setPadding(rl_point_group.getPaddingLeft(), rl_point_group.getPaddingTop(),
                    (int) indicator_border_distance, rl_point_group.getPaddingBottom());
        }

        rl_point_group.setLayoutParams(params);
        invalidate();
    }

    class LoopThread extends Thread {
        @Override
        public void run() {
            while (true) {
                try {
                    Thread.sleep(loop_time);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (isAutoPlay) {
                    currentItem++;
                    if (currentItem == imageViewList.size()) {
                        currentItem = 0;
                    }
                    handler.sendEmptyMessage(0x123);
                }
            }
        }
    }

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == 0x123) {
                vp_viewPage.setCurrentItem(currentItem);
            }
        }
    };

    public ImageView getDefaultImageView() {
        ImageView iv = new ImageView(context);
        iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
        return iv;
    }

    /**
     * viewpager的滑动监听
     *
     * @author Kevin
     */
    class GuidePageListener implements ViewPager.OnPageChangeListener {

        // 滑动事件
        @Override
        public void onPageScrolled(int position, float positionOffset,
                                   int positionOffsetPixels) {
            int len = (int) (pointSpacing * positionOffset) + position * pointSpacing;
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(radii, radii);
            params.leftMargin = len;// 设置左边距
            view_select_point.setLayoutParams(params);// 重新给小红点设置布局参数
            view_select_point.setBackgroundColor(indicator_color_select);
            view_select_point.setBackgroundResource(R.drawable.shape_point);
            GradientDrawable bgShape = (GradientDrawable) view_select_point.getBackground();
            bgShape.setColor(indicator_color_select);
        }

        @Override
        public void onPageSelected(int position) {
            currentItem = position;
        }

        @Override
        public void onPageScrollStateChanged(int state) {}
    }
}
