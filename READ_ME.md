## READ ME ##

### 原理和思路 ###

  [在这里O(∩_∩)O在这里](https://jianyuyouhun.com/index.php/archives/60/)

### 使用方法 ###

项目clone或者下载到本地，然后将library引用为库，我会尽量加入远程仓库的。

        autoBannerView = (AutoBannerView) findViewById(R.id.autoBannerView);
        autoBannerView.setWaitMilliSceond(3000);
        autoBannerView.setOnBannerChangeListener(new AutoBannerView.OnBannerChangeListener() {
            @Override
               public void onCurrentItemChanged(int position) {

            }
        });
        list = new ArrayList<>();
        autoBannerAdapter = new MyAutoBannerAdapter(getApplicationContext());
        autoBannerAdapter.changeItems(list);
        autoBannerView.setAdapter(autoBannerAdapter);

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