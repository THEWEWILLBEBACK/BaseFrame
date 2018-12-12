package com.optimusprime.simple;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.optimusprime.xframe.ui.XFrameActivity;

public class BActivity extends XFrameActivity {


    @Override
    protected int getLayoutId() {
        return R.layout.activity_b;
    }

    @Override
    protected StateVector getSateVector() {
        return null;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initListener() {

    }

    @Override
    public void post(Runnable runnable) {

    }
}
