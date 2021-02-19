package com.example.channelpractise2

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Build
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.view.children
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch


class RandomLayout(context: Context, attrs: AttributeSet?): ViewGroup(context, attrs) {
    private val paint = Paint()
    private val drawableClock = CustomClockDrawable()

    init {
        GlobalScope.launch {
            observeAttachedState().collect { Log.d("AAA", "DIP $it") }
        }

        drawableClock.callback = this

        setWillNotDraw(false)
        paint.color = Color.BLACK
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 10F
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val maxWidthSize = if (MeasureSpec.getMode(widthMeasureSpec) == MeasureSpec.UNSPECIFIED) {
            Int.MAX_VALUE
        } else {
            MeasureSpec.getSize(widthMeasureSpec)
        }

        val maxHeightSize = if (MeasureSpec.getMode(heightMeasureSpec) == MeasureSpec.UNSPECIFIED) {
            Int.MAX_VALUE
        } else {
            MeasureSpec.getSize(heightMeasureSpec)
        }

        for (child in children) {
            val childWidthMeasureSpec = when (child.layoutParams.width) {
                LayoutParams.MATCH_PARENT -> {
                    MeasureSpec.makeMeasureSpec(maxWidthSize, MeasureSpec.EXACTLY)
                }
                LayoutParams.WRAP_CONTENT -> {
                    MeasureSpec.makeMeasureSpec(maxWidthSize, MeasureSpec.AT_MOST)
                }
                else -> {
                    MeasureSpec.makeMeasureSpec(child.layoutParams.width, MeasureSpec.EXACTLY)
                }
            }

            val childHeightMeasureSpec = when (child.layoutParams.height) {
                LayoutParams.MATCH_PARENT -> {
                    MeasureSpec.makeMeasureSpec(maxHeightSize, MeasureSpec.EXACTLY)
                }
                LayoutParams.WRAP_CONTENT -> {
                    MeasureSpec.makeMeasureSpec(maxHeightSize, MeasureSpec.AT_MOST)
                }
                else -> {
                    MeasureSpec.makeMeasureSpec(child.layoutParams.width, MeasureSpec.EXACTLY)
                }
            }

            child.measure(childWidthMeasureSpec, childHeightMeasureSpec)
        }

        val optimalWidth = when (MeasureSpec.getMode(widthMeasureSpec)) {
            MeasureSpec.EXACTLY -> {maxWidthSize}
            else -> {children.maxBy { it.measuredWidth }?.measuredWidth}
        }

        val optimalHeight = when (MeasureSpec.getMode(heightMeasureSpec)) {
            MeasureSpec.EXACTLY -> {maxHeightSize}
            else -> {children.maxBy { it.measuredHeight }?.measuredHeight}
        }

        if (optimalWidth != null && optimalHeight != null) {
            setMeasuredDimension(optimalWidth, optimalHeight)
        }
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        for (child in children) {

            val widthSpace = width - child.measuredWidth
            val heightSpace = height - child.measuredHeight

            val x = (0 .. widthSpace).random()
            val y = (0 .. heightSpace).random()

            child.layout(x, y, x + child.measuredWidth, y + child.measuredHeight)
        }
    }

    override fun dispatchDraw(canvas: Canvas) {
        for (child in children) {
            drawChild(canvas, child, drawingTime)

            canvas.drawRect(child.x, child.y,
                child.x + child.width, child.y + child.height, paint)
        }
    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        if (ev.actionMasked == MotionEvent.ACTION_DOWN && childCount > 0) {
            val destitnationX = ev.x
            val destitnationY = ev.y

            val child = getChildAt(0)

            child.animate()
                .x(destitnationX - child.width / 2)
                .y(destitnationY - child.height / 2)
                .setUpdateListener { invalidate() }

        }

        return super.dispatchTouchEvent(ev)
    }

}

fun View.observeAttachedState(): Flow<Boolean> = flow {
    val channel = Channel<Boolean>(Channel.CONFLATED)
    channel.send(windowToken != null)

    val listener = object : View.OnAttachStateChangeListener {

        override fun onViewAttachedToWindow(v: View?) {
            channel.offer(true)
        }

        override fun onViewDetachedFromWindow(v: View?) {
            channel.offer(false)
        }
    }
    addOnAttachStateChangeListener(listener)

    try {
        for (value in channel) {
            emit(value)
        }
    } finally {
        removeOnAttachStateChangeListener(listener)
    }
}