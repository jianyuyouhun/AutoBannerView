## READ ME ##

### 原理和思路 ###

  [在这里O(∩_∩)O在这里](https://jianyuyouhun.com/index.php/archives/60/)

### 一个Demo ###

这个效果图是修改过的，[代码走这里](doc/OnBannerChange.md)

看不到图或者不全的话,[点这里试试](http://occ9eufqe.bkt.clouddn.com/autobannergif.gif)

<img src="http://occ9eufqe.bkt.clouddn.com/autobannergif.gif" />

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
	        compile 'com.github.jianyuyouhun:AutoBannerView:0.1.1',{
				exclude group: 'com.android.support'
			}
		}

   暂时需要各位手动消除support包冲突，下个版本再解决这个问题

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
        list = new ArrayList<>();
        autoBannerAdapter = new MyAutoBannerAdapter(getApplicationContext());
        autoBannerAdapter.changeItems(list);
        autoBannerView.setAdapter(autoBannerAdapter);

代码中

当数据改变的时候调用autoBannerAdapter.changeItems()方法，然后还需要调用autoBannerView.setAdapter(autoBannerAdapter);

　　MyAutoBannerAdapter是自己实现的适配器，网络图片异步加载的话去百度吧。给你一个使用xutils的简单实现

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

还得感谢一下xutils框架呢,[链接在这里](https://github.com/wyouflf/xUtils3)



### 版本变化 ###

1. v 0.1.0 
   
    第一次提交，实现基本功能

2. v 0.1.1

    增加xml中配置圆点间距，圆点布局gravity和轮播间隔