package com.jianyuyouhun.autobannerview.mvp;

import com.jianyuyouhun.autobannerview.adapter.MyAutoBannerAdapter;
import com.jianyuyouhun.autobannerview.entity.BannerInfo;
import com.jianyuyouhun.jmvplib.mvp.BaseJPresenterImpl;
import com.jianyuyouhun.jmvplib.mvp.OnResultListener;

import java.util.List;

/**
 * Created by wangyu on 2017/3/17.
 */

public class ImgPresenter extends BaseJPresenterImpl<ImgModel, ImgView> {

    private MyAutoBannerAdapter.OnBannerClickListener onBannerClickListener = new MyAutoBannerAdapter.OnBannerClickListener() {
        @Override
        public void onClick(BannerInfo info) {
            getJView().showToast(info.getName());
        }
    };

    public void registerListener() {
        getJView().getAdapter().setOnBannerClickListener(onBannerClickListener);
    }

    public void beginPresent() {
        final ImgView view = getJView();
        view.showDialog();
        mModel.doTest(new OnResultListener<List<BannerInfo>>() {
            @Override
            public void onResult(int i, final List<BannerInfo> bannerInfos) {
                if (i == RESULT_SUCCESS) {
                    view.hideDialog();
                    view.showData(bannerInfos);
                } else {
                    view.showError("error");
                }
            }
        });
    }

    public void refresh() {
        beginPresent();
    }
}
