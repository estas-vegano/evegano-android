package com.estasvegano.android.estasvegano.view.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import com.estasvegano.android.estasvegano.R;

/**
 * Created by rstk on 12/12/15.
 */
public class AspectHeightFrameLayout extends FrameLayout
{
    private float aspectRatio;

    public AspectHeightFrameLayout(Context context)
    {
        super(context);
        init(context, null);
    }

    public AspectHeightFrameLayout(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init(context, attrs);
    }

    public AspectHeightFrameLayout(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public AspectHeightFrameLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes)
    {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs)
    {
        if (attrs == null) return;

        TypedArray a = context.getTheme().obtainStyledAttributes(attrs,
                R.styleable.AspectHeightFrameLayout, 0, 0);

        try
        {
            aspectRatio = a.getFloat(R.styleable.AspectHeightFrameLayout_aspect_ratio, 1.0f);
        }
        finally
        {
            a.recycle();
        }
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        int width = View.MeasureSpec.getSize(widthMeasureSpec);

        super.onMeasure(
                MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec((int) (width * aspectRatio), MeasureSpec.EXACTLY));

    }
}
