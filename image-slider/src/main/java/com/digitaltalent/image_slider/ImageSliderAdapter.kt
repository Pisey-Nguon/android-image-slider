package com.digitaltalent.image_slider

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ImageView.ScaleType
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class ImageSliderAdapter(
    private val images: List<String>,
    var scaleType: ScaleType,
    var itemMarginHorizontal: Float,
    var itemCircularRadius: Float,
    var onClickedListener: ((String) -> Unit)?,
) : RecyclerView.Adapter<ImageSliderAdapter.ImageViewHolder>() {

    fun getImageCount():Int{
        return images.size
    }

    inner class ImageViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        fun onBind(image: String,scaleType: ScaleType, itemMarginHorizontal: Float, itemCircularRadius: Float,onClickedListener:((String)-> Unit)?) {
            val layoutCard = view.findViewById<CardView>(R.id.layoutCard)
            val imageView = view.findViewById<ImageView>(R.id.imageView)
            layoutCard.radius = itemCircularRadius
            val layoutParams = ViewGroup.MarginLayoutParams(
                ViewGroup.MarginLayoutParams.MATCH_PARENT,
                ViewGroup.MarginLayoutParams.MATCH_PARENT
            )
            layoutParams.setMargins(itemMarginHorizontal.toInt(), 0, itemMarginHorizontal.toInt(), 0) // left, top, right, bottom
            layoutCard.layoutParams = layoutParams
            imageView.scaleType = scaleType
            Glide.with(view.context).load(image).into(imageView)
            layoutCard.setOnClickListener { onClickedListener?.invoke(image) }


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
            scaleType,
            itemMarginHorizontal,
            itemCircularRadius,
            onClickedListener
        )
    }


}



