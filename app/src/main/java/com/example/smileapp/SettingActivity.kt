package com.example.smileapp

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*

class SettingActivity : AppCompatActivity() {

    ////
    // Настроики приложения:
    ////
    var photoSetting : Boolean? = false      // показывать ли фотографии
    var textSetting : Boolean? = false       // показывать ли текст
    var randomSetting : Boolean? = false     // листать ли картинки рендомом

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

        val intent = getIntent()
        val extra1 = intent.getBooleanExtra(getString(R.string.photo_setting_answer), false)
        val extra2 = intent.getBooleanExtra(getString(R.string.text_setting_answer), false)
        val extra3 = intent.getBooleanExtra(getString(R.string.random_setting_answer), false)
        //Toast.makeText(applicationContext, "Values: " + extra1.toString() + "/" + extra2.toString(), Toast.LENGTH_LONG).show()

        val photoCheckBox = findViewById<CheckBox>(R.id.photoCheckBox)
        val textCheckBox = findViewById<CheckBox>(R.id.textCheckBox)

        if (extra1) {
            photoCheckBox.isChecked = true
            photoSetting = true
        }
        if (extra2) {
            textCheckBox.isChecked = true
            textSetting = true
        }

        //val radioGroup = findViewById<RadioGroup>(R.id.radioGroup)
        val inOrderRadioButton = findViewById<RadioButton>(R.id.inOrderRadioButton)
        val inRandomRadioButton = findViewById<RadioButton>(R.id.inRandomRadioButton)

        if (extra3) {
            inRandomRadioButton.isChecked = true
            //radioGroup.check(R.id.inRandomRadioButton)
            randomSetting = true
        }
        else {
            inOrderRadioButton.isChecked = true
            //radioGroup.check(R.id.inOrderRadioButton)
            randomSetting = false
        }

        // Устанавливаем стрелку Back в Action Bar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        val saveButton = findViewById<Button>(R.id.saveButton)
        saveButton.setOnClickListener{

            var isRandom = inRandomRadioButton.isChecked
            //if (inRandomRadioButton.isChecked) isRandom = true
            // = radioGroup.checkedRadioButtonId
            //val isRandom = detectRandomSetting()

            photoSetting = photoCheckBox.isChecked
            textSetting = textCheckBox.isChecked
            randomSetting = isRandom

            // Сохранение настроек
            val sharedPreferences = this.getSharedPreferences("smileAppSharedPreferences", Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.putBoolean("photoSetting", photoCheckBox.isChecked)
            editor.putBoolean("textSetting", textCheckBox.isChecked)
            editor.putBoolean("randomSetting", isRandom)
            editor.apply()

            val answerIntent = Intent()
            answerIntent.putExtra(getString(R.string.photo_setting_answer), photoSetting)
            answerIntent.putExtra(getString(R.string.text_setting_answer), textSetting)
            answerIntent.putExtra(getString(R.string.random_setting_answer), randomSetting)
            setResult(RESULT_OK, answerIntent)
            finish()
        }

    }
/*
    fun detectRandomSetting() : Boolean {
        val radioGroup = findViewById<RadioGroup>(R.id.radioGroup)
        val checkedRBT = radioGroup.checkedRadioButtonId
        if (checkedRBT == 0)
            return false
        else
            return true
    }*/
}