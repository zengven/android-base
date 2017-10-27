package com.github.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Region;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatEditText;
import android.text.InputType;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.github.R;

/**
 * author: zengven
 * date: 2017/10/1 17:15
 * desc: 实现功能区域清除内容,密码明暗文切换
 */
public class SuperEditText extends AppCompatEditText {

    private Drawable mDrawable;
    private int mIntrinsicWidth;
    private int mIntrinsicHeight;
    private int mPadding; //设置图片padding
    private boolean mIsVisible;
    private int mOriginalInputType;

    public SuperEditText(Context context) {
        this(context, null);

    }

    public SuperEditText(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.editTextStyle);
        //必须加R.attr.editTextStyle,否则无法输入
    }

    public SuperEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if (attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SuperEditText);
            mDrawable = typedArray.getDrawable(R.styleable.SuperEditText_src);
            mPadding = (int) typedArray.getDimension(R.styleable.SuperEditText_src_padding, 0);
            typedArray.recycle();
        }
        init();
    }

    private void init() {
        mIsVisible = mDrawable != null;
        if (mIsVisible) {
            mIntrinsicWidth = mDrawable.getIntrinsicWidth();
            mIntrinsicHeight = mDrawable.getIntrinsicHeight();
            mDrawable.setBounds(0, 0, mIntrinsicWidth, mIntrinsicHeight);
            this.setPadding(getPaddingLeft(), getPaddingTop(), (getPaddingRight() + mIntrinsicWidth) + 2 * mPadding, getPaddingBottom());
        }
        mOriginalInputType = getInputType();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mIsVisible) {
            int width = getWidth();
            int height = getHeight();
            canvas.translate(width + getScrollX() - mIntrinsicWidth - mPadding, (height - mIntrinsicHeight) / 2); //移动canvas原点坐标
            mDrawable.draw(canvas);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!mIsVisible) {
            return super.onTouchEvent(event);
        }
        Region region = createRegion();
        if (region.contains((int) event.getX(), (int) event.getY())) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_UP:
                    if (mOnFunctionClickListener != null) {
                        mOnFunctionClickListener.onRightFunctionClick();
                    }
                    break;
            }
            return true;
        }
        return super.onTouchEvent(event);
    }

    private Region createRegion() {
        int left = getWidth() - mIntrinsicWidth - mPadding;
        int top = (getHeight() - mIntrinsicHeight) / 2 - mPadding;
        int right = getWidth() + mPadding;
        int buttom = (getHeight() - mIntrinsicHeight) / 2 + mIntrinsicHeight + mPadding;
        return new Region(left, top, right, buttom);
    }

    private OnFunctionClickListener mOnFunctionClickListener;

    public void setOnFunctionClickListener(OnFunctionClickListener listener) {
        mOnFunctionClickListener = listener;
    }

    public interface OnFunctionClickListener {
        void onRightFunctionClick();
    }

    /**
     * 设置edit是否可输入
     *
     * @param editable
     */
    public void setEditTextenable(boolean editable) {
        if (!editable) { // disable editing password
            setFocusable(false);
            setFocusableInTouchMode(false); // user touches widget on phone with touch screen
            setClickable(false); // user navigates with wheel and selects widget
            setInputType(InputType.TYPE_NULL);
        } else { // enable editing of password
            setFocusable(true);
            setFocusableInTouchMode(true);
            setClickable(true);
            setInputType(mOriginalInputType);
        }
    }

}
