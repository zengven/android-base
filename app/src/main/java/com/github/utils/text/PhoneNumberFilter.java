package com.github.utils.text;

import android.content.Context;
import android.text.InputFilter;
import android.text.Spanned;

import com.huijiayou.huijiayou.R;
import com.huijiayou.huijiayou.utils.RegularUtil;
import com.huijiayou.huijiayou.utils.ToastUtils;

/**
 * author: zengven
 * date: 2017/8/2 18:10
 * desc: 电话号码输入过滤器
 */
public class PhoneNumberFilter implements InputFilter {

    private Context mContext;
    private int mMax;

    public PhoneNumberFilter(int max, Context context) {
        this.mContext = context;
        this.mMax = max;
    }

    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        int keep = mMax - (dest.length() - (dend - dstart));
        if (keep <= 0) {
            return "";
        } else if (keep <= 1) {
            if (RegularUtil.isMobileSimple(dest.toString() + source.toString())) {
                return null; // keep original
            } else {
                if ((dend - dstart) <= 0)
                    ToastUtils.show(mContext, R.string.notice_input_phonenum_error);
            }
        } else if (keep >= end - start) {
            return null;
        }
        return null;
    }
}
