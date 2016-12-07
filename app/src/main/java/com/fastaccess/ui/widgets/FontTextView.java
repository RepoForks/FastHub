package com.fastaccess.ui.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

import com.fastaccess.R;
import com.fastaccess.helper.TypeFaceHelper;
import com.fastaccess.helper.ViewHelper;


/**
 * Created by Kosh on 8/18/2015. copyrights are reserved
 */
public class FontTextView extends AppCompatTextView {

    public FontTextView(Context context) {
        this(context, null);
    }

    public FontTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FontTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attributeSet) {
        if (attributeSet != null) {
            TypedArray tp = context.obtainStyledAttributes(attributeSet, R.styleable.FontTextView);
            try {
                int color = tp.getColor(R.styleable.FontTextView_drawableColor, -1);
                if (color != -1) {
                    Drawable[] drawables = getCompoundDrawablesRelative();
                    for (Drawable drawable : drawables) {
                        if (drawable == null) continue;
                        ViewHelper.tintDrawable(drawable, color);
                    }
                }
            } finally {
                tp.recycle();
            }
        }
        if (isInEditMode()) return;
        TypeFaceHelper.applyTypeface(this);
    }

    public void setTextColor(@ColorRes int normalColor, @ColorRes int pressedColor) {
        int nColor = ContextCompat.getColor(getContext(), normalColor);
        int pColor = ContextCompat.getColor(getContext(), pressedColor);
        setTextColor(ViewHelper.textSelector(nColor, pColor));
    }

}
