package com.github.widget;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

import com.github.R;

/**
 * author: zengven
 * date: 2017/7/13 12:37
 * desc: 倒计时textview
 */
public class CountDownTextView extends AppCompatTextView implements Runnable {

    private boolean mIsCountDown = false; //是否倒计时
    private static final int DEFULT_COUNTDOWN_TIME = 60;//倒计时默认60S
    private int mCountDownTime;

    public CountDownTextView(Context context) {
        super(context);
    }

    public CountDownTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CountDownTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * 设置倒计时时长
     *
     * @param time
     */
    public void setCountDownTime(int time) {
        this.mCountDownTime = time;
    }

    /**
     * 是否正在运行
     *
     * @return
     */
    public boolean isRun() {
        return mIsCountDown;
    }

    public void start() {
        mCountDownTime = DEFULT_COUNTDOWN_TIME;
        this.mIsCountDown = true;
        removeCallbacks(null);
        run();
    }

    public void stop() {
        removeCallbacks(null);
        this.mIsCountDown = false;
    }

    @Override
    public void run() {
        if (mIsCountDown) {
            String format = String.format(String.valueOf(getContext().getText(R.string.count_down_time)), mCountDownTime);
            this.setText(format);
            postDelayed(this, 1000);
            computeTime();
        } else {
            removeCallbacks(null);
        }
    }

    private void computeTime() {
        if (mCountDownTime == 0) {
            removeCallbacks(null);
            stop();
            if (mOnCountDownEndListener != null) {
                mOnCountDownEndListener.OnCountDownEnd();
            }
        } else {
            mCountDownTime--;
        }
    }

    private OnCountDownEndListener mOnCountDownEndListener;

    public interface OnCountDownEndListener {
        void OnCountDownEnd();
    }

    public void setOnCountDownEndListener(OnCountDownEndListener listener) {
        this.mOnCountDownEndListener = listener;
    }
}
