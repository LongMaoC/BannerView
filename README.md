
#BannerView
##BannerView是什么?
一个可以轮播图可以控制它的

* 滑动时间和展示时间
* 指示器颜色，大小，位置
* 可以自定义加载图片的工具
* 手势处理，按下停止滑动，抬起继续滑动



#使用方式
引入依赖
打开 Project root 的 build.gradle，在 repositories 节点添加上 maven { url "https://jitpack.io" }
```gradle
    allprojects {
    repositories {
        jcenter()
        maven { url "https://jitpack.io" }
    }
}
```
打开 app  build.gradle，引入
```gradle
    compile 'com.github.LongMaoC:BannerView:v1.0'
}
```

先写xml（自定义的属性可写可不写）
```xml
    xmlns:app="http://schemas.android.com/apk/res-auto"

    <com.cxy.library.view.BannerView
    android:id="@+id/id_bannerView"
    android:layout_width="match_parent"
    android:layout_height="150dp"
    app:indicator_border_distance="16dp"    指示器的边界距离,当indicator_positions的值不为center时使用
    app:indicator_bottom_distance="2dp"     指示器的底界距离
    app:indicator_color_no_select="#478BAD" 指示器未选中时候的颜色
    app:indicator_color_select="#57B5E1"    指示器选中时的颜色
    app:indicator_radii="8dp"               指示器的半径
    app:indicator_positions="right"         指示器的位置（值:left、center、right）
    app:loop_time="2000"                    图片循环时间
    app:scroll_time="2000"                  banner的滚动时间
    />
        
```

获取数据转成实体类,添加进集合
```java
        List<BannerEntity> entities = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            BannerEntity entity = new BannerEntity();
            entity.setUrl("
                http://f.hiphotos.baidu.com/image/h%3D360/sign=0ec20d71f01fbe09035ec5125b600c30/00e93901213fb80e0ee553d034d12f2eb9389484.jpg
            ");
            entities.add(entity);
        }
```
和listview类似，需要些个适配器
```java
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
```

设置适配器
```java
    BannerView bannerView = (BannerView) findViewById(R.id.id_bannerView);
    bannerView.setAdapter(new LoopPhotoAdapter(context, entities));
```

##有问题反馈
在使用中有任何问题、意见或建议，欢迎反馈给我，可以用以下联系方式跟我交流

* 邮件(xingyu1112@foxmail.com)
* QQ: 1209101049





##关于作者

目前是一个在北京的android求职者 