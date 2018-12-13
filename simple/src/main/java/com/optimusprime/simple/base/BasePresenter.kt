package com.optimusprime.simple.base

import java.lang.ref.WeakReference

/**
 * Created by Xiejq on 2018/12/13.
 *
 * presenter 的base类
 */
abstract class BasePresenter<T> {

    //View层的实例引用，这里使用弱引用，防止内存泄漏问题
    var mViewRef: WeakReference<T>? = null

    /**
     * attach to View
     */
    fun attachView(view: T) {
        mViewRef = WeakReference<T>(view)
    }

    /**
     * detach from view
     */
    fun detachView() {
        mViewRef!!.clear()
        release()
    }

    /**
     * 释放presenter持有的引用
     */
    abstract fun release()
}