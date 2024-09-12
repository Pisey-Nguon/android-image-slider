package com.digitaltalent.page_indicator

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat

class PageIndicator @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val circlePaint: Paint
    private val selectedCirclePaint: Paint

    var index: Int = 0
        set(value) {
            field = value
            invalidate()
        }

    var length: Int = 0
        set(value) {
            field = value
            requestLayout()
        }

    private val circleRadius: Float
    private val circleSpacing: Float

    init {
        val typedArray = context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.PageIndicator,
            0,
            0
        )

        try {
            val defaultCircleColor = ContextCompat.getColor(context, android.R.color.darker_gray)
            val defaultSelectedCircleColor = ContextCompat.getColor(context, android.R.color.holo_blue_dark)
            val circleColor = typedArray.getColor(R.styleable.PageIndicator_circleColor, defaultCircleColor)
            val selectedCircleColor = typedArray.getColor(R.styleable.PageIndicator_selectedCircleColor, defaultSelectedCircleColor)

            circlePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                style = Paint.Style.FILL
                color = circleColor
            }

            selectedCirclePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                style = Paint.Style.FILL
                color = selectedCircleColor
            }

            circleRadius = typedArray.getDimension(R.styleable.PageIndicator_circleRadius, 10f)
            circleSpacing = typedArray.getDimension(R.styleable.PageIndicator_circleSpacing, 8f)
        } finally {
            typedArray.recycle()
        }

        // Set a sample length for preview in XML
        if (isInEditMode) {
            length = 5
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (length <= 0) return

        // Total width of all indicators and spacing
        val totalWidth = (circleRadius * 2 * length) + (circleSpacing * (length - 1))

        // Calculate the start position to ensure centering
        val startX = (width - totalWidth) / 2f + circleRadius // Add circleRadius to adjust for correct centering
        val centerY = height / 2f

        for (i in 0 until length) {
            val x = startX + i * (circleRadius * 2 + circleSpacing)
            canvas.drawCircle(x, centerY, circleRadius, if (i == index) selectedCirclePaint else circlePaint)
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        // Calculate the desired width based on the number of indicators, their size, and the spacing
        val desiredWidth = (circleRadius * 2 * length + circleSpacing * (length - 1)).toInt()
        val desiredHeight = (circleRadius * 2).toInt()

        // Ensure the view's width matches the desired width, while respecting the parent's constraints
        val width = resolveSize(desiredWidth, widthMeasureSpec)
        val height = resolveSize(desiredHeight, heightMeasureSpec)

        // Set the measured dimensions
        setMeasuredDimension(width, height)
    }
}
