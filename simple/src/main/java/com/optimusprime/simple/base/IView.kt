package com.optimusprime.simple.base

/**
 * Created by Xiejq on 2018/12/13.
 */
interface IView {

    /**
     * 用于关联生命周期，释放内存的引用
     */
    fun release()
}