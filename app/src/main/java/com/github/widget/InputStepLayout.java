package com.github.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.github.R;

/**
 * author: zengven
 * date: 2017/10/16 14:41
 * desc: 数字按步输入
 */
public class InputStepLayout extends LinearLayout {

    private int mPaneNum = 6;//默认6格
    private int mPaneWidth; //default 80px
    private int mPaneHeight; //default 80px
    private int mPanePadding; //default 20px
    private int mBackgroundResId;
    private int mTextColor;
    private float mTextSize;

    public InputStepLayout(Context context) {
        this(context, null);
    }

    public InputStepLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public InputStepLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(context, attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public InputStepLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initAttrs(context, attrs);
    }

    /**
     * @param context
     * @param attrs
     */
    private void initAttrs(Context context, AttributeSet attrs) {
        if (attrs == null)
            return;
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.InputStepLayout);
        mPaneNum = typedArray.getInt(R.styleable.InputStepLayout_pane_num, 6);
        mPaneWidth = (int) typedArray.getDimension(R.styleable.InputStepLayout_pane_width, 80);
        mPaneHeight = (int) typedArray.getDimension(R.styleable.InputStepLayout_pane_height, 80);
        mPanePadding = (int) typedArray.getDimension(R.styleable.InputStepLayout_pane_padding, 20);
        mBackgroundResId = typedArray.getResourceId(R.styleable.InputStepLayout_pane_background, 0);
        mTextColor = typedArray.getColor(R.styleable.InputStepLayout_pane_textColor, 0);
        mTextSize = typedArray.getDimensionPixelSize(R.styleable.InputStepLayout_pane_textSize, 15);
        typedArray.recycle();
        addInputView();
    }

    /**
     * add edittext
     */
    private void addInputView() {
        this.setOrientation(HORIZONTAL);
        for (int i = 0; i < mPaneNum; i++) {
            EditText editText = createEditText(i);
            this.addView(editText);
        }
    }

    /**
     * create edittext
     *
     * @param index
     * @return
     */
    private EditText createEditText(int index) {
        final EditText editText = new EditText(getContext());
        editText.setInputType(InputType.TYPE_CLASS_NUMBER);
        editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(1)}); //限制输入1个字符
        editText.addTextChangedListener(new InputTextWatcher(this, editText));
        editText.setOnKeyListener(new OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_DEL) {
                    String content = editText.getText().toString();
                    if (TextUtils.isEmpty(content)) {
                        int index = InputStepLayout.this.indexOfChild(editText);
                        if ((index - 1) >= 0) {
                            EditText view = (EditText) InputStepLayout.this.getChildAt(index - 1);
                            view.setFocusable(true);
                            view.setFocusableInTouchMode(true);
                            view.requestFocus();
                        }
                    }
                }
                return false;
            }
        });
        if (mBackgroundResId != 0) {
            editText.setBackgroundResource(mBackgroundResId);
        }
        if (mTextColor != 0) {
            editText.setTextColor(mTextColor);
        }
        if (mTextSize != 0) {
            editText.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTextSize);
        }
        LayoutParams layoutParams = new LayoutParams(mPaneWidth, mPaneHeight);
        if (index != 0) {
            layoutParams.leftMargin = mPanePadding;
        }
        layoutParams.gravity = Gravity.CENTER;
        editText.setGravity(Gravity.CENTER);
        editText.setLayoutParams(layoutParams);
        return editText;
    }

    private static class InputTextWatcher implements TextWatcher {

        private InputStepLayout inputStepLayout;
        private EditText editText;

        public InputTextWatcher(InputStepLayout inputStepLayout, EditText editText) {
            this.inputStepLayout = inputStepLayout;
            this.editText = editText;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (before == 0) { //输入
                int index = inputStepLayout.indexOfChild(editText);
                if ((index + 1) < inputStepLayout.getChildCount()) {
                    EditText view = (EditText) inputStepLayout.getChildAt(index + 1);
                    view.setFocusable(true);
                    view.setFocusableInTouchMode(true);
                    view.requestFocus();
                }
            } else if (before == 1) { //删除
                int index = inputStepLayout.indexOfChild(editText);
                if ((index - 1) >= 0) {
                    EditText view = (EditText) inputStepLayout.getChildAt(index - 1);
                    view.setFocusable(true);
                    view.setFocusableInTouchMode(true);
                    view.requestFocus();
                }
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    }

    /**
     * get input text
     *
     * @return
     */
    public String getText() {
        StringBuilder content = new StringBuilder();
        for (int i = 0; i < getChildCount(); i++) {
            EditText view = (EditText) getChildAt(i);
            String charStr = view.getText().toString().trim();
            content.append(charStr);
        }
        return content.toString();
    }

    /**
     * get pane num
     *
     * @return
     */
    public int getPaneNum() {
        return mPaneNum;
    }
}
