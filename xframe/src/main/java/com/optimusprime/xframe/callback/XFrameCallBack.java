package com.optimusprime.xframe.callback;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Xiejq on 2018/2/28.
 */

public class XFrameCallBack {
    private static volatile XFrameCallBack mCallBack;

    private XFrameCallBack() {}

    public static XFrameCallBack getInstance() {
        if (mCallBack == null) {
            synchronized (XFrameCallBack.class) {
                if (mCallBack == null) {
                    mCallBack = new XFrameCallBack();
                }
            }
        }
        return mCallBack;
    }


    public List<OnBackPress> mOnBackPressList;

    public void addOnBackPress(OnBackPress onBackPress) {
        if (mOnBackPressList == null) {
            mOnBackPressList = new ArrayList<>();
        }
        mOnBackPressList.add(onBackPress);
    }

    /**
     * 用来将返回键事件传递给fragment
     */
    public interface OnBackPress {
        void onActivityBackPress(boolean isLastFragment);
    }

    /**
     *  移除传入的回掉
     * @param onBackPress
     */
    public void removeCallBack(OnBackPress onBackPress) {
        if (mOnBackPressList != null) {
            mOnBackPressList.contains(onBackPress);
            mOnBackPressList.remove(onBackPress);
        }
    }

    /**
     * 清楚所以的回掉
     */
    public void removeAllCallBack() {
        if (mOnBackPressList != null) {
            mOnBackPressList.clear();
        }
    }


}
