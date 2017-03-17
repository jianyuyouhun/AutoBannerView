package com.jianyuyouhun.autobannerview.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;

import com.jianyuyouhun.autobannerview.entity.BannerInfo;
import com.jianyuyouhun.library.AutoBannerView;

import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangyu on 2017/3/17.
 */

public class MyAutoBannerAdapter  implements AutoBannerView.AutoBannerAdapter {
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