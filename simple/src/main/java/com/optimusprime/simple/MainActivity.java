package com.optimusprime.simple;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.optimusprime.xframe.ui.XFrameActivity;

public class MainActivity extends XFrameActivity {


    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
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

    public void next(View view) {
        goToActivity(new Intent(this, BActivity.class));
    }
}
