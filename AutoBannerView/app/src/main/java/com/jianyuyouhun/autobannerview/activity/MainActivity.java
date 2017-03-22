package com.jianyuyouhun.autobannerview.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.jianyuyouhun.autobannerview.App;
import com.jianyuyouhun.autobannerview.entity.BannerInfo;
import com.jianyuyouhun.autobannerview.mvp.ImgModel;
import com.jianyuyouhun.autobannerview.mvp.ImgPresenter;
import com.jianyuyouhun.autobannerview.mvp.ImgView;
import com.jianyuyouhun.autobannerview.adapter.MyAutoBannerAdapter;
import com.jianyuyouhun.autobannerview.R;
import com.jianyuyouhun.jmvplib.app.BaseActivity;
import com.jianyuyouhun.library.AutoBannerView;

import java.util.List;

public class MainActivity extends BaseActivity<ImgPresenter, ImgModel> implements ImgView {
    private AutoBannerView autoBannerView;
    private MyAutoBannerAdapter autoBannerAdapter;
    private TextView title;
    private List<BannerInfo> list;

    private AutoBannerView.OnBannerChangeListener onBannerChangeListener = new AutoBannerView.OnBannerChangeListener() {
        @Override
        public void onCurrentItemChanged(int position) {
            title.setText(list.get(position).getName());
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initData();
    }

    @Override
    public int getLayoutResId() {
        return R.layout.activity_main;
    }

    @Override
    public ImgPresenter getPresenter() {
        return App.getInstance().getJPresenter(ImgPresenter.class);
    }

    @Override
    protected ImgModel initModel() {
        return new ImgModel();
    }

    @Override
    public boolean bindModelAndView() {
        mPresenter.onBindModelView(mModel, this);
        return true;
    }

    private void initView() {
        autoBannerView = (AutoBannerView) findViewById(R.id.autoBannerView);
        title = (TextView) findViewById(R.id.bannerTitle);
        autoBannerView.setDotGravity(AutoBannerView.DotGravity.RIGHT);
        autoBannerView.setWaitMilliSceond(3000);
        autoBannerView.setDotMargin(4);
        autoBannerView.setOnBannerChangeListener(onBannerChangeListener);
        autoBannerAdapter = new MyAutoBannerAdapter(getApplicationContext());
    }

    private void initData() {
        mPresenter.beginPresent();
    }

    @Override
    public void showDialog() {
        showProgressDialog();
    }

    @Override
    public void hideDialog() {
        dismissProgressDialog();
    }

    @Override
    public void showData(List<BannerInfo> infos) {
        list = infos;
        title.setVisibility(View.VISIBLE);
        autoBannerAdapter.changeItems(list);
        autoBannerView.setAdapter(autoBannerAdapter);
    }

    @Override
    public void showError(String s) {

    }
}
