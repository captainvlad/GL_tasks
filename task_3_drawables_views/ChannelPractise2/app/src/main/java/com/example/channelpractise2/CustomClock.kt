package com.example.channelpractise2

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.SystemClock
import android.util.AttributeSet
import android.view.View
import kotlin.math.cos
import kotlin.math.sin

class CustomClock(context: Context, attrs: AttributeSet?): View(context, attrs) {
    private val standardMargin = 50
    private val numbersCoords = listOf(90.toDouble() to 3, 180.toDouble() to 6, 270.toDouble() to 9, 354.toDouble() to 12)
    private val paint = Paint()

    override fun onDraw(canvas: Canvas) {
        paint.color = Color.BLACK
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 10F
        paint.textSize = 80F

        val borderRadius = (measuredWidth.coerceAtMost(measuredHeight) / 2).toFloat() - standardMargin
        val numbersRadius = borderRadius - standardMargin * 3
        val centerX = (measuredWidth / 2).toFloat()
        val centerY = (measuredHeight / 2).toFloat()

        val internalMarkLength = (borderRadius * 0.9).toFloat()
        canvas.drawCircle(centerX,centerY,borderRadius, paint)

        for (i in 0 until 360 step 6) {
            val lineSin = sin( Math.toRadians(i.toDouble()) )
            val lineCos = cos( Math.toRadians(i.toDouble()) )

            canvas.drawLine(
                (centerX + borderRadius * lineSin).toFloat(), (centerY - borderRadius * lineCos).toFloat(),
                (centerX - borderRadius * lineSin).toFloat(), (centerY + borderRadius * lineCos).toFloat(), paint)
        }

        paint.style = Paint.Style.FILL_AND_STROKE
        canvas.drawCircle(centerX,centerY,internalMarkLength, paint)
        paint.color = Color.GRAY

        for (number in numbersCoords) {
            val numberSin = Math.sin( Math.toRadians(number.first) )
            val numberCos = Math.cos( Math.toRadians(number.first) )

            canvas.drawText("${number.second}",
                ((centerX + numbersRadius * numberSin).toFloat()),
                ((centerY - numbersRadius * numberCos).toFloat()), paint
            )
        }

        val angles = listOf(( drawingTime % 1000000) * 360 / 60 / 1000, (drawingTime % 1000) * 360 / 1000)
//        SystemClock.uptimeMillis()

        for (angle in angles) {
            val arrowSin = Math.sin( Math.toRadians(angle.toDouble()) )
            val arrowCos = Math.cos( Math.toRadians(angle.toDouble()) )

            canvas.drawLine(
                (centerX + borderRadius * arrowCos).toFloat(), (centerY + borderRadius * arrowSin).toFloat(),
                centerX, centerY, paint)
        }

        invalidate()
    }

}