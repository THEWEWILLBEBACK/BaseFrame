package com.optimusprime.simple

import android.content.Intent
import android.view.View

import com.optimusprime.xframe.ui.XFrameActivity

class MainActivity : XFrameActivity() {


    override fun getLayoutId(): Int {
        return R.layout.activity_main
    }

    override fun getSateVector(): XFrameActivity.StateVector? {
        return null
    }

    override fun initView() {

    }

    override fun initData() {

    }

    override fun initListener() {

    }

    override fun post(runnable: Runnable) {

    }


    fun next(view: View) {
        goToActivity(Intent(this, BActivity::class.java))
    }
}
