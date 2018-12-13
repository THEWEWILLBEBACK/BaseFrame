package com.optimusprime.simple.base

import android.os.Bundle
import com.optimusprime.xframe.ui.XFrameActivity

/**
 * Created by Xiejq on 2018/12/13.
 */
abstract class BaseActivity<V, T : BasePresenter<V>> : XFrameActivity() {
    //presenter层引用
    var presenter: T? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //将presenter层依附到view层
        presenter = createPresenter()
        presenter!!.attachView(view = this as V)
    }

    abstract fun createPresenter(): T

    override fun onDestroy() {
        super.onDestroy()
        //将presenter层从view层剥离
        presenter!!.detachView()
    }

}