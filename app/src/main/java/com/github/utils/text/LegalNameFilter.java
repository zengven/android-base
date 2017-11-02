package com.github.utils.text;

import android.content.Context;
import android.text.InputFilter;
import android.text.Spanned;

import com.github.utils.RegularUtil;


/**
 * author: zengven
 * date: 2017/8/2 19:31
 * desc: 姓名验证过滤器
 */

public class LegalNameFilter implements InputFilter {

    private int mMax;
    private Context mContext;

    public LegalNameFilter(int max, Context context) {
        mMax = max;
        mContext = context;
    }

    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        int keep = mMax - (dest.length() - (dend - dstart));
        if (keep <= 0) {
            return "";
        } else if (keep >= end - start) {
            if (RegularUtil.isLegalName(dest.toString() + source.toString())) {
                return null; // keep original
            } else {
                if ((dend - dstart) <= 0){
//                    ToastUtil.show(mContext, R.string.notice_input_name_error);
                }
            }
        }
        return null;
    }
}
