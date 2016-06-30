package com.cxy.bannerview;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.Toast;

import com.cxy.library.entity.BannerEntity;
import com.cxy.library.iutils.IBannerBaseAdapter;
import com.cxy.library.view.BannerView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BannerView bannerView = (BannerView) findViewById(R.id.id_bannerView);
        context =this;
        List<BannerEntity> entities = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            BannerEntity loopPhotoEntity = new BannerEntity();
            loopPhotoEntity.setUrl("http://f.hiphotos.baidu.com/image/h%3D360/sign=0ec20d71f01fbe09035ec5125b600c30/00e93901213fb80e0ee553d034d12f2eb9389484.jpg");
            entities.add(loopPhotoEntity);
        }

        bannerView.setAdapter(new LoopPhotoAdapter(context, entities));
    }
    class LoopPhotoAdapter implements IBannerBaseAdapter {
        Context context;
        List<BannerEntity> data;

        public LoopPhotoAdapter(Context context, List<BannerEntity> entities) {//构造方法
            this.context = context;
            data= entities;
        }
        @Override
        public void loadImage(String url,ImageView imageView) {//加载图片
            Picasso.with(context).load(url).into(imageView);
        }
        @Override
        public int setItemCount() {//数据个数
            return data.size();
        }
        @Override
        public List<BannerEntity> setData() {//设置数据
            return data;
        }
        @Override
        public void onItemClick(int position) {//点击事件
            Toast.makeText(context, "position = " + position, Toast.LENGTH_SHORT).show();
        }
    }
}
