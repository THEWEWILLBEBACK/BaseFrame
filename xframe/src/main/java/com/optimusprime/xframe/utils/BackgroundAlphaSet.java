package com.optimusprime.xframe.utils;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.view.WindowManager;

/**
 * Created by xiejq on 2017/6/15.
 */

public class BackgroundAlphaSet {

    private static Activity sActivity;

    private static float mAlpha;

    private static Handler sHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            setBackgroundAlpha(sActivity, (Float) msg.obj);
            return false;
        }
    });

    public static void setBackgroundAlpha(Activity activity, float bgAlpha) {
        if (activity != null) {
            WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
            lp.alpha = bgAlpha;
            if (bgAlpha == 1) {
                activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);//不移除该Flag的话,在有视频的页面上的视频会出现黑屏的bug
            } else {
                activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);//此行代码主要是解决在华为手机上半透明效果无效的bug
            }
            activity.getWindow().setAttributes(lp);
        }
    }

    public static void clearActivity() {
        sActivity = null;
    }


    public static void loopChangeAlpha(Activity activity, final Action action) {
        sActivity = null;
        sActivity = activity;

        new Thread(new Runnable() {
            @Override
            public void run() {
                if (action == Action.DISMISS) {
                    //此处while的条件alpha不能<= 1否则会出现黑屏
                    mAlpha = 0.7f;
                    while (mAlpha < 1.0f) {
                        try {
                            Thread.sleep(4);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        Message msg = sHandler.obtainMessage();
                        msg.what = 2;
                        mAlpha += 0.01f;
                        if (mAlpha >= 1.0f) {
                            mAlpha = 1.0f;
                        }
                        msg.obj = mAlpha;
                        sHandler.sendMessage(msg);
                    }
                } else if (action == Action.SHOW) {
                    mAlpha = 1.0f;
                    while (mAlpha > 0.7f) {
                        try {
                            //4是根据弹出动画时间和减少的透明度计算
                            Thread.sleep(4);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        Message msg = sHandler.obtainMessage();
                        msg.what = 2;
                        //每次减少0.01，精度越高，变暗的效果越流畅
                        mAlpha -= 0.01f;
                        if (mAlpha < 0.7f) {
                            mAlpha = 0.7f;
                        }
                        msg.obj = mAlpha;
                        sHandler.sendMessage(msg);
                    }
                }
            }

        }).start();
    }

    public enum Action {
        SHOW, DISMISS
    }


}
