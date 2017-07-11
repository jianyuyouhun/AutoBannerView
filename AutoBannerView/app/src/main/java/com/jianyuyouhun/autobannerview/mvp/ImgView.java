package com.jianyuyouhun.autobannerview.mvp;

import com.jianyuyouhun.autobannerview.adapter.MyAutoBannerAdapter;
import com.jianyuyouhun.autobannerview.entity.BannerInfo;
import com.jianyuyouhun.jmvplib.mvp.BaseJView;

import java.util.List;

/**
 * Created by wangyu on 2017/3/17.
 */

public interface ImgView extends BaseJView {
    void showDialog();
    void hideDialog();
    void showData(List<BannerInfo> infos);
    MyAutoBannerAdapter getAdapter();
    void showToast(String msg);
}
