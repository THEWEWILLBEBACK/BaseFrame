package com.optimusprime.simple.base

/**
 * Created by Xiejq on 2018/12/13.
 */
interface IPresenter<T> {


    /**
     * attach to View
     */
    fun attachView(view: T)


    /**
     * detach from view
     */
    fun detachView()

    /**
     * 释放presenter持有的引用
     */
    fun release()
}