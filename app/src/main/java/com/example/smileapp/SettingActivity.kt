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
    var photoSetting : Boolean? = false         // показывать ли фотографии
    var textSetting : Boolean? = false          // показывать ли мотивацию
    var textFatherSetting : Boolean? = false    // показывать ли текст для папы
    var textMotherSetting : Boolean? = false    // показывать ли текст для мамы
    var textIraSetting : Boolean? = false       // показывать ли текст для Иры
    var textIlyaSetting : Boolean? = false      // показывать ли текст для Ильи
    var textAnnaSetting : Boolean? = false      // показывать ли текст для Ани
    var randomSetting : Boolean? = false        // листать ли картинки рендомом
    var notificationSetting : Boolean? = false  // показывать ли нотификации

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

        val intent = getIntent()
        val extra1 = intent.getBooleanExtra(getString(R.string.photo), false)
        val extra2 = intent.getBooleanExtra(getString(R.string.text), false)
        val extra21 = intent.getBooleanExtra(getString(R.string.father), false)
        val extra22 = intent.getBooleanExtra(getString(R.string.mother), false)
        val extra23 = intent.getBooleanExtra(getString(R.string.ira), false)
        val extra24 = intent.getBooleanExtra(getString(R.string.ilya), false)
        val extra25 = intent.getBooleanExtra(getString(R.string.anna), false)
        val extra3 = intent.getBooleanExtra(getString(R.string.random), false)
        val extra4 = intent.getBooleanExtra(getString(R.string.notification), false)
        //Toast.makeText(applicationContext, "Values: " + extra1.toString() + "/" + extra2.toString(), Toast.LENGTH_LONG).show()

        val photoCheckBox = findViewById<CheckBox>(R.id.photoCheckBox)
        val textCheckBox = findViewById<CheckBox>(R.id.textCheckBox)
        val fatherCheckBox = findViewById<CheckBox>(R.id.textFatherCheckBox)
        val motherCheckBox = findViewById<CheckBox>(R.id.textMotherCheckBox)
        val iraCheckBox = findViewById<CheckBox>(R.id.textIraCheckBox)
        val ilyaCheckBox = findViewById<CheckBox>(R.id.textIlyaCheckBox)
        val annaCheckBox = findViewById<CheckBox>(R.id.textAnnaCheckBox)
        val notificationSwitch = findViewById<Switch>(R.id.notifySwitch)

        if (extra1) {
            photoCheckBox.isChecked = true
            photoSetting = true
        }
        if (extra2) {
            textCheckBox.isChecked = true
            textSetting = true
        }
        if (extra21) {
            fatherCheckBox.isChecked = true
            textFatherSetting = true
        }
        if (extra22) {
            motherCheckBox.isChecked = true
            textMotherSetting = true
        }
        if (extra23) {
            iraCheckBox.isChecked = true
            textIraSetting = true
        }
        if (extra24) {
            ilyaCheckBox.isChecked = true
            textIlyaSetting = true
        }
        if (extra25) {
            annaCheckBox.isChecked = true
            textAnnaSetting = true
        }
        if (extra4) {
            notificationSwitch.isChecked = true
            notificationSetting = true
        }


        val inOrderRadioButton = findViewById<RadioButton>(R.id.inOrderRadioButton)
        val inRandomRadioButton = findViewById<RadioButton>(R.id.inRandomRadioButton)

        if (extra3) {
            inRandomRadioButton.isChecked = true
            randomSetting = true
        }
        else {
            inOrderRadioButton.isChecked = true
            randomSetting = false
        }

        // Устанавливаем стрелку Back в Action Bar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        val saveButton = findViewById<Button>(R.id.saveButton)
        saveButton.setOnClickListener{

            var isRandom = inRandomRadioButton.isChecked

            photoSetting = photoCheckBox.isChecked
            textSetting = textCheckBox.isChecked
            textFatherSetting = fatherCheckBox.isChecked
            textMotherSetting = motherCheckBox.isChecked
            textIraSetting = iraCheckBox.isChecked
            textIlyaSetting = ilyaCheckBox.isChecked
            textAnnaSetting = annaCheckBox.isChecked
            randomSetting = isRandom
            notificationSetting = notificationSwitch.isChecked

            // Сохранение настроек
            val sharedPreferences = this.getSharedPreferences("smileAppSharedPreferences", Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.putBoolean(getString(R.string.photo), photoCheckBox.isChecked)
            editor.putBoolean(getString(R.string.text), textCheckBox.isChecked)
            editor.putBoolean(getString(R.string.father), fatherCheckBox.isChecked)
            editor.putBoolean(getString(R.string.mother), motherCheckBox.isChecked)
            editor.putBoolean(getString(R.string.ira), iraCheckBox.isChecked)
            editor.putBoolean(getString(R.string.ilya), ilyaCheckBox.isChecked)
            editor.putBoolean(getString(R.string.anna), annaCheckBox.isChecked)
            editor.putBoolean(getString(R.string.random), isRandom)
            editor.putBoolean(getString(R.string.notification), notificationSwitch.isChecked)
            editor.apply()

            val answerIntent = Intent()
            answerIntent.putExtra(getString(R.string.photo), photoSetting)
            answerIntent.putExtra(getString(R.string.text), textSetting)
            answerIntent.putExtra(getString(R.string.father), textFatherSetting)
            answerIntent.putExtra(getString(R.string.mother), textMotherSetting)
            answerIntent.putExtra(getString(R.string.ira), textIraSetting)
            answerIntent.putExtra(getString(R.string.ilya), textIlyaSetting)
            answerIntent.putExtra(getString(R.string.anna), textAnnaSetting)
            answerIntent.putExtra(getString(R.string.random), randomSetting)
            answerIntent.putExtra(getString(R.string.notification), notificationSetting)
            setResult(RESULT_OK, answerIntent)
            finish()
        }

    }
}