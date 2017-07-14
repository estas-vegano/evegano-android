package com.estasvegano.android.estasvegano.view.util

import android.graphics.*

import com.squareup.picasso.Transformation

class PicassoCircleBorderTransform(private val borderColor: Int, private val borderWidth: Int) : Transformation {

    override fun transform(source: Bitmap): Bitmap {
        val size = Math.min(source.width, source.height)

        val x = (source.width - size) / 2
        val y = (source.height - size) / 2

        val squaredBitmap = Bitmap.createBitmap(source, x, y, size, size)
        if (squaredBitmap != source) {
            source.recycle()
        }

        val bitmap = Bitmap.createBitmap(size, size, source.config)

        val canvas = Canvas(bitmap)
        val paint = Paint()
        val shader = BitmapShader(squaredBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
        paint.shader = shader
        paint.isAntiAlias = true

        val r = size / 2f
        canvas.drawCircle(r, r, r, paint)

        paint.shader = null
        paint.color = borderColor
        paint.strokeWidth = borderWidth.toFloat()
        paint.style = Paint.Style.STROKE

        canvas.drawCircle(r, r, r - borderWidth / 2, paint)

        squaredBitmap.recycle()
        return bitmap
    }

    override fun key(): String {
        return "circle/$borderColor/$borderWidth"
    }
}