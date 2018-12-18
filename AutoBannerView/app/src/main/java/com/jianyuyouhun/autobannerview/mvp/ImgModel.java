package com.jianyuyouhun.autobannerview.mvp;

import com.jianyuyouhun.autobannerview.entity.BannerInfo;
import com.jianyuyouhun.jmvplib.app.broadcast.LightBroadcast;
import com.jianyuyouhun.jmvplib.mvp.BaseJModelImpl;
import com.jianyuyouhun.jmvplib.mvp.OnResultListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangyu on 2017/3/17.
 */

public class ImgModel extends BaseJModelImpl {

    public void doTest(final OnResultListener<List<BannerInfo>> listener) {
        final List<BannerInfo> list = new ArrayList<>();
        BannerInfo info1 = new BannerInfo();
        info1.setUrl("http://img3.imgtn.bdimg.com/it/u=1749061261,2462112140&fm=21&gp=0.jpg");
        info1.setName("寒冰射手");
        list.add(info1);
        BannerInfo info2 = new BannerInfo();
        info2.setUrl("http://img5.imgtn.bdimg.com/it/u=1949438216,3782070973&fm=23&gp=0.jpg");
        info2.setName("齐天大圣");
        list.add(info2);
        BannerInfo info3 = new BannerInfo();
        info3.setUrl("http://imgs.91danji.com/Upload/201419/2014191037351347278.jpg");
        info3.setName("放逐之刃");
        list.add(info3);
        LightBroadcast.getInstance().postDelayed(new Runnable() {
            @Override
            public void run() {
                listener.onResult(OnResultListener.RESULT_SUCCESS, new ArrayList<BannerInfo>());
            }
        }, 2000);
    }
}
