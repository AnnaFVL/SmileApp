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
        val extra31 = intent.getIntExtra("textFatherSize", 0)
        val extra32 = intent.getIntExtra("textMotherSize", 0)
        val extra33 = intent.getIntExtra("textIraSize", 0)
        val extra34 = intent.getIntExtra("textIlyaSize", 0)
        val extra35 = intent.getIntExtra("textAnnaSize", 0)

        val imagesFunnyValueTextView = findViewById<TextView>(R.id.imagesFunnyValueTextView)
        val imagesPhotoValueTextView = findViewById<TextView>(R.id.imagesPhotoValueTextView)
        val imagesTextValueTextView = findViewById<TextView>(R.id.imagesTextValueTextView)
        val fatherValueTextView = findViewById<TextView>(R.id.imagesTextValueFatherTextView)
        val motherValueTextView = findViewById<TextView>(R.id.imagesTextValueMotherTextView)
        val iraValueTextView = findViewById<TextView>(R.id.imagesTextValueIraTextView)
        val ilyaValueTextView = findViewById<TextView>(R.id.imagesTextValueIlyaTextView)
        val annaValueTextView = findViewById<TextView>(R.id.imagesTextValueAnnaTextView)

        imagesFunnyValueTextView.text = extra1.toString()
        imagesPhotoValueTextView.text = extra2.toString()
        imagesTextValueTextView.text = extra3.toString()
        fatherValueTextView.text = extra31.toString()
        motherValueTextView.text = extra32.toString()
        iraValueTextView.text = extra33.toString()
        ilyaValueTextView.text = extra34.toString()
        annaValueTextView.text = extra35.toString()

        // Устанавливаем стрелку Back в Action Bar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

    }
}