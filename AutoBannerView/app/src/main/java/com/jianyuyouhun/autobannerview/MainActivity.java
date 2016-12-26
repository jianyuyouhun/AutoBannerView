package com.jianyuyouhun.autobannerview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.jianyuyouhun.library.AutoBannerView;

import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private AutoBannerView autoBannerView;
    private MyAutoBannerAdapter autoBannerAdapter;
    private List<String> list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        autoBannerView = (AutoBannerView) findViewById(R.id.autoBannerView);
        autoBannerView.setDotGravity(AutoBannerView.DotGravity.LEFT);
        autoBannerView.setOnBannerChangeListener(new AutoBannerView.OnBannerChangeListener() {
            @Override
            public void onCurrentItemChanged(int position) {

            }
        });
        list = new ArrayList<>();
        autoBannerAdapter = new MyAutoBannerAdapter(getApplicationContext());
        test();
        autoBannerAdapter.changeItems(list);
        autoBannerView.setAdapter(autoBannerAdapter);
    }

    public void test() {
        list.add("http://img3.imgtn.bdimg.com/it/u=1749061261,2462112140&fm=21&gp=0.jpg");
        list.add("http://img5.imgtn.bdimg.com/it/u=1949438216,3782070973&fm=23&gp=0.jpg");
        list.add("http://imgs.91danji.com/Upload/201419/2014191037351347278.jpg");
        list.add("http://img.newyx.net/newspic/image/201410/14/90ea74d5b0.jpg");
    }

    private class MyAutoBannerAdapter implements AutoBannerView.AutoBannerAdapter {
        List<String> urls;
        Context context;
        public MyAutoBannerAdapter(Context context) {
            this.context = context;
            this.urls = new ArrayList<>();
        }

        public void changeItems(@NonNull List<String> urls) {
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
            x.image().bind(imageView, urls.get(position));
            return imageView;
        }
    }
}
