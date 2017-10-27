package com.github.widget.LoopViewPager;

import android.content.Context;
import android.database.DataSetObserver;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.github.R;
import com.github.base.adapter.BasePagerAdapter;
import com.github.utils.DisplayUtil;

import java.lang.reflect.Field;
import java.util.List;

/**
 * author: zengven
 * date: 2017/6/16 14:51
 * desc: 轮播图.指示器
 */
public class LoopViewPager extends FrameLayout {

    private static final int HANDE_CODE = 1;
    private static final int MSG_DELAY = 2500;
    private ViewPager mViewPager;
    private LinearLayout mIndicatorContainer;
    private View mPreIndicator;
    private BasePagerAdapter mPagerAdapter;

    public LoopViewPager(@NonNull Context context) {
        this(context, null);
    }

    public LoopViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoopViewPager(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        mViewPager = new ViewPager(getContext());
//        mViewPager.setPageTransformer(true, new ZoomOutPageTransformer());
        this.addView(mViewPager);

        mIndicatorContainer = new LinearLayout(getContext());
        LayoutParams layoutParams = new LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM;
        layoutParams.bottomMargin = DisplayUtil.dip2px(getContext(), 15);
        mIndicatorContainer.setOrientation(LinearLayout.HORIZONTAL);
        this.addView(mIndicatorContainer, layoutParams);

        try {
            Field mField = ViewPager.class.getDeclaredField("mScroller");
            mField.setAccessible(true);
            LoopScroller mScroller = new LoopScroller(getContext());
            mScroller.setDuration(1000);
            mField.set(mViewPager, mScroller);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == HANDE_CODE) {
                if (mPagerAdapter == null || mPagerAdapter.getList() == null || mPagerAdapter.getList().size() <= 1)
                    return;
                if (mViewPager.getCurrentItem() < Short.MAX_VALUE - 1) {
                    mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1, true);
                    removeCallbacksAndMessages(null);
                    sendEmptyMessageDelayed(HANDE_CODE, MSG_DELAY);
                }
            }
        }
    };

    /**
     * Set a PagerAdapter that will supply views for this pager as needed.
     *
     * @param adapter Adapter to use
     */
    public void setAdapter(final BasePagerAdapter adapter) {
        mPagerAdapter = adapter;
        mViewPager.setAdapter(adapter);
        createIndicator(adapter);
        adapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                createIndicator(adapter);
            }
        });
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                if (mPagerAdapter != null && mPagerAdapter.getList() != null && !mPagerAdapter.getList().isEmpty()) {
                    View currentIndicator = mIndicatorContainer.getChildAt(position % mPagerAdapter.getList().size());
                    currentIndicator.setSelected(true);
                    if (mPreIndicator != null) {
                        mPreIndicator.setSelected(false);
                    }
                    mPreIndicator = currentIndicator;
                }
            }
        });
        mViewPager.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        stopLoop();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        stopLoop();
                        break;
                    case MotionEvent.ACTION_UP:
                        startLoop();
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        startLoop();
                    default:
                        break;
                }
                return false;
            }
        });
    }

    /**
     * create Indicator
     *
     * @param adapter
     */
    private void createIndicator(BasePagerAdapter adapter) {
        if (null == adapter.getList() || adapter.getList().isEmpty())
            return;
        mIndicatorContainer.setVisibility(adapter.getList().size() <= 1 ? INVISIBLE : VISIBLE);
        mIndicatorContainer.removeAllViews();
        List adapterList = adapter.getList();
        if (adapterList != null && !adapterList.isEmpty()) {
            for (int i = 0; i < adapterList.size(); i++) {
                View indicator = new View(getContext());
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(DisplayUtil.dip2px(getContext(), 8), DisplayUtil.dip2px(getContext(), 8));
                if (i != 0) {
                    if (Build.VERSION.SDK_INT >= 17) {
                        params.setMarginStart(DisplayUtil.dip2px(getContext(), 10));
                    } else {
                        params.leftMargin = DisplayUtil.dip2px(getContext(), 10);
                    }
                }
                indicator.setBackgroundResource(R.drawable.selector_indicator);
                mIndicatorContainer.addView(indicator, params);
            }
        }
        int index = Short.MAX_VALUE / 2 - (Short.MAX_VALUE / 2) % adapter.getList().size();
        mViewPager.setCurrentItem(index);
        mPreIndicator = mIndicatorContainer.getChildAt(index % adapter.getList().size());
        mPreIndicator.setSelected(true);
        startLoop();
    }

    /**
     * 重置指示器状态
     */
    private void resetIndicatorStatus() {
        int childCount = mIndicatorContainer.getChildCount();
        for (int i = 0; i < childCount; i++) {
            mIndicatorContainer.getChildAt(i).setSelected(false);
        }
    }

    public void startLoop() {
        mHandler.removeCallbacksAndMessages(null);
        mHandler.sendEmptyMessageDelayed(HANDE_CODE, MSG_DELAY);
    }

    public void stopLoop() {
        mHandler.removeCallbacksAndMessages(null);
    }

}
