package com.optimusprime.xframe;

import android.app.Application;

import com.optimusprime.xframe.utils.ExceptionCatcher;

/**
 * Created by Xiejq on 2018/2/28.
 */

public class XFrameApp extends Application{

    @Override
    public void onCreate() {
        super.onCreate();
        iniExceptionCatcher();
    }

    private void iniExceptionCatcher() {
        ExceptionCatcher exceptionCatcher = ExceptionCatcher.getInstance();
    }
}
