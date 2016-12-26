package com.jianyuyouhun.autobannerview;

import android.app.Application;

import org.xutils.x;

/**
 * Created by Jianyuyouhun on 2016/12/26.
 */

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        x.Ext.init(this);
    }
}
