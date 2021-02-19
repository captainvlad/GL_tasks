package com.example.channelpractise2

import android.graphics.*
import android.graphics.drawable.Drawable
import java.util.*
import kotlin.math.cos
import kotlin.math.sin

class CustomClockDrawable: Drawable() {
    private val standardMargin = 50
    private val numbersCoords = listOf(90.toDouble() to 3, 180.toDouble() to 6, 270.toDouble() to 9, 354.toDouble() to 12)
    private val paint = Paint()

    override fun draw(canvas: Canvas) {
        paint.color = Color.BLACK
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 10F
        paint.textSize = 80F

        val allowedWidth = bounds.width()
        val allowedHeight = bounds.height()


        val borderRadius = ((allowedHeight.coerceAtMost(allowedWidth) / 2) - standardMargin).toFloat()
        val numbersRadius = borderRadius - standardMargin * 3
        val centerX = (allowedWidth / 2).toFloat()
        val centerY = (allowedHeight / 2).toFloat()

        val internalMarkLength = (borderRadius * 0.9).toFloat()
        canvas.drawCircle(centerX,centerY,borderRadius, paint)

        for (i in 0 until 360 step 6) {
            var lineSin = sin( Math.toRadians(i.toDouble()) )
            var lineCos = cos( Math.toRadians(i.toDouble()) )

            canvas.drawLine(
                (centerX + borderRadius * lineSin).toFloat(), (centerY - borderRadius * lineCos).toFloat(),
                (centerX - borderRadius * lineSin).toFloat(), (centerY + borderRadius * lineCos).toFloat(), paint)
        }

        paint.style = Paint.Style.FILL_AND_STROKE
        canvas.drawCircle(centerX,centerY,internalMarkLength, paint)

        paint.color = Color.GRAY

        for (number in numbersCoords) {
            val numberSin = sin( Math.toRadians(number.first) )
            val numberCos = cos( Math.toRadians(number.first) )

            canvas.drawText("${number.second}",
                ((centerX + numbersRadius * numberSin).toFloat()),
                ((centerY - numbersRadius * numberCos).toFloat()), paint
            )
        }

        val angles = listOf( Calendar.getInstance().get(Calendar.SECOND) * 6,
            Calendar.getInstance().get(Calendar.MILLISECOND) )

        for (angle in angles) {
            val arrowSin = sin( Math.toRadians(angle.toDouble()) )
            val arrowCos = cos( Math.toRadians(angle.toDouble()) )

            canvas.drawLine(
                (centerX + borderRadius * arrowCos).toFloat(), (centerY + borderRadius * arrowSin).toFloat(),
                centerX, centerY, paint)
        }

        invalidateSelf()
    }

    override fun setAlpha(alpha: Int) {}

    override fun setColorFilter(colorFilter: ColorFilter?) {}

    override fun getOpacity(): Int {
        return PixelFormat.TRANSLUCENT
    }

}