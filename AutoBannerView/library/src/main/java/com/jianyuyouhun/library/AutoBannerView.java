package com.jianyuyouhun.library;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * AutoBannerView
 * Created by Jianyuyouhun on 2016/12/26.
 */

public class AutoBannerView extends RelativeLayout {

    private static final int AUTO_START = 0;

    private static final int PAGER_MAX_VALUE = 10000;

    private Context mContext;

    /** 存放视图 */
    private ViewPager mViewPager;

    /** 圆点容器 */
    private LinearLayout dotContainer;

    /** 存放圆点 */
    private List<ImageView> mImageViews;

    /** ViewPagerAdapter */
    private BannerPagerAdapter pagerAdapter;

    private OnBannerChangeListener onBannerChangeListener;

    private int mDotResId = R.drawable.ic_health_poit_index_deep;
    private int mDotShadowResId = R.drawable.ic_health_poit_index_shallow;

    /** 轮播状态 */
    private boolean isRunning = false;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case AUTO_START:
                    if (isRunning) {
                        if (mImageViews.size() != 0) {
                            if (mViewPager.getCurrentItem() == PAGER_MAX_VALUE - 1) {
                                mViewPager.setCurrentItem(0);
                            } else {
                                mViewPager.setCurrentItem((mViewPager.getCurrentItem() + 1));
                            }
                            mHandler.sendEmptyMessageDelayed(AUTO_START, mWaitMillisecond);
                        }
                    }
                    break;
            }
        }
    };

    /** 轮播间隔 */
    private int mWaitMillisecond = 3000;

    public AutoBannerView(Context context){
        this(context, null, 0);
    }
    public AutoBannerView(Context context, AttributeSet attrs){
        this(context, attrs, 0);
    }
    public AutoBannerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context){
        mContext = context;
        View view = LayoutInflater.from(mContext).inflate(R.layout.view_auto_banner, this, false);
        mViewPager = (ViewPager) view.findViewById(R.id.viewPager);
        dotContainer = (LinearLayout) view.findViewById(R.id.dotContainer);
        mImageViews = new ArrayList<>();
        mViewPager.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_UP:
                        // 开始图片滚动
                        startImageTimerTask();
                        break;
                    default:
                        // 停止图片滚动
                        stopImageTimerTask();
                        break;
                }
                return false;
            }
        });
        mViewPager.setOnPageChangeListener(new AutoBannerChangeListener());
        pagerAdapter = new BannerPagerAdapter();
        mViewPager.setAdapter(pagerAdapter);
        this.addView(view);
    }

    /**
     * 开始轮播
     */
    public void startImageTimerTask() {
        if (mImageViews.size() > 1) {
            isRunning = true;
            mHandler.removeMessages(AUTO_START);
            mHandler.sendEmptyMessageDelayed(AUTO_START, mWaitMillisecond);
        }
    }

    /**
     * 停止轮播
     */
    public void stopImageTimerTask() {
        mHandler.removeMessages(AUTO_START);
        isRunning = false;
    }

    /**
     * 设置适配器
     * @param adapter AutoBannerAdapter
     */
    public void setAdapter(@NonNull AutoBannerAdapter adapter) {
        stopImageTimerTask();
        this.dotContainer.removeAllViews();
        this.mImageViews.clear();
        this.autoBannerAdapter = adapter;
        int count = autoBannerAdapter.getCount();
        if (count == 0) {
            return;
        }
        for (int i = 0; i < count; i++) {
            ImageView dotImage = new ImageView(mContext);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.leftMargin = 30;
            dotImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
            dotImage.setLayoutParams(params);
            if (i == 0) {
                dotImage.setBackgroundResource(mDotResId);
            } else {
                dotImage.setBackgroundResource(mDotShadowResId);
            }
            mImageViews.add(dotImage);
            dotContainer.addView(dotImage);
        }
        int offset = (PAGER_MAX_VALUE / 2) % count;//计算偏移量
        pagerAdapter.notifyDataSetChanged();
        mViewPager.setCurrentItem(PAGER_MAX_VALUE / 2 - offset, false);//从Integer.MAX_VALUE的中间开始加载，确保左滑右滑都能ok
        if (count > 1) {
            startImageTimerTask();
        }
    }

    /**
     * 设置圆点样式
     * @param selectedId    选中状态
     * @param unSelectedId  未选中状态
     */
    public void setDotStateId(int selectedId, int unSelectedId) {
        this.mDotResId = selectedId;
        this.mDotShadowResId = unSelectedId;
    }

    /**
     * 设置等待时间间隔（毫秒）
     * @param milliSecond 等待时间
     */
    public void setWaitMilliSceond(int milliSecond) {
        this.mWaitMillisecond = milliSecond;
    }

    /**
     * 设置轮播图内容切换监听
     * @param onBannerChangeListener OnBannerChangeListener
     */
    public void setOnBannerChangeListener(OnBannerChangeListener onBannerChangeListener) {
        this.onBannerChangeListener = onBannerChangeListener;
    }

    private AutoBannerAdapter autoBannerAdapter;

    /**
     * 轮播布局适配器接口
     */
    public interface AutoBannerAdapter {
        int getCount();
        View getView(View convertView, int position);
    }

    /**
     * banner改变时的回调
     */
    public interface OnBannerChangeListener {
        void onCurrentItemChanged(int position);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        isRunning = false;
    }

    private class AutoBannerChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            if (mImageViews.size() == 0) {
                return;
            }
            int pos = position % mImageViews.size();
            mImageViews.get(pos).setBackgroundResource(mDotResId);
            for (int i = 0; i < mImageViews.size(); i++) {
                if (pos != i) {
                    mImageViews.get(i).setBackgroundResource(mDotShadowResId);
                }
            }
            if (onBannerChangeListener != null) {
                onBannerChangeListener.onCurrentItemChanged(pos);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            if (state == ViewPager.SCROLL_STATE_IDLE)
                startImageTimerTask();
        }
    }

    private class BannerPagerAdapter extends PagerAdapter {

        LinkedList<View> cacheList = new LinkedList<>();//缓存机制

        @Override
        public int getCount() {
            return PAGER_MAX_VALUE;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View view = (View) object;
            container.removeView(view);
            cacheList.push(view);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            if (autoBannerAdapter == null || autoBannerAdapter.getCount() == 0) {
                return null;
            }
            int offset = position % autoBannerAdapter.getCount();
            View view;
            if (cacheList.size() == 0) {
                view = autoBannerAdapter.getView(null, offset);
            } else {
                //poll为删除list最后一个实体并取出,peek则是不删除list中对应的实体
                view = autoBannerAdapter.getView(cacheList.pollLast(), offset);
            }
            container.addView(view);
            return view;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }

}
