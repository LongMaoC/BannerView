package com.cxy.library.adapter;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;

import java.util.List;

/**
 * Created by CXY on 2016/6/28.
 */
public class ViewPagerAdapter extends PagerAdapter {
    List<ImageView> imageViewList;
    public ViewPagerAdapter(List<ImageView> imageViewList) {
        this.imageViewList= imageViewList;
    }

    public void ViewPagerAdapter(){

    }
    // 获取数据的个数
    public int getCount() {
        return imageViewList.size();
    }

    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;
    }

    public void destroyItem(View arg0, int position, Object object) {
        ((ViewPager) arg0).removeView((View) object);
    }

    public Object instantiateItem(View arg0, int position) {
        ((ViewPager) arg0).addView(imageViewList.get(position));
        return imageViewList.get(position);
    }
}
