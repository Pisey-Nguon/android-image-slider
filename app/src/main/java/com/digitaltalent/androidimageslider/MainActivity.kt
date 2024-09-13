package com.digitaltalent.androidimageslider

import android.os.Bundle
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.digitaltalent.image_slider.ImageSliderView
import com.digitaltalent.image_slider.OnSnapPositionChangeListener
import com.digitaltalent.page_indicator.PageIndicator

const val TAG = "main_activity"

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val pageIndicator = findViewById<PageIndicator>(R.id.pageIndicator)
        pageIndicator.length = 3
        val imageSlider = findViewById<ImageSliderView>(R.id.imageSlider)
        val defaultSpacing = resources.getDimension(R.dimen.default_spacing)
        imageSlider.setImages(
            listOf(
                "https://picsum.photos/id/1/200/300",
                "https://picsum.photos/id/12/200/300",
                "https://picsum.photos/id/23/200/300"
            ),
            itemMarginHorizontal = defaultSpacing
            , onSnapPositionChangeListener = object :
                OnSnapPositionChangeListener {
                override fun onSnapPositionChange(position: Int) {
                    pageIndicator.index = position
                }

            })
        imageSlider.setAutoScroll(3000)
    }
}