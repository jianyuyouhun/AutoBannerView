package com.jianyuyouhun.autobannerview.mvp;

import android.os.Handler;

import com.jianyuyouhun.autobannerview.entity.BannerInfo;
import com.jianyuyouhun.jmvplib.app.JApp;
import com.jianyuyouhun.jmvplib.mvp.BaseJPresenterImpl;
import com.jianyuyouhun.jmvplib.mvp.OnResultListener;

import java.util.List;

/**
 * Created by wangyu on 2017/3/17.
 */

public class ImgPresenter extends BaseJPresenterImpl<ImgModel, ImgView> {
    private Handler handler;

    @Override
    public void onPresenterCreate(JApp jApp) {
        handler = jApp.getSuperHandler();
    }

    @Override
    public void beginPresent() {
        final ImgView view = getJView();
        view.showDialog();
        mModel.doTest(new OnResultListener<List<BannerInfo>>() {
            @Override
            public void onResult(int i, final List<BannerInfo> bannerInfos) {
                if (i == RESULT_SUCCESS) {
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            view.hideDialog();
                            view.showData(bannerInfos);
                        }
                    }, 2000);
                }
            }
        });
    }
}
