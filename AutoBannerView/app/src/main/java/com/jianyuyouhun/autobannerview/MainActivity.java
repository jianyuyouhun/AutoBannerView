package com.jianyuyouhun.autobannerview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jianyuyouhun.library.AutoBannerView;

import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private AutoBannerView autoBannerView;
    private MyAutoBannerAdapter autoBannerAdapter;
    private TextView title;
    private List<BannerInfo> list;

    private AutoBannerView.OnBannerChangeListener onBannerChangeListener = new AutoBannerView.OnBannerChangeListener() {
        @Override
        public void onCurrentItemChanged(int position) {
            title.setText(list.get(position).getName());
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initData();
    }

    private void initView() {
        autoBannerView = (AutoBannerView) findViewById(R.id.autoBannerView);
        title = (TextView) findViewById(R.id.bannerTitle);
        autoBannerView.setDotGravity(AutoBannerView.DotGravity.RIGHT);
        autoBannerView.setWaitMilliSceond(3000);
        autoBannerView.setDotMargin(4);
        autoBannerView.setOnBannerChangeListener(onBannerChangeListener);
    }

    private void initData() {
        autoBannerAdapter = new MyAutoBannerAdapter(getApplicationContext());
        list = new ArrayList<>();
        test();
        autoBannerAdapter.changeItems(list);
        autoBannerView.setAdapter(autoBannerAdapter);
    }

    public void test() {
        BannerInfo info1 = new BannerInfo();
        info1.setUrl("http://img3.imgtn.bdimg.com/it/u=1749061261,2462112140&fm=21&gp=0.jpg");
        info1.setName("寒冰射手");
        list.add(info1);
        BannerInfo info2 = new BannerInfo();
        info2.setUrl("http://img5.imgtn.bdimg.com/it/u=1949438216,3782070973&fm=23&gp=0.jpg");
        info2.setName("齐天大圣");
        list.add(info2);
        BannerInfo info3 = new BannerInfo();
        info3.setUrl("http://imgs.91danji.com/Upload/201419/2014191037351347278.jpg");
        info3.setName("放逐之刃");
        list.add(info3);
        BannerInfo info4 = new BannerInfo();
        info4.setUrl("http://img.newyx.net/newspic/image/201410/14/90ea74d5b0.jpg");
        info4.setName("好多好多");
        list.add(info4);
    }

    private class MyAutoBannerAdapter implements AutoBannerView.AutoBannerAdapter {
        List<BannerInfo> urls;
        Context context;
        public MyAutoBannerAdapter(Context context) {
            this.context = context;
            this.urls = new ArrayList<>();
        }

        public void changeItems(@NonNull List<BannerInfo> urls) {
            this.urls.clear();
            this.urls.addAll(urls);
        }

        @Override
        public int getCount() {
            return urls.size();
        }

        @Override
        public View getView(View convertView, int position) {
            ImageView imageView;
            if (convertView == null) {
                imageView = new ImageView(context);
            } else {
                imageView = (ImageView) convertView;
            }
            x.image().bind(imageView, urls.get(position).getUrl());
            return imageView;
        }
    }

}
