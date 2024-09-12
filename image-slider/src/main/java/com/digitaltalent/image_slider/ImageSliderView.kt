package com.digitaltalent.image_slider

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.RelativeLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView

class ImageSliderView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : RelativeLayout(context, attrs, defStyleAttr) {

    private val recyclerView: RecyclerView
    private val handler: Handler = Handler(Looper.getMainLooper())
    private var delayMillis: Long = 0L
    private var isAutoScrollEnabled: Boolean = false
    private var autoScrollRunnable: Runnable? = null

    init {
        val view = LayoutInflater.from(context).inflate(R.layout.view_image_slider, this, false)
        recyclerView = view.findViewById(R.id.recyclerView)
        addView(view)
    }

    private fun startAutoScroll() {
        if(delayMillis == 0L) return
        if (isAutoScrollEnabled) return
        isAutoScrollEnabled = true
        autoScrollRunnable = object : Runnable {
            override fun run() {
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val currentPosition = layoutManager.findFirstVisibleItemPosition()
                val nextPosition = (currentPosition + 1) % (recyclerView.adapter?.itemCount ?: 1)
                recyclerView.smoothScrollToPosition(nextPosition)
                handler.postDelayed(this, delayMillis)
            }
        }
        autoScrollRunnable?.let { handler.postDelayed(it, delayMillis) }
    }

    private fun stopAutoScroll() {
        isAutoScrollEnabled = false
        autoScrollRunnable?.let { handler.removeCallbacks(it) }
    }

    fun setImages(
        images: List<String>,
        itemMarginHorizontal: Float = 0f,
        itemCircularRadius: Float = 0f,
        onSnapPositionChangeListener: OnSnapPositionChangeListener
    ) {
        val snapHelper = PagerSnapHelper()
        val adapter = ImageSliderAdapter(images, itemMarginHorizontal, itemCircularRadius)
        val layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter
        snapHelper.attachToRecyclerView(recyclerView)
        recyclerView.addOnScrollListener(
            SnapOnScrollListener(
                snapHelper = snapHelper,
                count = images.size,
                onSnapPositionChangeListener = onSnapPositionChangeListener,
                onScrollStateChangedListener = object : OnSnapStateChangedListener {
                    override fun onSnapStateChange(newState: Int) {
                        when (newState) {
                            RecyclerView.SCROLL_STATE_IDLE -> startAutoScroll()
                            RecyclerView.SCROLL_STATE_DRAGGING -> stopAutoScroll()
                        }
                    }
                }
            )
        )
    }

    fun setAutoScroll(delayMillis: Long) {
        this.delayMillis = delayMillis
        if (isAutoScrollEnabled) {
            stopAutoScroll()
            startAutoScroll()
        } else {
            startAutoScroll()
        }
    }
}
