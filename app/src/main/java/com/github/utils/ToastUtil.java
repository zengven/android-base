package com.github.utils;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;


/**
 * author: zengven
 * date: 2017/6/7
 * Desc: toast工具类
 */
public class ToastUtil {
    private static Toast sToast;

    private ToastUtil() {
    }

    public static void show(Context context, int resId) {
        show(context, resId, Toast.LENGTH_SHORT);
    }

    public static void show(Context context, CharSequence text) {
        show(context, text, Toast.LENGTH_SHORT);
    }

    public static void show(Context context, CharSequence text, int len) {
        if (context == null)
            return;
        if (sToast == null) {
            sToast = Toast.makeText(context.getApplicationContext(), text, len);
            sToast.setGravity(Gravity.CENTER, 0, 0);
//            LinearLayout linearLayout = (LinearLayout) sToast.getView();
//            TextView messageTextView = (TextView) linearLayout.getChildAt(0);
//            messageTextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
        } else {
            sToast.setDuration(len);
            sToast.setText(text);
        }
        sToast.show();
    }

    public static void show(Context context, int resId, int len) {
        if (context == null)
            return;
        if (sToast == null) {
            sToast = Toast.makeText(context.getApplicationContext(), resId, len);
            sToast.setGravity(Gravity.CENTER, 0, 0);
//            LinearLayout linearLayout = (LinearLayout) sToast.getView();
//            TextView messageTextView = (TextView) linearLayout.getChildAt(0);
//            messageTextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
        } else {
            sToast.setDuration(len);
            sToast.setText(resId);
        }
        sToast.show();
    }

    public static void cancel() {
        if (null != sToast) {
            sToast.cancel();
        }
    }


    /**
     *
     * @param context  The Class's context , where  want to use this tool
     * @param message  The message will be show
     */
    @Deprecated
    public static Toast createNormalToast(Context context , String message){
        if (sToast == null) {
            sToast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
            sToast.setGravity(Gravity.CENTER, 0, 0);
            sToast.show();
        }else{
            sToast.setText(message);
            sToast.setGravity(Gravity.CENTER, 0, 0);
            sToast.show();
        }
        return sToast;
    }

    @Deprecated
    public static Toast createLongToast(Context context , String message){
        if (sToast == null) {
            sToast = Toast.makeText(context, message, Toast.LENGTH_LONG);
            sToast.setGravity(Gravity.CENTER, 0, 0);
            sToast.show();
        }else{
            sToast.setText(message);
            sToast.setGravity(Gravity.CENTER, 0, 0);
            sToast.show();
        }
        return sToast;
    }

}
