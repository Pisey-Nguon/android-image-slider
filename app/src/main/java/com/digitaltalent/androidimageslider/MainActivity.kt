package com.digitaltalent.androidimageslider

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.digitaltalent.image_slider.ImageSliderView
import com.digitaltalent.image_slider.OnSnapPositionChangeListener

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

        findViewById<ImageSliderView>(R.id.imageSlider).setImages(
            listOf(
                "https://picsum.photos/id/1/200/300",
                "https://picsum.photos/id/2/200/300",
                "https://picsum.photos/id/3/200/300"
            ), onSnapPositionChangeListener = object :
                OnSnapPositionChangeListener {
                override fun onSnapPositionChange(position: Int) {

                }

            })
    }
}