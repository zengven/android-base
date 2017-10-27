package com.github.base.dialog;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialog;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.github.R;
import com.github.utils.DisplayUtil;
import com.github.utils.Logger;

/**
 * author: zengven
 * date: 2017/6/13
 * Desc: dialog通用基类
 */
public abstract class BaseDialog extends AppCompatDialog {

    //dialog显示样式
    protected enum Mode {
        Fill,  //横向填充屏幕
        Default, //默认居中
        Full //全屏
    }

    public BaseDialog(Context context) {
        this(context, 0);
    }

    public BaseDialog(Context context, int themeResId) {
        super(context, themeResId == 0 ? R.style.CustomDialog : themeResId); //默认主题,子类可自定义
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = onCreateView(getLayoutInflater());
        setContentView(view);
        onViewCreated(view);
        initData();
    }

    /**
     * Called to have the dialog instantiate its user view.
     * This will be called between{@link #onCreate(Bundle)}
     *
     * @param inflater
     * @return
     */
    protected abstract View onCreateView(LayoutInflater inflater);

    /**
     * Called immediately after {@link #onCreateView(LayoutInflater)}
     * has returned, but before any saved state has been restored in to the view.
     *
     * @param view
     */
    protected void onViewCreated(View view) {
    }

    protected void initData() {
    }

    /**
     * 设置dialog样式
     *
     * @param mode    dialog显示样式
     * @param gravity dialog显示位置 {@link Gravity}
     * @param resId   dialog进入退出动画 默认动画:0
     */
    protected void setStyle(Mode mode, int gravity, int resId) {
        Window dialogWindow = getWindow();
        if (mode == Mode.Fill || mode == Mode.Full) {
            WindowManager.LayoutParams lp = dialogWindow.getAttributes();
            DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
            lp.width = (displayMetrics.widthPixels);//dialog铺满整个屏幕
            if (mode == Mode.Full) {
                lp.height = displayMetrics.heightPixels - DisplayUtil.getStatusHeight(getContext());
                Logger.e(" width: " + lp.width + " height: " + lp.height);
            }
            dialogWindow.setAttributes(lp);
        }
        if (gravity != Gravity.NO_GRAVITY) {
            dialogWindow.setGravity(gravity); //设置在屏幕的位置
        }
        if (resId != 0)
            dialogWindow.setWindowAnimations(resId);//设置动画
    }

}
