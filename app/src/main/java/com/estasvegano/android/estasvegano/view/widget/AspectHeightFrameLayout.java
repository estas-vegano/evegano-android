package com.estasvegano.android.estasvegano.view.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import com.estasvegano.android.estasvegano.R;

public class AspectHeightFrameLayout extends FrameLayout {
    private float aspectRatio;

    public AspectHeightFrameLayout(@NonNull Context context) {
        super(context);
        init(context, null);
    }

    public AspectHeightFrameLayout(@NonNull Context context, @NonNull AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public AspectHeightFrameLayout(
            @NonNull Context context,
            @NonNull AttributeSet attrs,
            @NonNull int defStyleAttr
    ) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public AspectHeightFrameLayout(
            @NonNull Context context,
            @NonNull AttributeSet attrs,
            int defStyleAttr,
            int defStyleRes
    ) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    private void init(@NonNull Context context, @Nullable AttributeSet attrs) {
        if (attrs == null) return;

        TypedArray a = context.getTheme().obtainStyledAttributes(attrs,
                R.styleable.AspectHeightFrameLayout, 0, 0);

        try {
            aspectRatio = a.getFloat(R.styleable.AspectHeightFrameLayout_aspect_ratio, 1.0f);
        } finally {
            a.recycle();
        }
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = View.MeasureSpec.getSize(widthMeasureSpec);

        super.onMeasure(
                MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec((int) (width * aspectRatio), MeasureSpec.EXACTLY));

    }
}
