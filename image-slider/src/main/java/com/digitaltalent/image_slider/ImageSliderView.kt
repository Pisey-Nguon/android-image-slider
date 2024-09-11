package com.digitaltalent.image_slider

import android.content.Context
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

    init {
        val view = LayoutInflater.from(context).inflate(R.layout.view_image_slider, this, false)
        recyclerView = view.findViewById(R.id.recyclerView)
        addView(view)
    }

    fun setImages(
        images: List<String>,
        onSnapPositionChangeListener: OnSnapPositionChangeListener
    ) {
        val snapHelper = PagerSnapHelper()
        val adapter = ImageSliderAdapter(images)
        val layoutManager = LinearLayoutManager(context,RecyclerView.HORIZONTAL,false)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter
        recyclerView.let { snapHelper.attachToRecyclerView(it) }
        recyclerView.addOnScrollListener(SnapOnScrollListener(snapHelper = snapHelper, onSnapPositionChangeListener = onSnapPositionChangeListener))
    }
}