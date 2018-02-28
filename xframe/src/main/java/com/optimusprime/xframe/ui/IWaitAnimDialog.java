package com.optimusprime.xframe.ui;

import android.view.Window;

/**
 * Created by Xiejq on 2018/2/28.
 * 为页面耗时加载提供的弹框接口
 */

public interface IWaitAnimDialog {

    /**
     * 加载loading显示的方法
     */
    void show();

    /**
     * 加载弹框消失的方法
     */
    void dismiss();

    /**
     * loading是否在加载中
     *
     * @return boolean true onLoading, false noLoading
     */
    boolean isShowing();

    /**
     * 返回dialog的window，用于防止window的token过期抛出的异常
     * @return
     */
    Window getDialogWindow();



}
