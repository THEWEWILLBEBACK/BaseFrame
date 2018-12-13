package com.optimusprime.xframe;

import android.app.Application;
import android.support.multidex.MultiDexApplication;

import com.optimusprime.xframe.database.DbOpenHelper;
import com.optimusprime.xframe.utils.ExceptionCatcher;

/**
 * Created by Xiejq on 2018/2/28.
 */

public class XFrameApp extends MultiDexApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        iniExceptionCatcher();
        DbOpenHelper.init(this, "Test.db");
    }

    private void iniExceptionCatcher() {
        ExceptionCatcher exceptionCatcher = ExceptionCatcher.getInstance();
        exceptionCatcher.init(this);
    }
}
