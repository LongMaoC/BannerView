package com.cxy.library.iutils;

import android.widget.ImageView;

import com.cxy.library.entity.BannerEntity;

import java.util.List;

/**
 * Created by CXY on 2016/6/16.
 */
public  interface IBannerBaseAdapter {

     void loadImage(String url, ImageView imageView);

     int setItemCount();

    List<BannerEntity> setData();

    void onItemClick(int position);

}
