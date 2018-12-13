package com.optimusprime.simple.persent

import com.optimusprime.simple.base.BaseActivity
import com.optimusprime.simple.base.BasePresenter
import com.optimusprime.simple.base.IPresenter

/**
 * Created by Xiejq on 2018/12/13.
 */
interface IMainPresenter<T> : IPresenter<T> {

    fun thingOne()
}