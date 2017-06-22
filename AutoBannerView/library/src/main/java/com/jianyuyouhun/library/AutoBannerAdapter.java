package com.jianyuyouhun.library;

import android.view.View;

/**
 * 轮播图适配器
 * Created by wangyu on 2017/6/22.
 */

public abstract class AutoBannerAdapter {

    private AutoBannerView.BannerObserver bannerObserver;

    protected abstract int getCount();

    protected abstract Object getItem(int position);

    protected abstract View getView(View convertView, int position);

    /**
     * 设置数据变化监听器
     * @param observer   数据观察者
     */
    void setViewPagerObserver(AutoBannerView.BannerObserver observer) {
        synchronized (this) {
            bannerObserver = observer;
        }
    }

    /**
     * 通知数据变更
     */
    public void notifyDataSetChanged() {
        synchronized (this) {
            if (bannerObserver != null) {
                bannerObserver.onChanged();
            }
        }
    }
}
