package com.digitaltalent.image_slider

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper
import com.bumptech.glide.Glide

class ImageSliderAdapter(
    private val images: List<String>,
    private val itemMarginHorizontal: Float,
    private val itemCircularRadius: Float
) : RecyclerView.Adapter<ImageSliderAdapter.ImageViewHolder>() {

    inner class ImageViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        fun onBind(image: String, itemMarginHorizontal: Float, itemCircularRadius: Float) {
            val layoutCard = view.findViewById<CardView>(R.id.layoutCard)
            val imageView = view.findViewById<ImageView>(R.id.imageView)
            layoutCard.radius = itemCircularRadius
            val layoutParams = ViewGroup.MarginLayoutParams(
                ViewGroup.MarginLayoutParams.MATCH_PARENT,
                ViewGroup.MarginLayoutParams.WRAP_CONTENT
            )
            layoutParams.setMargins(itemMarginHorizontal.toInt(), 0, itemMarginHorizontal.toInt(), 0) // left, top, right, bottom
            layoutCard.layoutParams = layoutParams

            Glide.with(view).load(image).into(imageView)


        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_image, parent, false)
        return ImageViewHolder(view)
    }

    override fun getItemCount(): Int {
        return Int.MAX_VALUE
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        holder.onBind(
            images[holder.adapterPosition % images.size],
            itemMarginHorizontal,
            itemCircularRadius
        )
    }


}

interface OnSnapPositionChangeListener {

    fun onSnapPositionChange(position: Int)
}
interface OnSnapStateChangedListener{
    fun onSnapStateChange(newState:Int)
}

class SnapOnScrollListener(
    private val snapHelper: SnapHelper,
    private var behavior: Behavior = Behavior.NOTIFY_ON_SCROLL_STATE_IDLE,
    private val count:Int,
    var onSnapPositionChangeListener: OnSnapPositionChangeListener? = null,
    var onScrollStateChangedListener: OnSnapStateChangedListener? = null,
) : RecyclerView.OnScrollListener() {

    enum class Behavior {
        NOTIFY_ON_SCROLL,
        NOTIFY_ON_SCROLL_STATE_IDLE
    }

    private var snapPosition = RecyclerView.NO_POSITION

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        if (behavior == Behavior.NOTIFY_ON_SCROLL) {
            maybeNotifySnapPositionChange(recyclerView)
        }
    }

    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
        onScrollStateChangedListener?.onSnapStateChange(newState)
        if (behavior == Behavior.NOTIFY_ON_SCROLL_STATE_IDLE
            && newState == RecyclerView.SCROLL_STATE_IDLE
        ) {
            maybeNotifySnapPositionChange(recyclerView)
        }
    }

    private fun maybeNotifySnapPositionChange(recyclerView: RecyclerView) {
        val snapView = snapHelper.findSnapView(recyclerView.layoutManager)
        val snapPosition = snapView?.let { recyclerView.layoutManager?.getPosition(it) } ?: return
        val snapPositionChanged = this.snapPosition != snapPosition
        if (snapPositionChanged) {
            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

            // Calculate real position based on the visible item and dataset size
            val adapterPosition = (firstVisibleItemPosition + (snapPosition - firstVisibleItemPosition)) % count
            onSnapPositionChangeListener?.onSnapPositionChange(adapterPosition)
            this.snapPosition = snapPosition
        }
    }


}

