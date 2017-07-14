package com.estasvegano.android.estasvegano.view.widget

import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import com.estasvegano.android.estasvegano.R

class AspectHeightFrameLayout : FrameLayout {

    private var aspectRatio: Float = 1F

    constructor(context: Context) : super(context) {
        init(context, null)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context, attrs)
    }

    constructor(
            context: Context,
            attrs: AttributeSet,
            defStyleAttr: Int
    ) : super(context, attrs, defStyleAttr) {
        init(context, attrs)
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(
            context: Context,
            attrs: AttributeSet,
            defStyleAttr: Int,
            defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes) {
        init(context, attrs)
    }

    private fun init(context: Context, attrs: AttributeSet?) {
        if (attrs == null) return

        val a = context.theme.obtainStyledAttributes(attrs,
                R.styleable.AspectHeightFrameLayout, 0, 0)

        try {
            aspectRatio = a.getFloat(R.styleable.AspectHeightFrameLayout_aspect_ratio, 1f)
        } finally {
            a.recycle()
        }
    }

    public override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val width = View.MeasureSpec.getSize(widthMeasureSpec)

        super.onMeasure(
                View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY),
                View.MeasureSpec.makeMeasureSpec((width * aspectRatio).toInt(), View.MeasureSpec.EXACTLY))

    }
}
