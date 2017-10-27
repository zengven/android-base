package com.github.widget;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

/**
 * author: zengven
 * date: 2017/6/16 10:08
 * desc: RecyclerView分隔线
 */
public class GridItemDecoration extends RecyclerView.ItemDecoration {

    private int mDividerSize = 10;
    private int mDividerMargin;
    private Paint mColorPaint;

    public GridItemDecoration(int dividerColor, int dividerSize) {
        this(dividerColor, dividerSize, 0);
    }

    public GridItemDecoration(int dividerColor, int dividerSize, int dividerMargin) {
        super();
        this.mDividerSize = dividerSize;
        this.mDividerMargin = dividerMargin;
        mColorPaint = new Paint();
        mColorPaint.setColor(dividerColor);
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
        RecyclerView.LayoutManager lm = parent.getLayoutManager();
        if (lm instanceof GridLayoutManager) {
            GridLayoutManager gridLayoutManager = (GridLayoutManager) lm;
            int spanCount = gridLayoutManager.getSpanCount();
            drawVerticalDivider(c, parent, spanCount);
            drawHorizontalDivider(c, parent, spanCount);
        } else if (lm instanceof StaggeredGridLayoutManager) {
            StaggeredGridLayoutManager staggeredGridLayoutManager = (StaggeredGridLayoutManager) lm;
            int spanCount = staggeredGridLayoutManager.getSpanCount();
            drawVerticalDivider(c, parent, spanCount);
            drawHorizontalDivider(c, parent, spanCount);
        }
    }

    private void drawVerticalDivider(Canvas c, RecyclerView parent, int spanCount) {
        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();

            int top = child.getTop() - params.topMargin;
            int bottom = child.getBottom() + params.bottomMargin;

            int left = 0;
            int right = 0;

            if ((i % spanCount) != (spanCount - 1)) { //非最后一列
                if ((i / spanCount) == 0) { //第一行
                    top = top + mDividerMargin;
                }
                if ((i / spanCount) == ((childCount - 1) / spanCount)) { //最后一行
                    bottom = bottom - mDividerMargin;
                }
                left = child.getRight() + params.rightMargin - mDividerSize;
                right = left + mDividerSize;
            }
            if (mColorPaint != null && left != 0 && right != 0) {
                c.drawRect(left, top, right, bottom, mColorPaint);
            }

        }
    }

    private void drawHorizontalDivider(Canvas c, RecyclerView parent, int spanCount) {
        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();

            int left = child.getLeft() - params.leftMargin - mDividerSize;
            int right = child.getRight() + params.rightMargin;
            int top = 0;
            int bottom = 0;

            if ((i / spanCount) != ((childCount - 1) / spanCount)) { //非最后一行
                if (i % spanCount == 0) {
                    left = left + mDividerMargin;
                }
                if (i % spanCount == (spanCount - 1)) {
                    right = right - mDividerMargin;
                }
                top = child.getBottom() + params.bottomMargin;
                bottom = top + mDividerSize;
            }
            if (mColorPaint != null && top != 0 && bottom != 0) {
                c.drawRect(left, top, right, bottom, mColorPaint);
            }
        }
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDrawOver(c, parent, state);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
    }
}
