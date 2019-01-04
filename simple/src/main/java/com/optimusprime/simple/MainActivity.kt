package com.optimusprime.simple

import android.content.Intent
import android.view.View
import android.widget.ImageView
import com.optimusprime.simple.base.BaseActivity
import com.optimusprime.simple.persent.IMainPresenter
import com.optimusprime.simple.persent.imp.MainPresenterImp
import com.optimusprime.simple.view.MainView

import com.optimusprime.xframe.ui.XFrameActivity
import com.plattysoft.leonids.ParticleSystem

class MainActivity : BaseActivity<MainView, IMainPresenter<MainView>>(), MainView {
    val TAG: String = "MainActivity"
    var mIvBoom :ImageView ?= null


    override fun createPresenter(): MainPresenterImp<MainView> {
        return MainPresenterImp()
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_main
    }

    override fun getSateVector(): XFrameActivity.StateVector? {
        return StateVector.Builder()
                .build()
    }

    override fun initView() {
        presenter.thingOne()
        mIvBoom = findViewById<ImageView>(R.id.iv_boom)
    }

    override fun initData() {

    }

    override fun initListener() {
        mIvBoom!!.setOnClickListener {
            iconBoom(it)
        }
    }


    /**
     * 开启粒子动画
     */
    private fun iconBoom(view: View?) {
        ParticleSystem(this@MainActivity, 2000, R.drawable.boom_partical, 3000)
                .setSpeedModuleAndAngleRange(0.05f,0.5f,180, 360)
                .setRotationSpeed(30f)
                .setAcceleration(0.00097f * 2, 90)
                .oneShot(view, 2000)

    }

    override fun post(runnable: Runnable) {

    }


    fun next(view: View) {
        goToActivity(Intent(this, BActivity::class.java))
    }

    override fun release() {

    }


}
