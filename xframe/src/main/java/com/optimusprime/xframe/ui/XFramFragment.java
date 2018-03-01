package com.optimusprime.xframe.ui;


import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.optimusprime.xframe.R;

import me.yokeyword.fragmentation.ExtraTransaction;
import me.yokeyword.fragmentation.ISupportFragment;
import me.yokeyword.fragmentation.SupportFragmentDelegate;
import me.yokeyword.fragmentation.SupportHelper;
import me.yokeyword.fragmentation.anim.FragmentAnimator;


/**
 * Created by Xiejq on 2017/5/4.
 * BaseFragment 全局Fragment的初始化
 */

public abstract class XFramFragment extends Fragment implements ISupportFragment {
    public static final String TAG = XFramFragment.class.getSimpleName();
    final SupportFragmentDelegate mDelegate = new SupportFragmentDelegate(this);

    //用来标记是否第一次进入fragment，不能删除
    protected boolean isNotFirstRun;
    protected View mLayout;

    protected FragmentActivity mActivity;
    //进行耗时操作的loading
    protected IWaitAnimDialog mWaitAnimDialog;
    private boolean isFastClick;

    protected RelativeLayout mLoadingErr;
    protected TextView mTxtLoadErrTip;
    private LayoutInflater mInflater;
    private ImageView mIvNonet;


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mDelegate.onAttach(activity);
        mActivity = mDelegate.getActivity();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mActivity = null;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDelegate.onCreate(savedInstanceState);
        initWaitingDialog();
        mInflater = LayoutInflater.from(getContext());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mInflater = inflater;
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mDelegate.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mDelegate.onSaveInstanceState(outState);
    }

    private void initWaitingDialog() {
        // todo  to initialize the loading
    }


    /**
     * 进行Fragment页面的View的初始化
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

    /**
     * 跳转到其它activity
     *
     * @param activity
     * @param bundle
     */
    public void goToActivity(Class activity, Bundle bundle) {
        ((XFrameActivity) getActivity()).goToActivity(activity, bundle);
    }

    /**
     * 跳转到其它activity
     *
     * @param activity
     * @param bundle
     */
    public void goToActivity(Class activity, Bundle bundle, String key) {
        ((XFrameActivity) getActivity()).goToActivity(activity, bundle, key);
    }

    /**
     * 使用共享参数跳转（api 16以上）
     *
     * @param activity
     * @param bundle
     * @param options
     */
    public void goToActivity(Class activity, Bundle bundle, Bundle options) {
        Intent intent = new Intent(getActivity(), activity);
        if (bundle != null && bundle.size() != 0) {
            intent.putExtra("data", bundle);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            startActivity(intent, options);
        }
    }


    //----------------------------------------------------------------------------------------------
    public static final int LEFT_RIGHT = 6661;
    public static final int OPEN = 6662;
    public static final int NOTHING = 6663;
    public static final int TOLEFT = 6664;
    public static final int F_TEST = 6665;

    public void goToFragment(@IdRes int containerViewId, Fragment targetFragment, String flag) {
        goToFragment(containerViewId, targetFragment, LEFT_RIGHT, flag);
    }

    /**
     * "带栈的"fragment的跳转
     * 使用后 按返回键不退出activity 清空fragment后退出activity
     * 注：第一层fragment添加请用ft.add（） 该方法只用于跳转
     *
     * @param containerViewId 要填充的控件id
     * @param targetFragment  跳转目标
     * @param type            BeautyMallBaseFragment.LEFT_RIGHT 类似activity进出动画；.OPEN 打开动画；.NOTHING 无动画
     * @param flag            标志：建议写targetFragment的类名；
     */
    public void goToFragment(@IdRes int containerViewId, Fragment targetFragment, int type, String flag) {
        FragmentTransaction ft;
        if (getActivity() != null) {
            ft = getActivity()
                    .getSupportFragmentManager()
                    .beginTransaction();
        } else return;
        switch (type) {
            case LEFT_RIGHT:
                ft.setCustomAnimations(R.anim.xframe_slide_from_right, R.anim.xframe_slide_to_left, R.anim.xframe_slide_from_left, R.anim.xframe_slide_to_right);
                break;
            case OPEN:
            case NOTHING:
            case TOLEFT:
            case F_TEST:
        }
        ft.addToBackStack(flag).replace(containerViewId, targetFragment).commit();
    }

    /**
     * 弹出最上层fragment页
     */
    public void backFragment() {
        if (getActivity() != null && isFastClick) {
            getActivity().getSupportFragmentManager().popBackStack();
        }
    }

    /**
     * @param tag
     */
    public void backFragment(String tag) {
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.hide(this)
                .show(getActivity()
                        .getSupportFragmentManager()
                        .findFragmentByTag(tag))
                .commit();
    }


    /**
     * 根据tag来进行弹回退栈
     *
     * @param tag
     * @param flag
     */
    public void backFragment(String tag, PopFlag flag) {
        if (tag != null) {
            if (flag == PopFlag.POP_BACK_STACK_INCLUSIVE) {
                //如果是1，弹出该fragment（包括该fragment）以上的fragment。
                getActivity().getSupportFragmentManager().popBackStack(tag, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            } else if (flag == PopFlag.POP_BACK_WITHOUT_THIS) {
                //如果tag不为null，那就会找到这个tag所对应的fragment，flags为0时，弹出该fragment以上的Fragment
                getActivity().getSupportFragmentManager().popBackStack(tag, 0);
            }
        } else {
            if (flag == PopFlag.POP_BACK_STACK_INCLUSIVE) {
                //如果tag为null ，flags为1时，弹出回退栈中所有fragment
                getActivity().getSupportFragmentManager().popBackStack(tag, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            } else if (flag == PopFlag.POP_BACK_WITHOUT_THIS) {
                // 如果tag为null，flags为0时，弹出回退栈中最上层的那个fragment
                getActivity().getSupportFragmentManager().popBackStack(tag, 0);
            }
        }


    }

    enum PopFlag {
        POP_BACK_STACK_INCLUSIVE, POP_BACK_WITHOUT_THIS
    }

    /**
     * 弹出多层fragment
     *
     * @param number 弹出的页面数量
     */
    public void backFragment(int number) {
        FragmentManager supportFragmentManager = getActivity().getSupportFragmentManager();
        for (int i = 0; i < number; i++) {
            supportFragmentManager.popBackStackImmediate();
        }
    }

    //----------------------------------------------------------------------------------------------

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initLoadingFailedView(view);
    }

    public void initLoadingFailedView(View view) {
        StateVector stateVector = initStateVector();
        if (stateVector == null) {
            RelativeLayout container = (RelativeLayout) view.findViewById(stateVector.getContainerId());
            mLoadingErr = (RelativeLayout) mInflater.inflate(stateVector.getStatePageLayoutId(), ((ViewGroup) view), false);
            mIvNonet = ((ImageView) mLoadingErr.findViewById(stateVector.getStateTipsId()));
            mTxtLoadErrTip = ((TextView) mLoadingErr.findViewById(stateVector.getStatePicId()));
            DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(displayMetrics.widthPixels, displayMetrics.heightPixels);
            layoutParams.height = displayMetrics.heightPixels;
            layoutParams.width = displayMetrics.widthPixels;
            container.addView(mLoadingErr, 0, layoutParams);
            mLoadingErr.setLayoutParams(layoutParams);
            mLoadingErr.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * 初始化内部状态页面载体类
     *
     * @return
     */
    protected StateVector initStateVector() {
        return null;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        mDelegate.onResume();
        //防止过快操作
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                isFastClick = true;
            }
        }, 400);
        isNotFirstRun = true;
    }


    @Override
    public void onPause() {
        super.onPause();
        mDelegate.onPause();
    }

    @Override
    public void onDestroyView() {
        mDelegate.onDestroyView();
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        mDelegate.onDestroy();
        super.onDestroy();
    }


    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        mDelegate.onHiddenChanged(hidden);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        mDelegate.setUserVisibleHint(isVisibleToUser);
    }


    /**
     * 显示加载的loading
     */
    protected void showWaitingAnimDialog() {
        if (mWaitAnimDialog != null && !mWaitAnimDialog.isShowing()) {
            if (mWaitAnimDialog.getDialogWindow() != null) {
                mWaitAnimDialog.show();
            }
        }
    }

    /**
     * 移除dialog
     */
    protected void removeWaitingAnimDialog() {
        if (mWaitAnimDialog != null && mWaitAnimDialog.isShowing()) {
            if (mWaitAnimDialog.getDialogWindow() != null) {
                mWaitAnimDialog.dismiss();
            }
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
            case NONINTERNETMOVE:
                showNoInternetMoveToast();
                break;
            case SUCCESSED:
                hideHttpResultLayout();
        }
    }

    private void hideHttpResultLayout() {
        mLoadingErr.setVisibility(View.INVISIBLE);
    }

    //------------------------在项目的base类里面重写这些方法----------------------------------------

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

    //----------------------------------------------------------------------------------------------

    /**
     * OFFLINETOAST: 无网络状态的吐司，带有叹号的吐司
     * LOADINGFAILED：第一次进入，加载失败了显示的布局
     * NOINTERNET：第一次进入，无网络显示的布局
     * LOADINGFAILEDTOAST：加载失败的吐司（非第一次进入出现）
     * NONINTERNETMOVE：无网络状况下的吐司（非第一次进入）
     */
    protected enum HttpResult {
        SUCCESSED, OFFLINETOAST, LOADINGFAILED, NOINTERNET, LOADINGFAILEDTOAST, NONINTERNETMOVE
    }


    //--------------supportFragment--------------------

    /**
     * Perform some extra transactions.
     * 额外的事务：自定义Tag，添加SharedElement动画，操作非回退栈Fragment
     */
    @Override
    public ExtraTransaction extraTransaction() {
        return mDelegate.extraTransaction();
    }


    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        return mDelegate.onCreateAnimation(transit, enter, nextAnim);
    }


    /**
     * If you want to call the start()/pop()/showHideFragment() on the onCreateXX/onActivityCreated,
     * call this method to deliver the transaction to the queue.
     * <p>
     * 在onCreate/onCreateView/onActivityCreated中使用 start()/pop()/showHideFragment(),请使用该方法把你的任务入队
     *
     * @param runnable start() , pop() or showHideFragment()
     */
    @Override
    public void enqueueAction(Runnable runnable) {
        mDelegate.enqueueAction(runnable);
    }


    /**
     * Called when the enter-animation end.
     * 入栈动画 结束时,回调
     */
    @Override
    public void onEnterAnimationEnd(Bundle savedInstanceState) {
        mDelegate.onEnterAnimationEnd(savedInstanceState);
    }


    /**
     * Lazy initial，Called when fragment is first called.
     * <p>
     * 同级下的 懒加载 ＋ ViewPager下的懒加载  的结合回调方法
     */
    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        mDelegate.onLazyInitView(savedInstanceState);
    }

    /**
     * Called when the fragment is visible.
     * 当Fragment对用户可见时回调
     * <p>
     * Is the combination of  [onHiddenChanged() + onResume()/onPause() + setUserVisibleHint()]
     */
    @Override
    public void onSupportVisible() {
        mDelegate.onSupportVisible();
    }

    /**
     * Called when the fragment is invivible.
     * <p>
     * Is the combination of  [onHiddenChanged() + onResume()/onPause() + setUserVisibleHint()]
     */
    @Override
    public void onSupportInvisible() {
        mDelegate.onSupportInvisible();
    }

    /**
     * Return true if the fragment has been supportVisible.
     */
    @Override
    final public boolean isSupportVisible() {
        return mDelegate.isSupportVisible();
    }

    /**
     * Set fragment animation with a higher priority than the ISupportActivity
     * 设定当前Fragmemt动画,优先级比在SupportActivity里高
     */
    @Override
    public FragmentAnimator onCreateFragmentAnimator() {
        return mDelegate.onCreateFragmentAnimator();
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
     * 设置Fragment内的全局动画
     */
    @Override
    public void setFragmentAnimator(FragmentAnimator fragmentAnimator) {
        mDelegate.setFragmentAnimator(fragmentAnimator);
    }

    /**
     * 按返回键触发,前提是SupportActivity的onBackPressed()方法能被调用
     *
     * @return false则继续向上传递, true则消费掉该事件
     */
    @Override
    public boolean onBackPressedSupport() {
        return mDelegate.onBackPressedSupport();
    }

    /**
     * 类似 {@link Activity#setResult(int, Intent)}
     * <p>
     * Similar to {@link Activity#setResult(int, Intent)}
     *
     * @see #startForResult(ISupportFragment, int)
     */
    @Override
    public void setFragmentResult(int resultCode, Bundle bundle) {
        mDelegate.setFragmentResult(resultCode, bundle);
    }

    /**
     * 类似  {@link Activity#onActivityResult(int, int, Intent)}
     * <p>
     * Similar to {@link Activity#onActivityResult(int, int, Intent)}
     *
     * @see #startForResult(ISupportFragment, int)
     */
    @Override
    public void onFragmentResult(int requestCode, int resultCode, Bundle data) {
        mDelegate.onFragmentResult(requestCode, resultCode, data);
    }

    /**
     * 在start(TargetFragment,LaunchMode)时,启动模式为SingleTask/SingleTop, 回调TargetFragment的该方法
     * 类似 {@link Activity#onNewIntent(Intent)}
     * <p>
     * Similar to {@link Activity#onNewIntent(Intent)}
     *
     * @param args putNewBundle(Bundle newBundle)
     * @see #start(ISupportFragment, int)
     */
    @Override
    public void onNewBundle(Bundle args) {
        mDelegate.onNewBundle(args);
    }

    /**
     * 添加NewBundle,用于启动模式为SingleTask/SingleTop时
     *
     * @see #start(ISupportFragment, int)
     */
    @Override
    public void putNewBundle(Bundle newBundle) {
        mDelegate.putNewBundle(newBundle);
    }


    /****************************************以下为可选方法(Optional methods)******************************************************/
    // 自定制Support时，可移除不必要的方法

    /**
     * 隐藏软键盘
     */
    protected void hideSoftInput() {
        mDelegate.hideSoftInput();
    }

    /**
     * 显示软键盘,调用该方法后,会在onPause时自动隐藏软键盘
     */
    protected void showSoftInput(final View view) {
        mDelegate.showSoftInput(view);
    }

    /**
     * 加载根Fragment, 即Activity内的第一个Fragment 或 Fragment内的第一个子Fragment
     *
     * @param containerId 容器id
     * @param toFragment  目标Fragment
     */
    public void loadRootFragment(int containerId, ISupportFragment toFragment) {
        mDelegate.loadRootFragment(containerId, toFragment);
    }

    public void loadRootFragment(int containerId, ISupportFragment toFragment, boolean addToBackStack, boolean allowAnim) {
        mDelegate.loadRootFragment(containerId, toFragment, addToBackStack, allowAnim);
    }

    public void start(ISupportFragment toFragment) {
        mDelegate.start(toFragment);
    }

    /**
     * @param launchMode Similar to Activity's LaunchMode.
     */
    public void start(final ISupportFragment toFragment, @LaunchMode int launchMode) {
        mDelegate.start(toFragment, launchMode);
    }

    /**
     * Launch an fragment for which you would like a result when it poped.
     */
    public void startForResult(ISupportFragment toFragment, int requestCode) {
        mDelegate.startForResult(toFragment, requestCode);
    }

    /**
     * Launch a fragment while poping self.
     */
    public void startWithPop(ISupportFragment toFragment) {
        mDelegate.startWithPop(toFragment);
    }

    public void replaceFragment(ISupportFragment toFragment, boolean addToBackStack) {
        mDelegate.replaceFragment(toFragment, addToBackStack);
    }

    public void pop() {
        mDelegate.pop();
    }

    /**
     * Pop the last fragment transition from the manager's fragment
     * back stack.
     * <p>
     * 出栈到目标fragment
     *
     * @param targetFragmentClass   目标fragment
     * @param includeTargetFragment 是否包含该fragment
     */
    public void popTo(Class<?> targetFragmentClass, boolean includeTargetFragment) {
        mDelegate.popTo(targetFragmentClass, includeTargetFragment);
    }

    /**
     * 获取栈内的fragment对象
     */
    public <T extends ISupportFragment> T findChildFragment(Class<T> fragmentClass) {
        return SupportHelper.findFragment(getChildFragmentManager(), fragmentClass);
    }


    /**
     * 网络加载状态的载体类
     */
    public static class StateVector {
        private @LayoutRes
        int statePageLayoutId;
        private @IdRes
        int containerId;
        private @IdRes
        int stateTipsId;
        private @IdRes
        int statePicId;

        public StateVector(Builder builder) {
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

        private static class Builder {
            private @LayoutRes
            int statePageId;
            private @IdRes
            int containerId;
            private @IdRes
            int stateTipsId;
            private @IdRes
            int statePicId;

            public Builder statePage(@LayoutRes int statePageId) {
                this.statePageId = statePageId;
                return this;
            }

            public Builder container(@IdRes int containerId) {
                this.containerId = containerId;
                return this;
            }

            public Builder stateTips(@IdRes int stateTipsId) {
                this.stateTipsId = stateTipsId;
                return this;
            }

            public Builder statePic(@IdRes int statePicId) {
                this.statePicId = statePicId;
                return this;
            }

            public StateVector build() {
                return new StateVector(this);
            }


        }
    }

}
