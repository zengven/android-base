package com.github.utils;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * author: zengven
 * date: 2017/6/21 17:08
 * desc: view 工具类
 */
public class ViewUtil {

    /**
     * reset view size, the width is screen width
     *
     * @param view
     * @param ratio 宽高比
     */
    public static void resetSize(View view, float ratio) {
        resetSize(view, DisplayUtil.getScreenWidth(view.getContext()), ratio);
    }

    /**
     * reset view size
     *
     * @param view
     * @param width new width
     * @param ratio 宽高比
     */
    public static void resetSize(View view, int width, float ratio) {
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.width = width;
        layoutParams.height = (int) (width / ratio);
        view.setLayoutParams(layoutParams);
    }

    /**
     * 自动在没行末尾加上换行符,用于处理中英文排版参差不齐现象
     *
     * @param textView
     * @param content
     * @return
     */
    public static String autoSplitText(TextView textView, String content) {
        final Paint tvPaint = textView.getPaint(); //paint，包含字体等信息
        final float tvWidth = textView.getWidth() - textView.getPaddingLeft() - textView.getPaddingRight(); //控件可用宽度
        //将原始文本按行拆分
        String[] rawTextLines = content.replaceAll("\r", "").split("\n");
        StringBuilder sbNewText = new StringBuilder();
        for (String rawTextLine : rawTextLines) {
            if (tvPaint.measureText(rawTextLine) <= tvWidth) {
                //如果整行宽度在控件可用宽度之内，就不处理了
                sbNewText.append(rawTextLine);
            } else {
                //如果整行宽度超过控件可用宽度，则按字符测量，在超过可用宽度的前一个字符处手动换行
                float lineWidth = 0;
                for (int cnt = 0; cnt != rawTextLine.length(); ++cnt) {
                    char ch = rawTextLine.charAt(cnt);
                    lineWidth += tvPaint.measureText(String.valueOf(ch));
                    if (lineWidth <= tvWidth) {
                        sbNewText.append(ch);
                    } else {
                        sbNewText.append("\n");
                        lineWidth = 0;
                        --cnt;
                    }
                }
            }
            sbNewText.append("\n");
        }
        //把结尾多余的\n去掉
        if (!content.endsWith("\n")) {
            sbNewText.deleteCharAt(sbNewText.length() - 1);
        }
        return sbNewText.toString();
    }

    /**
     * 获取resid获取原生Drawable
     *
     * @param context
     * @param resid
     * @return
     */
    public static Drawable getDrawableFromAttr(Context context, int resid) {
        TypedValue typedValue = new TypedValue();
        context.getTheme().resolveAttribute(resid, typedValue, true);
        int[] attribute = new int[]{resid};
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(typedValue.resourceId, attribute);
        return typedArray.getDrawable(0);
    }

    private static final long INTERVAL_TIME = 800; //两次点击间隔最小800ms

    /**
     * 是否为快速点击
     *
     * @return
     */
    public synchronized static boolean isFastClick(View view) {
        long currentTimeMillis = System.currentTimeMillis();
        long lastTimeMillis = view.getTag() == null ? 0 : (Long) view.getTag();
        boolean isFastClick;
        if (currentTimeMillis - lastTimeMillis > INTERVAL_TIME) {
            isFastClick = false;
        } else {
            isFastClick = true;
        }
        view.setTag(currentTimeMillis);
        return isFastClick;
    }
}
