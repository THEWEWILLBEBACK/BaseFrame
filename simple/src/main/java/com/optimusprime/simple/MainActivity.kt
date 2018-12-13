package com.optimusprime.simple

import android.content.Intent
import android.util.Log
import android.view.View
import com.optimusprime.simple.base.BaseActivity
import com.optimusprime.simple.persent.IMainPresenter
import com.optimusprime.simple.persent.imp.MainPresenterImp
import com.optimusprime.simple.view.MainView

import com.optimusprime.xframe.ui.XFrameActivity

class MainActivity : BaseActivity<MainView, IMainPresenter<MainView>>(), MainView {
    val TAG: String = "MainActivity"


    override fun createPresenter(): MainPresenterImp<MainView> {
        return MainPresenterImp()
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_main
    }

    override fun getSateVector(): XFrameActivity.StateVector? {
        return null
    }

    override fun initView() {
        presenter!!.thingOne()
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

    override fun release() {

    }


}
