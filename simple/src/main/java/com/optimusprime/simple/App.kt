package com.optimusprime.simple

import android.util.Log
import com.optimusprime.xframe.XFrameApp

/**
 * Created by Xiejq on 2018/12/13.
 */
class App : XFrameApp() {


    //静态块
    companion object {
        const val TAG: String = "App"
        var app: App? = null
        fun getApplication(): App {
            return app!!
        }
    }

    override fun onCreate() {
        super.onCreate()
        app = this
        Log.i(TAG, "onCreate:")
    }


}