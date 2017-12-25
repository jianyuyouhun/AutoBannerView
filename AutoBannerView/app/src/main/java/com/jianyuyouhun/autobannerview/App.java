package com.jianyuyouhun.autobannerview;

import com.jianyuyouhun.autobannerview.mvp.ImgModel;
import com.jianyuyouhun.jmvplib.app.JApp;
import com.jianyuyouhun.jmvplib.mvp.BaseJModelImpl;
import com.jianyuyouhun.jmvplib.utils.http.JHttpFactory;
import com.jianyuyouhun.jmvplib.utils.imageloader.ImageLoader;

import java.util.List;

/**
 * Created by Jianyuyouhun on 2016/12/26.
 */

public class App extends JApp {

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    protected void initDependencies() {
        JHttpFactory.init();
        ImageLoader.getInstance().init(this);
    }

    @Override
    protected void initModels(List<BaseJModelImpl> models) {

    }
}
