package com.jianyuyouhun.autobannerview.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;

import com.jianyuyouhun.autobannerview.entity.BannerInfo;
import com.jianyuyouhun.library.AutoBannerAdapter;
import com.jianyuyouhun.library.AutoBannerView;

import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangyu on 2017/3/17.
 */

public class MyAutoBannerAdapter extends AutoBannerAdapter {
    List<BannerInfo> urls;
    Context context;
    public MyAutoBannerAdapter(Context context) {
        this.context = context;
        this.urls = new ArrayList<>();
    }

    public void changeItems(@NonNull List<BannerInfo> urls) {
        this.urls.clear();
        this.urls.addAll(urls);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return urls.size();
    }

    @Override
    protected Object getItem(int position) {
        return urls.get(position);
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