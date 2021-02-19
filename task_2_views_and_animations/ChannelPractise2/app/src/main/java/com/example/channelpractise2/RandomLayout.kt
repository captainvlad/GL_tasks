package com.example.channelpractise2

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.core.view.children
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch


class RandomLayout(context: Context, attrs: AttributeSet?): ViewGroup(context, attrs) {

    init {
        children.forEach { it.measure(it.layoutParams.width, it.layoutParams.height) }
        GlobalScope.launch {
            viewIsAttached().collect { Log.d("AAA", "DIP $it") }
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val maxWidthSize = when (MeasureSpec.getMode(widthMeasureSpec)) {
            MeasureSpec.AT_MOST -> {
                MeasureSpec.getSize(widthMeasureSpec)
            }
            MeasureSpec.UNSPECIFIED -> {
                Int.MAX_VALUE
            }
            else -> {
                MeasureSpec.getSize(widthMeasureSpec)
            }
        }

        val maxHeightSize = when (MeasureSpec.getMode(heightMeasureSpec)) {
            MeasureSpec.AT_MOST -> {
                MeasureSpec.getSize(heightMeasureSpec)
            }
            MeasureSpec.UNSPECIFIED -> {
                Int.MAX_VALUE
            }
            else -> {
                MeasureSpec.getSize(heightMeasureSpec)
            }
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

        setMeasuredDimension(maxWidthSize, maxHeightSize)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        for (child in children) {

            val widthSpace = measuredWidth - child.measuredWidth
            val heightSpace = measuredHeight - child.measuredHeight

            val x = (0 .. widthSpace).random()
            val y = (0 .. heightSpace).random()

            child.layout(x, y, x + child.measuredWidth, y + child.measuredHeight)
        }
    }

    override fun dispatchDraw(canvas: Canvas) {
        for (child in children) {
            drawChild(canvas, child, drawingTime)
        }
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        if (ev.actionMasked == MotionEvent.ACTION_DOWN) {
            val destinationX = ev.x
            val destinationY = ev.y

            val child = getChildAt(0)
            child.animate().x(destinationX - child.width / 2)
                .y(destinationY - child.height / 2)

        }

        return super.dispatchTouchEvent(ev)
    }

}

fun View.viewIsAttached(): Flow<Boolean> = flow {
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