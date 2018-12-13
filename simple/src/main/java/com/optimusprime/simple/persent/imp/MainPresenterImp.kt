package com.optimusprime.simple.persent.imp

import android.widget.Toast
import com.optimusprime.simple.App
import com.optimusprime.simple.base.BasePresenter
import com.optimusprime.simple.persent.IMainPresenter

/**
 * Created by Xiejq on 2018/12/13.
 */
class MainPresenterImp<T> : BasePresenter<T>(), IMainPresenter<T> {
    override fun thingOne() {
        //do something
        App.app!!.toast("今晚打老虎")
    }


    override fun release() {

    }
}