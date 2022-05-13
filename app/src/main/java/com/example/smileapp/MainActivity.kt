package com.example.smileapp

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    val requestID = 0           // для вызова SettingActivity, не имеет практического значения
    val requestAboutID = 1      // для вызова AboutActivity, не имеет практического значения

    ////
    // Настроики приложения:
    ////
    var photoSetting : Boolean? = true          // показывать ли фотографии
    var textSetting : Boolean? = true           // показывать ли текст
    var randomSetting : Boolean? = false        // листать ли картинки рендомом
    ////
    // Списки картинок:
    ////
    lateinit var funnyList: List<Int>           // список идентификаторов всех смешных картинок (R.id.<...>)
    lateinit var photoList: List<Int>           // список идентификаторов всех фотографий (R.id.<...>)
    lateinit var textList: List<Int>            // список идентификаторов всех текстов (R.id.<...>)
    lateinit var selectedImageList: List<Int>   // текущий список всех картинок, соответсвующий настройкам
    var currentImageID = 0                      // ID текущей картинки
    ////
    // Элементы UI:
    ////
    lateinit var randomButton : Button
    lateinit var nextButton : Button
    lateinit var prevButton : Button
    lateinit var smileImageView : ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Считываем сохраненные настройки
        val sharedPreferences = this.getSharedPreferences("smileAppSharedPreferences", Context.MODE_PRIVATE)
        photoSetting = sharedPreferences.getBoolean("photoSetting", true)
        textSetting = sharedPreferences.getBoolean("textSetting", true)
        randomSetting = sharedPreferences.getBoolean("randomSetting", false)

        // Создаем списки в соответсвии с настройками
        initialListMap()
        updateSelectedImageList()

        // Инициализируем элементы UI
        randomButton = findViewById(R.id.randomButton)
        nextButton = findViewById(R.id.nextButton)
        prevButton = findViewById(R.id.prevButton)
        smileImageView = findViewById(R.id.smileImageView)

        // Устанавливаем Listener's для кнопок
        randomButton.setOnClickListener{ showRandomImage() }
        nextButton.setOnClickListener{ showNextImage() }
        prevButton.setOnClickListener{ showPrevImage() }

        // Устанавливаем, какие кнопки отображать
        updateButtonsVisibility()
    }

    ////
    // Создание списков:
    ////
    private fun updateSelectedImageList() {
        selectedImageList = funnyList
        if (photoSetting == true) selectedImageList += photoList
        if (textSetting == true) selectedImageList += textList
    }

    private fun initialListMap() {
        funnyList = listOf(R.drawable.funny1, R.drawable.funny2, R.drawable.funny3, R.drawable.funny4, R.drawable.funny5,
            R.drawable.funny6, R.drawable.funny7, R.drawable.funny8, R.drawable.funny9, R.drawable.funny10,
            R.drawable.funny11, R.drawable.funny12, R.drawable.funny13, R.drawable.funny14, R.drawable.funny15)
        photoList = listOf(R.drawable.family1, R.drawable.family2, R.drawable.family3, R.drawable.family4,
            R.drawable.family5, R.drawable.family6, R.drawable.family7, R.drawable.family8, R.drawable.family9,
            R.drawable.family10, R.drawable.family11, R.drawable.family12, R.drawable.family13, R.drawable.family14,
            R.drawable.family15, R.drawable.family16, R.drawable.family17, R.drawable.family18, R.drawable.family19,
            R.drawable.family20, R.drawable.family21, R.drawable.family22, R.drawable.family23, R.drawable.family24,
            R.drawable.family25, R.drawable.family26, R.drawable.family27, R.drawable.family28, R.drawable.family29,
            R.drawable.family30, R.drawable.family31, R.drawable.family32, R.drawable.family33, R.drawable.family34,
            R.drawable.family35, R.drawable.family36, R.drawable.family37, R.drawable.family38, R.drawable.family39,
            R.drawable.family40, R.drawable.family41, R.drawable.family42, R.drawable.family43, R.drawable.family44,
            R.drawable.family45, R.drawable.family46, R.drawable.family47, R.drawable.family48, R.drawable.family49,
            R.drawable.family50, R.drawable.family51, R.drawable.family52)
        textList = listOf(R.drawable.text1, R.drawable.text2, R.drawable.text3, R.drawable.text4, R.drawable.text5,
            R.drawable.text6, R.drawable.text7, R.drawable.text8, R.drawable.text9, R.drawable.text10,
            R.drawable.text11, R.drawable.text12, R.drawable.text13, R.drawable.text14, R.drawable.text15,
            R.drawable.text16, R.drawable.text17, R.drawable.text18, R.drawable.text19, R.drawable.text20,
            R.drawable.text21, R.drawable.text22, R.drawable.text23, R.drawable.text24, R.drawable.text25,
            R.drawable.text26, R.drawable.text27, R.drawable.text28, R.drawable.text29, R.drawable.text30,
            R.drawable.text31, R.drawable.text32)

    }

    ////
    // Если вернулись (с ответом) из SettingActivity
    ////
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == requestID && resultCode == Activity.RESULT_OK) {
            photoSetting = data?.getBooleanExtra(getString(R.string.photo_setting_answer), true)
            textSetting = data?.getBooleanExtra(getString(R.string.text_setting_answer), true)
            randomSetting = data?.getBooleanExtra(getString(R.string.random_setting_answer), false)

            // Обновляем список изображений:
            updateSelectedImageList()
            if (currentImageID >= selectedImageList.size) currentImageID = 0
            //val toast = Toast.makeText(applicationContext, "Values: " + photoSetting.toString() + "/" + textSetting.toString(), Toast.LENGTH_LONG)
            //toast.show()

            // Устанавливаем, какие кнопки отображать
            updateButtonsVisibility()
        }
        else {
            //val toast = Toast.makeText(applicationContext, "Values: " + photoSetting.toString() + "/" + textSetting.toString(), Toast.LENGTH_LONG)
            //toast.show()
        }
    }

    ////
    // Устанавливаем доступность кнопок
    ////
    private fun updateButtonsVisibility() {
         if (randomSetting == null || randomSetting == false) {
            randomButton.isVisible = false
            nextButton.isVisible = true
            prevButton.isVisible = true
        } else {
            randomButton.isVisible = true
            nextButton.isVisible = false
            prevButton.isVisible = false
        }
    }

    ////
    // Работа с main_menu
    ////
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_settings -> {
                //Toast.makeText(applicationContext, "Settings!", Toast.LENGTH_LONG).show()
                val intent = Intent(this@MainActivity, SettingActivity::class.java)
                intent.putExtra(getString(R.string.photo_setting_answer), photoSetting)
                intent.putExtra(getString(R.string.text_setting_answer), textSetting)
                intent.putExtra(getString(R.string.random_setting_answer), randomSetting)
                startActivityForResult(intent, requestID)
            }
            R.id.action_about -> {
                val intentAbout = Intent(this@MainActivity, AboutActivity::class.java)
                intentAbout.putExtra("funnySize", funnyList.size)
                intentAbout.putExtra("photoSize", photoList.size)
                intentAbout.putExtra("textSize", textList.size)
                startActivity(intentAbout)
            }
            else -> Toast.makeText(applicationContext, "Unknown menu item is selected", Toast.LENGTH_LONG).show()
        }
        return super.onOptionsItemSelected(item)
    }

    ////
    // Выбор картинок
    ////
    private fun showRandomImage() {
        val listSize = selectedImageList.size
        val randomChoice = java.util.Random().nextInt(listSize)

        currentImageID = randomChoice
        val imageSource = selectedImageList[currentImageID]
        smileImageView.setImageResource(imageSource)
    }

    private  fun showNextImage() {
        currentImageID++

        if (currentImageID >= selectedImageList.size) {
            currentImageID = 0
        }
        val imageSource = selectedImageList[currentImageID]
        smileImageView.setImageResource(imageSource)
    }

    private fun showPrevImage() {
        currentImageID--

        if (currentImageID < 0) {
            currentImageID = selectedImageList.size - 1
        }
        val imageSource = selectedImageList[currentImageID]
        smileImageView.setImageResource(imageSource)
    }
}