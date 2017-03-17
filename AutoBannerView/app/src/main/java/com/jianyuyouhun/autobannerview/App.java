package com.jianyuyouhun.autobannerview;

import com.jianyuyouhun.autobannerview.mvp.ImgPresenter;
import com.jianyuyouhun.jmvplib.app.JApp;
import com.jianyuyouhun.jmvplib.mvp.BaseJPresenterImpl;

import org.xutils.x;

import java.util.List;

/**
 * Created by Jianyuyouhun on 2016/12/26.
 */

public class App extends JApp {
    @Override
    public void onCreate() {
        super.onCreate();
        x.Ext.init(this);
    }

    @Override
    public void initPresenter(List<BaseJPresenterImpl> list) {
        list.add(new ImgPresenter());
    }
}
