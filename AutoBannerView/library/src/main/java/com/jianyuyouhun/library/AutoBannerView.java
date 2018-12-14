package com.jianyuyouhun.library;

import android.content.Context;
import android.content.res.TypedArray;
import android.database.DataSetObserver;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
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

    /**
     * 存放视图
     */
    private NestScrollViewPager mViewPager;

    /**
     * 圆点容器
     */
    private LinearLayout dotContainer;

    /**
     * 存放圆点
     */
    private List<ImageView> mImageViews;

    /**
     * ViewPagerAdapter
     */
    private BannerPagerAdapter pagerAdapter;

    private AutoBannerAdapter autoBannerAdapter;

    private OnBannerChangeListener onBannerChangeListener;

    /**
     * 圆点间距
     */
    private int dotMargin = 30;

    private int mDotResId = R.drawable.ic_dot_deep;
    private int mDotShadowResId = R.drawable.ic_dot_shallow;

    private int dotVisible;

    /**
     * 轮播状态
     */
    private boolean isRunning = false;

    private DotGravity dotGravity = DotGravity.CENTER;

    private BannerObserver bannerObserver = new BannerObserver();

    private Handler mHandler = new Handler(Looper.getMainLooper()) {
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

    /**
     * 轮播间隔
     */
    private int mWaitMillisecond = 3000;

    public AutoBannerView(Context context) {
        this(context, null, 0);
    }

    public AutoBannerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AutoBannerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttr(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initAttr(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray array = context.getTheme().obtainStyledAttributes(attrs,
                R.styleable.AutoBannerView, defStyleAttr, 0);
        int n = array.getIndexCount();
        for (int i = 0; i < n; i++) {
            int attr = array.getIndex(i);
            if (attr == R.styleable.AutoBannerView_dotGravity) {
                dotGravity = DotGravity.valueOf(array.getInt(attr, 2));
            } else if (attr == R.styleable.AutoBannerView_waitMilliSecond) {
                mWaitMillisecond = array.getInt(attr, 3000);
            } else if (attr == R.styleable.AutoBannerView_dotMargin) {
                dotMargin = array.getDimensionPixelSize(attr, (int) TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP, 8, getResources().getDisplayMetrics()));
            } else if (attr == R.styleable.AutoBannerView_dotVisible) {
                dotVisible = array.getInt(R.styleable.AutoBannerView_dotVisible, View.VISIBLE);
            }
        }
        array.recycle();
    }

    private void initView(Context context) {
        mContext = context;
        View view = LayoutInflater.from(mContext).inflate(R.layout.view_auto_banner, this, false);
        mViewPager = view.findViewById(R.id.viewPager);
        dotContainer = view.findViewById(R.id.dotContainer);
        setDotGravity(dotGravity);
        mImageViews = new ArrayList<>();
        mViewPager.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_UP:
                        // 开始图片滚动
                        performClick();
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
        mViewPager.addOnPageChangeListener(new AutoBannerChangeListener());
        pagerAdapter = new BannerPagerAdapter();
        mViewPager.setAdapter(pagerAdapter);
        dotContainer.setVisibility(dotVisible);
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
     *
     * @param adapter AutoBannerAdapter
     */
    public void setAdapter(@NonNull AutoBannerAdapter adapter) {
        this.autoBannerAdapter = adapter;
        this.autoBannerAdapter.setViewPagerObserver(bannerObserver);
        this.autoBannerAdapter.notifyDataSetChanged();
    }

    /**
     * 显示/隐藏小圆点
     */
    public void setDotVisible(int visible) {
        this.dotVisible = visible;
        dotContainer.setVisibility(dotVisible);
    }

    /**
     * 数据变更
     */
    private void dataSetChanged() {
        stopImageTimerTask();
        this.dotContainer.removeAllViews();
        this.mImageViews.clear();
        int count = autoBannerAdapter.getCount();
        if (count == 0) {
            return;
        }
        for (int i = 0; i < count; i++) {
            ImageView dotImage = new ImageView(mContext);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.leftMargin = dotMargin;
            params.rightMargin = dotMargin;
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
        pagerAdapter = new BannerPagerAdapter();
        mViewPager.setAdapter(pagerAdapter);
        mViewPager.setCurrentItem(PAGER_MAX_VALUE / 2 - offset, false);//从Integer.MAX_VALUE的中间开始加载，确保左滑右滑都能ok
        mViewPager.setScrollAble(count > 1);
        startImageTimerTask();
    }

    /**
     * 设置圆点布局的位置
     *
     * @param gravity gravity
     */
    public void setDotGravity(DotGravity gravity) {
        int dotGravity;
        switch (gravity) {
            case LEFT:
                dotGravity = Gravity.START;
                break;
            case CENTER:
                dotGravity = Gravity.CENTER;
                break;
            case RIGHT:
                dotGravity = Gravity.END;
                break;
            default:
                dotGravity = Gravity.CENTER;
                break;
        }
        dotContainer.setGravity(dotGravity);
        requestLayout();
    }

    /**
     * 设置圆点样式
     *
     * @param selectedId   选中状态
     * @param unSelectedId 未选中状态
     */
    public void setDotStateId(@DrawableRes int selectedId, @DrawableRes int unSelectedId) {
        this.mDotResId = selectedId;
        this.mDotShadowResId = unSelectedId;
    }

    /**
     * 设置圆点间距
     *
     * @param margin 间距
     */
    public void setDotMargin(int margin) {
        this.dotMargin = margin;
        requestLayout();
    }

    /**
     * 设置等待时间间隔（毫秒）
     *
     * @param milliSecond 等待时间
     */
    public void setWaitMilliSecond(int milliSecond) {
        this.mWaitMillisecond = milliSecond;
    }

    /**
     * 设置轮播图内容切换监听
     *
     * @param onBannerChangeListener OnBannerChangeListener
     */
    public void setOnBannerChangeListener(OnBannerChangeListener onBannerChangeListener) {
        this.onBannerChangeListener = onBannerChangeListener;
    }

    /**
     * banner改变时的回调
     */
    public interface OnBannerChangeListener {
        void onPageScrolled(int position, float positionOffset, int positionOffsetPixels);

        void onCurrentItemChanged(int position);

        void onPageScrollStateChanged(int state);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        isRunning = false;
    }

    private class AutoBannerChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            if (mImageViews.size() == 0) {
                return;
            }
            int pos = position % mImageViews.size();
            if (onBannerChangeListener != null)
                onBannerChangeListener.onPageScrolled(pos, positionOffset, positionOffsetPixels);
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
            if (onBannerChangeListener != null)
                onBannerChangeListener.onCurrentItemChanged(pos);
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            if (state == ViewPager.SCROLL_STATE_IDLE)
                startImageTimerTask();
            if (onBannerChangeListener != null)
                onBannerChangeListener.onPageScrollStateChanged(state);
        }
    }

    private class BannerPagerAdapter extends PagerAdapter {

        LinkedList<View> cacheList = new LinkedList<>();//缓存机制

        @Override
        public int getCount() {
            return PAGER_MAX_VALUE;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            View view = (View) object;
            container.removeView(view);
            cacheList.push(view);
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
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
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }
    }

    public enum DotGravity {
        LEFT(1), CENTER(2), RIGHT(3);
        private int value;

        DotGravity(int value) {
            this.value = value;
        }

        public static DotGravity valueOf(int value) {
            switch (value) {
                case 1:
                    return LEFT;
                case 2:
                    return CENTER;
                case 3:
                    return RIGHT;
                default:
                    return CENTER;
            }
        }

        public int getValue() {
            return value;
        }

    }

    public class BannerObserver extends DataSetObserver {
        BannerObserver() {
        }

        @Override
        public void onChanged() {
            dataSetChanged();
        }

        @Override
        public void onInvalidated() {
            dataSetChanged();
        }
    }
}
