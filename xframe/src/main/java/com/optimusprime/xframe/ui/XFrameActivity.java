package com.optimusprime.xframe.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.optimusprime.xframe.R;
import com.optimusprime.xframe.constant.KeyConstant;
import com.optimusprime.xframe.utils.status.StatusBarUtil;

import me.yokeyword.fragmentation.ExtraTransaction;
import me.yokeyword.fragmentation.ISupportActivity;
import me.yokeyword.fragmentation.ISupportFragment;
import me.yokeyword.fragmentation.SupportActivityDelegate;
import me.yokeyword.fragmentation.SupportHelper;
import me.yokeyword.fragmentation.anim.FragmentAnimator;


/**
 * Created by Xiejq on 2017/5/4.
 * 注意： 继承的activity要在initView中设置布局。
 */

public abstract class XFrameActivity extends AppCompatActivity implements ISupportActivity{

    private static final String TAG = XFrameActivity.class.getSimpleName();
    private Bitmap bg;
    //进行耗时操作是的弹框
    protected LayoutInflater mInflater;
    protected View mContent;//根布局
    private RelativeLayout mLoadingErr;
    private TextView mTxtLoadErrTip;
    private IWaitAnimDialog mWaitAnimDialog;
    final SupportActivityDelegate mDelegate = new SupportActivityDelegate(this);
    private ImageView mMPicLoadState;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preStartActivity(savedInstanceState);
        initWaitingDialog();
        initInflate();
        initView();
        initData();
        initListener();
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP){
            StatusBarUtil.setColor(this, Color.WHITE);
        }
    }


    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDelegate.onCreate(savedInstanceState);
    }

    /**
     * activity开启前的操作
     */
    private void preStartActivity(@Nullable Bundle savedInstanceState) {
        mDelegate.onCreate(savedInstanceState);
        Intent intent = getIntent();
        if (intent.hasExtra(KeyConstant.BASE_DATA)) {
            Bundle bundle = intent.getBundleExtra(KeyConstant.BASE_DATA);
            if (bundle != null && bundle.containsKey(KeyConstant.GAUSS_PICTURE)) {
                bg = bundle.getParcelable(KeyConstant.GAUSS_PICTURE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    getWindow().getDecorView().setBackground(new BitmapDrawable(getResources(), bg));
                }
            }
        }

    }

    /**
     * 实例化waitingDialog
     */
    private void initWaitingDialog() {
        // todo  to initialize the loading
    }

    /**
     * 实例化inflate
     */
    private void initInflate() {
        if (mInflater == null) {
            mInflater = LayoutInflater.from(this);
        }
    }

    /**
     * 设置关联布局
     *
     * @param layoutId
     */
    protected void setActivityContentView(@LayoutRes int layoutId, StateVector stateVector) {
        mContent = mInflater.inflate(layoutId, null);
        setContentView(layoutId);
        initLoadingFailedView(stateVector);
    }


    protected void setActivityContentView(View contentView, StateVector stateVector) {
        mContent = contentView;
        setContentView(contentView);
        initLoadingFailedView(stateVector);
    }


    private void initLoadingFailedView(StateVector stateVector) {
        // TODO: 2017/9/4  网络状态统一管理
        RelativeLayout container = (RelativeLayout) findViewById(stateVector.getContainerId());
        mLoadingErr = (RelativeLayout) mInflater.inflate(stateVector.getStatePageLayoutId(), ((ViewGroup) mContent), false);
        mTxtLoadErrTip = ((TextView) mLoadingErr.findViewById(stateVector.getStateTipsId()));
        mMPicLoadState = (ImageView) mLoadingErr.findViewById(stateVector.getStatePicId());
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(displayMetrics.widthPixels, displayMetrics.heightPixels);
        layoutParams.height = displayMetrics.heightPixels;
        layoutParams.width = displayMetrics.widthPixels;
        container.addView(mLoadingErr, 0, layoutParams);
        mLoadingErr.setLayoutParams(layoutParams);
        mLoadingErr.setVisibility(View.INVISIBLE);
    }

    /**
     * 进行findviewbyid的操作
     */
    protected abstract void initView();

    /**
     * 进行数据的初始化
     */
    protected abstract void initData();

    /**
     * 进行设置监听的操作
     */
    protected abstract void initListener();



    protected void showWaitingDialog(){
        if (mWaitAnimDialog != null && !mWaitAnimDialog.isShowing()) {
            if (mWaitAnimDialog.getDialogWindow() != null) {
                mWaitAnimDialog.show();
            }
        }
    }

    /**
     * 移除dialog
     */
    protected void removeWaitingDialog() {
        if (mWaitAnimDialog != null && mWaitAnimDialog.isShowing()) {
            if (mWaitAnimDialog.getDialogWindow() != null) {
                mWaitAnimDialog.dismiss();
            }
        }
    }





    @Override
    public SupportActivityDelegate getSupportDelegate() {
        return mDelegate;
    }


    /**
     * Perform some extra transactions.
     * 额外的事务：自定义Tag，添加SharedElement动画，操作非回退栈Fragment
     */
    @Override
    public ExtraTransaction extraTransaction() {
        return mDelegate.extraTransaction();
    }

    /**
     * Note： return mDelegate.dispatchTouchEvent(ev) || super.dispatchTouchEvent(ev);
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return mDelegate.dispatchTouchEvent(ev) || super.dispatchTouchEvent(ev);
    }

    /**
     * 该方法回调时机为,Activity回退栈内Fragment的数量 小于等于1 时,默认finish Activity
     * 请尽量复写该方法,避免复写onBackPress(),以保证SupportFragment内的onBackPressedSupport()回退事件正常执行
     */
    @Override
    public void onBackPressedSupport() {
        mDelegate.onBackPressedSupport();
    }

    /**
     * 获取设置的全局动画 copy
     *
     * @return FragmentAnimator
     */
    @Override
    public FragmentAnimator getFragmentAnimator() {
        return mDelegate.getFragmentAnimator();
    }

    /**
     * Set all fragments animation.
     * 设置Fragment内的全局动画
     */
    @Override
    public void setFragmentAnimator(FragmentAnimator fragmentAnimator) {
        mDelegate.setFragmentAnimator(fragmentAnimator);
    }

    /**
     * Set all fragments animation.
     * 构建Fragment转场动画
     * <p/>
     * 如果是在Activity内实现,则构建的是Activity内所有Fragment的转场动画,
     * 如果是在Fragment内实现,则构建的是该Fragment的转场动画,此时优先级 > Activity的onCreateFragmentAnimator()
     *
     * @return FragmentAnimator对象
     */
    @Override
    public FragmentAnimator onCreateFragmentAnimator() {
        return mDelegate.onCreateFragmentAnimator();
    }

    // 选择性拓展其他方法

    public void loadRootFragment(int containerId, @NonNull ISupportFragment toFragment) {
        mDelegate.loadRootFragment(containerId, toFragment);
    }

    public void start(ISupportFragment toFragment) {
        mDelegate.start(toFragment);
    }

    /**
     * @param launchMode Same as Activity's LaunchMode.
     */
    public void start(ISupportFragment toFragment, @ISupportFragment.LaunchMode int launchMode) {
        mDelegate.start(toFragment, launchMode);
    }

    /**
     * Pop the fragment.
     */
    public void pop() {
        mDelegate.pop();
    }

    /**
     * Pop the last fragment transition from the manager's fragment
     * back stack.
     */
    public void popTo(Class<?> targetFragmentClass, boolean includeTargetFragment) {
        mDelegate.popTo(targetFragmentClass, includeTargetFragment);
    }

    /**
     * If you want to begin another FragmentTransaction immediately after popTo(), use this method.
     * 如果你想在出栈后, 立刻进行FragmentTransaction操作，请使用该方法
     */
    public void popTo(Class<?> targetFragmentClass, boolean includeTargetFragment, Runnable afterPopTransactionRunnable) {
        mDelegate.popTo(targetFragmentClass, includeTargetFragment, afterPopTransactionRunnable);
    }

    public void popTo(Class<?> targetFragmentClass, boolean includeTargetFragment, Runnable afterPopTransactionRunnable, int popAnim) {
        mDelegate.popTo(targetFragmentClass, includeTargetFragment, afterPopTransactionRunnable, popAnim);
    }

    /**
     * 得到位于栈顶Fragment
     */
    public ISupportFragment getTopFragment() {
        return SupportHelper.getTopFragment(getSupportFragmentManager());
    }

    /**
     * 获取栈内的fragment对象
     */
    public <T extends ISupportFragment> T findFragment(Class<T> fragmentClass) {
        return SupportHelper.findFragment(getSupportFragmentManager(), fragmentClass);
    }

    /**
     * 跳转到其它activity
     *
     * @param intent
     */
    public void goToActivity(Intent intent) {
        startActivity(intent);
        overridePendingTransitionEnter();
    }

    /**
     * 跳转到其它activity
     *
     * @param activity
     * @param bundle
     */
    public void goToActivity(Class activity, Bundle bundle) {
        Intent intent = new Intent(this, activity);
        if (bundle != null && bundle.size() != 0) {
            intent.putExtra(KeyConstant.BASE_DATA, bundle);
        }
        goToActivity(intent);
    }


    /**
     * 跳转到其它activity
     *
     * @param activity
     * @param bundle
     */
    public void goToActivity(Class activity, Bundle bundle, String key) {
        Intent intent = new Intent(this, activity);
        if (bundle != null && bundle.size() != 0) {
            intent.putExtra(key, bundle);
        }
        goToActivity(intent);
    }

    /**
     * 使用共享参数跳转（api 16以上）
     *
     * @param activity
     * @param bundle
     * @param options
     */
    public void goToActivity(Class activity, Bundle bundle, Bundle options) {
        Intent intent = new Intent(this, activity);
        if (bundle != null && bundle.size() != 0) {
            intent.putExtra(KeyConstant.BASE_DATA, bundle);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            startActivity(intent, options);
            overridePendingTransition(0, 0);
        }
    }

    /**
     * 左右切换的退出
     */
    public void exitFinishNoAnim() {
        finish();
        overridePendingTransition(0, 0);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onBackPressed() {
//        if (XFrameCallBack.getInstance().mOnBackPressList != null) {
//            if (XFrameCallBack.getInstance().mOnBackPressList.get(XFrameCallBack.getInstance().mOnBackPressList.size() - 1) instanceof BeautyMallBaseFragment) {
//                if (((BeautyMallBaseFragment) MallCallBack.getInstance().mOnBackPress).getHost().equals(this)) {
//                    MallCallBack.getInstance().mOnBackPress.onActivityBackPress(getSupportFragmentManager().getBackStackEntryCount() < 1);
//                } else {
//                    exitFinish();
//                }
//            }
//        } else if (MallCallBack.getInstance().mOnBackPress == null &&   //这种情形存在与跳转到其他的activity，回来时还要按照之前的回退来(目前是存在订单也到商品详情页跳转下单回退回来的情形)
//                getSupportFragmentManager().getBackStackEntryCount() >= 1) {
//            //由于回退和动画在首次进入时，点击过快会有冲突，导致页面不可用，此处延时
//            new Handler().postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    getSupportFragmentManager().popBackStack();
//                }
//            }, 400);
//        } else {
//            exitFinish();
//        }

    }


    /**
     * 左右切换的退出
     */
    public void exitFinish() {
        finish();
        overridePendingTransitionExit();
    }

    /**
     * Overrides the pending Activity transition by performing the "Enter" animation.
     */
    public void overridePendingTransitionEnter() {
        overridePendingTransition(R.anim.xframe_slide_from_right, R.anim.xframe_slide_to_left);
    }

    /**
     * Overrides the pending Activity transition by performing the "Exit" animation.
     */
    public void overridePendingTransitionExit() {
        overridePendingTransition(R.anim.xframe_slide_from_left, R.anim.xframe_slide_to_right);
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        mDelegate.onDestroy();
        super.onDestroy();
        if (bg != null && !bg.isRecycled()) {
            bg.recycle();
            bg = null;
            System.gc();
        }
    }

    protected void showHttpResult(HttpResult httpResult) {
        switch (httpResult) {
            case OFFLINETOAST:
                shopOffLineToast();
                break;
            case LOADINGFAILED:
                showLoadingFailed();
                break;
            case NOINTERNET:
                showNoInternet();
                break;
            case LOADINGFAILEDTOAST:
                showLoadingFailedToast();
                break;
            case NONINTERNETMOVETOAST:
                showNoInternetMoveToast();
                break;
            case SUCCESSED:
                hideHttpResultLayout();
        }
    }

    private void hideHttpResultLayout() {
        mLoadingErr.setVisibility(View.INVISIBLE);
    }

    /**
     * 显示没有网络的吐司（居中的文字吐司）
     */
    protected void showNoInternetMoveToast() {
    }

    /**
     * 显示加载失败的吐司
     */
    protected void showLoadingFailedToast() {
    }


    /**
     * 显示没有网络的布局
     */
    protected void showNoInternet() {
        if (mLoadingErr != null) {
            mLoadingErr.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 显示加载失败的布局
     */
    protected void showLoadingFailed() {
        if (mLoadingErr != null) {
            mLoadingErr.setVisibility(View.VISIBLE);
        }
    }


    /**
     * 显示没有网络的吐司
     */
    protected void shopOffLineToast() {
    }

    /**
     * OFFLINETOAST: 无网络状态的吐司，带有叹号的吐司
     * LOADINGFAILED：第一次进入，加载失败了显示的布局
     * NOINTERNET：第一次进入，无网络显示的布局
     * LOADINGFAILEDTOAST：加载失败的吐司（非第一次进入出现）
     * NONINTERNETMOVETOAST：无网络状况下的吐司（非第一次进入）
     */
    protected enum HttpResult {
        SUCCESSED, OFFLINETOAST, LOADINGFAILED, NOINTERNET, LOADINGFAILEDTOAST, NONINTERNETMOVETOAST
    }


    /**
     * 网络加载状态的载体类
     */
    public static class StateVector{
        private @LayoutRes int statePageLayoutId;
        private @IdRes int containerId;
        private @IdRes int stateTipsId;
        private @IdRes int statePicId;

        public StateVector(Builder builder){
            this.statePageLayoutId = builder.statePageId;
            this.containerId = builder.containerId;
            this.stateTipsId = builder.stateTipsId;
            this.statePicId = builder.statePicId;
        }


        public int getStatePageLayoutId() {
            return statePageLayoutId;
        }

        public int getContainerId() {
            return containerId;
        }

        public int getStateTipsId() {
            return stateTipsId;
        }

        public int getStatePicId() {
            return statePicId;
        }

        private static class Builder{
            private @LayoutRes int statePageId;
            private @IdRes int containerId;
            private @IdRes int stateTipsId;
            private @IdRes int statePicId;

            public Builder statePage(@LayoutRes int statePageId){
                this.statePageId = statePageId;
                return this;
            }

            public Builder container(@IdRes int containerId){
                this.containerId = containerId;
                return this;
            }

            public Builder stateTips(@IdRes int stateTipsId){
                this.stateTipsId = stateTipsId;
                return this;
            }

            public Builder statePic(@IdRes int statePicId){
                this.statePicId = statePicId;
                return this;
            }

            public StateVector build(){
                return new StateVector(this);
            }


        }
    }
}