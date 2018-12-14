## READ ME ##

### 原理和思路 ###

  [在这里O(∩_∩)O在这里](https://jianyuyouhun.com/index.php/archives/60/)

### 一个Demo ###

这个效果图是修改过的，[代码走这里](doc/OnBannerChange.md)

<img src="https://jianyuyouhun.com/imgs/autobannergif.gif" />

### 使用方法 ###

1. Add it in your root build.gradle at the end of repositories:
	
		allprojects {
		
			repositories {
				...
				maven { url 'https://jitpack.io' }
			}
		}

2. Add the dependency

		dependencies {
	        compile 'com.github.jianyuyouhun:autobannerview:0.2.3',{
				exclude group: 'com.android.support'
			}
		}

使用方法
	
	<com.jianyuyouhun.library.AutoBannerView
        android:id="@+id/autoBannerView"
        android:layout_width="match_parent"
        android:layout_height="200dp"
        app:dotGravity="right"
        app:waitMilliSecond="2000"
        app:dotMargin="4dp"/>

xml中

        autoBannerView = (AutoBannerView) findViewById(R.id.autoBannerView);
        autoBannerView.setWaitMilliSceond(3000);
		autoBannerView.setDotGravity(AutoBannerView.DotGravity.LEFT);
        autoBannerView.setOnBannerChangeListener(new AutoBannerView.OnBannerChangeListener() {
            @Override
               public void onCurrentItemChanged(int position) {

            }
        });
        autoBannerAdapter = new MyAutoBannerAdapter(getApplicationContext());
        autoBannerView.setAdapter(autoBannerAdapter);

		//获取到数据之后调用adapter.notifyDataSetChanged,这里由于封装在adapter里的changeItems了，所以没有调用。
        list = new ArrayList<>();
        autoBannerAdapter.changeItems(list);

代码中

当数据改变的时候调用autoBannerAdapter.notifyDataSetChanged

　　MyAutoBannerAdapter是自己实现的适配器，继承了动态类AutoBannerAdapter，网络图片异步加载的话去百度吧。这里我使用了自己实现的imageLoader，其集成在了[JMVP](https://github.com/jianyuyouhun/JMVP)里面

	
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

### 版本变化 ###


	v 0.2.0
	修复已知bug，AutoBannerAdapter改为一个class而不是interface，提供了notifyDataSetChanged方法变更数据

历史版本

	v 0.1.0 
    第一次提交，实现基本功能
　
	
	v 0.1.1
    增加xml中配置圆点间距，圆点布局gravity和轮播间隔
    
　

	v 0.1.4
	修复bug，只有一张图时不自动轮播，增加了源码打包

### 新的Demo ###

　　源码中包含了一个新的Demo，使用了一些自己写的框架，有兴趣的人可以看下
　

