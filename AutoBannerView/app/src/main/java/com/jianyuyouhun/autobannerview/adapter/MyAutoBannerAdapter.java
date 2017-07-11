package com.jianyuyouhun.autobannerview.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;

import com.jianyuyouhun.autobannerview.entity.BannerInfo;
import com.jianyuyouhun.jmvplib.mvp.model.SdcardModel;
import com.jianyuyouhun.jmvplib.utils.imageloader.ImageLoadListener;
import com.jianyuyouhun.jmvplib.utils.imageloader.ImageLoadOptions;
import com.jianyuyouhun.jmvplib.utils.imageloader.ImageLoader;
import com.jianyuyouhun.jmvplib.utils.injecter.model.Model;
import com.jianyuyouhun.jmvplib.utils.injecter.model.ModelInjector;
import com.jianyuyouhun.library.AutoBannerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangyu on 2017/3/17.
 */

public class MyAutoBannerAdapter extends AutoBannerAdapter {
    private ImageLoader imageLoader = ImageLoader.getInstance();
    List<BannerInfo> urls;
    Context context;
    @Model
    private SdcardModel mSdcardModel;
    private OnBannerClickListener onBannerClickListener;

    public MyAutoBannerAdapter(Context context) {
        this.context = context;
        this.urls = new ArrayList<>();
        ModelInjector.injectModel(this);
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
    public View getView(View convertView, final int position) {
        ImageView imageView = null;
        if (convertView == null) {
            imageView = new ImageView(context);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        } else {
            imageView = (ImageView) convertView;
        }
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onBannerClickListener == null) return;
                onBannerClickListener.onClick(urls.get(position));
            }
        });
        final String url = urls.get(position).getUrl();
        imageView.setTag(url);
        ImageLoadOptions.Builder builder = new ImageLoadOptions.Builder(mSdcardModel.getImgPath(url));
        builder.setUrl(url);
        builder.bindImageView(imageView);
        final ImageView finalImageView = imageView;
        imageLoader.loadImage(builder.build(), new ImageLoadListener.SimpleImageLoadListener(){
            @Override
            public void onLoadFailed(String reason) {
                super.onLoadFailed(reason);
            }

            @Override
            public void onLoadSuccessful(BitmapSource bitmapSource, Bitmap bitmap) {
                super.onLoadSuccessful(bitmapSource, bitmap);
                if (finalImageView.getTag().toString().equals(url)) {
                    finalImageView.setImageBitmap(bitmap);
                }
            }
        });
        return finalImageView;
    }

    public void setOnBannerClickListener(OnBannerClickListener onBannerClickListener) {
        this.onBannerClickListener = onBannerClickListener;
    }

    public interface OnBannerClickListener {
        void onClick(BannerInfo info);
    }
}