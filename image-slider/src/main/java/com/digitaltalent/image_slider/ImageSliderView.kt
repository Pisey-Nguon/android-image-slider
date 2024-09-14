package com.digitaltalent.image_slider

import android.annotation.SuppressLint
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ImageView.ScaleType
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
    private val snapHelper = PagerSnapHelper()
    private var imageSliderAdapter: ImageSliderAdapter? = null

    // Separated Properties
    private var scaleType: ScaleType = ScaleType.CENTER
    private var itemMarginHorizontal: Float = 0f
    private var itemCircularRadius: Float = 0f
    private var onSnapPositionChangeListener: ((Int) -> Unit)? = null
    private var onScrollStateChangedListener: ((Int) -> Unit)? = null
    private var onClickedListener: ((String) -> Unit)? = null

    init {
        recyclerView = inflateView()
    }

    private fun inflateView(): RecyclerView {
        val view = LayoutInflater.from(context).inflate(R.layout.view_image_slider, this, false)
        addView(view)
        return view.findViewById(R.id.recyclerView)
    }

    // Separated Setters
    fun setScaleType(scaleType: ScaleType) {
        this.scaleType = scaleType
        refreshAdapter()
    }

    fun setItemMarginHorizontal(itemMarginHorizontal: Float) {
        this.itemMarginHorizontal = itemMarginHorizontal
        refreshAdapter()
    }

    fun setItemCircularRadius(itemCircularRadius: Float) {
        this.itemCircularRadius = itemCircularRadius
        refreshAdapter()
    }

    fun setOnSnapPositionChangeListener(onSnapPositionChangeListener: (Int) -> Unit) {
        this.onSnapPositionChangeListener = onSnapPositionChangeListener
    }

    fun setOnClickListener(onClickedListener: ((String) -> Unit)?) {
        this.onClickedListener = onClickedListener
        refreshAdapter()
    }

    fun setImages(images: List<String>) {
        setupRecyclerView(images)
        setupScrollListener()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun refreshAdapter() {
        imageSliderAdapter?.let {
            it.scaleType = this.scaleType
            it.itemMarginHorizontal = this.itemMarginHorizontal
            it.itemCircularRadius = this.itemCircularRadius
            it.onClickedListener = this.onClickedListener
            it.notifyDataSetChanged()
        }
    }

    private fun setupRecyclerView(images: List<String>) {
        imageSliderAdapter = ImageSliderAdapter(
            images,
            scaleType,
            itemMarginHorizontal,
            itemCircularRadius,
            onClickedListener
        )
        recyclerView.layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        recyclerView.adapter = imageSliderAdapter
        snapHelper.attachToRecyclerView(recyclerView)
    }

    enum class Behavior {
        NOTIFY_ON_SCROLL,
        NOTIFY_ON_SCROLL_STATE_IDLE
    }

    private fun setupScrollListener() {
        val behavior = Behavior.NOTIFY_ON_SCROLL
        recyclerView.clearOnScrollListeners()
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            private var snapPosition = RecyclerView.NO_POSITION

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (behavior == Behavior.NOTIFY_ON_SCROLL) {
                    maybeNotifySnapPositionChange(recyclerView)
                }
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                handleScrollStateChange(newState)
                onScrollStateChangedListener?.invoke(newState)
                if (behavior == Behavior.NOTIFY_ON_SCROLL_STATE_IDLE
                    && newState == RecyclerView.SCROLL_STATE_IDLE
                ) {
                    maybeNotifySnapPositionChange(recyclerView)
                }
            }

            private fun maybeNotifySnapPositionChange(recyclerView: RecyclerView) {
                val snapView = snapHelper.findSnapView(recyclerView.layoutManager)
                val snapPosition =
                    snapView?.let { recyclerView.layoutManager?.getPosition(it) } ?: return
                val snapPositionChanged = this.snapPosition != snapPosition
                if (snapPositionChanged) {
                    val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                    val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

                    // Calculate real position based on the visible item and dataset size
                    val adapterPosition =
                        (firstVisibleItemPosition + (snapPosition - firstVisibleItemPosition)) % (imageSliderAdapter?.getImageCount()
                            ?: 0)
                    handleScrollPositionChange(adapterPosition)
                    this.snapPosition = snapPosition
                }
            }


        })

    }

    private fun handleScrollPositionChange(position: Int) {
        onSnapPositionChangeListener?.invoke(position)
    }

    private fun handleScrollStateChange(newState: Int) {
        when (newState) {
            RecyclerView.SCROLL_STATE_IDLE -> startAutoScroll()
            RecyclerView.SCROLL_STATE_DRAGGING -> stopAutoScroll()
        }
    }

    fun setAutoScroll(delayMillis: Long) {
        this.delayMillis = delayMillis
        toggleAutoScroll()
    }

    private fun toggleAutoScroll() {
        if (isAutoScrollEnabled) {
            stopAutoScroll()
        }
        startAutoScroll()
    }

    private fun startAutoScroll() {
        if (delayMillis == 0L || isAutoScrollEnabled) return
        isAutoScrollEnabled = true
        autoScrollRunnable = createAutoScrollRunnable()
        autoScrollRunnable?.let { handler.postDelayed(it, delayMillis) }
    }

    private fun createAutoScrollRunnable(): Runnable {
        return object : Runnable {
            override fun run() {
                scrollToNextPosition()
                handler.postDelayed(this, delayMillis)
            }
        }
    }

    private fun scrollToNextPosition() {
        val layoutManager = recyclerView.layoutManager as LinearLayoutManager
        val currentPosition = layoutManager.findFirstVisibleItemPosition()
        val nextPosition = (currentPosition + 1) % (recyclerView.adapter?.itemCount ?: 1)
        recyclerView.smoothScrollToPosition(nextPosition)
    }

    fun stopAutoScroll() {
        isAutoScrollEnabled = false
        autoScrollRunnable?.let { handler.removeCallbacks(it) }
    }
}
