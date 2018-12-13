package com.optimusprime.simple.base

import android.content.Context
import com.optimusprime.xframe.ui.XFramFragment

/**
 * Created by Xiejq on 2018/12/13.
 */
abstract class BaseFragment<V, T : IPresenter<V>> : XFramFragment() {
    //presenter层引用
    var presenter: T? = null


    override fun onAttach(context: Context?) {
        super.onAttach(context)
        //将presenter层依附到view层
        presenter = createPresenter()
        presenter!!.attachView(view = this as V)
    }

    abstract fun createPresenter(): T

    override fun onDetach() {
        super.onDetach()
        //将presenter层从view层剥离
        presenter!!.detachView()
    }
}