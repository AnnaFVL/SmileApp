package com.example.smileapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class AboutActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)

        val intent = intent
        val extra1 = intent.getIntExtra("funnySize", 0)
        val extra2 = intent.getIntExtra("photoSize", 0)
        val extra3 = intent.getIntExtra("textSize", 0)

        val imagesFunnyValueTextView = findViewById<TextView>(R.id.imagesFunnyValueTextView)
        val imagesPhotoValueTextView = findViewById<TextView>(R.id.imagesPhotoValueTextView)
        val imagesTextValueTextView = findViewById<TextView>(R.id.imagesTextValueTextView)

        imagesFunnyValueTextView.text = extra1.toString()
        imagesPhotoValueTextView.text = extra2.toString()
        imagesTextValueTextView.text = extra3.toString()

        // Устанавливаем стрелку Back в Action Bar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

    }
}