package com.example.smileapp

import android.app.*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.view.isVisible
import java.util.*


class MainActivity : AppCompatActivity() {

    ////
    // Настроики приложения:
    ////
    var photoSetting : Boolean? = true          // показывать ли фотографии
    var textSetting : Boolean? = true           // показывать ли мотивацию
    var textFatherSetting : Boolean? = true     // показывать ли текст для папы
    var textMotherSetting : Boolean? = true     // показывать ли текст для мамы
    var textIraSetting : Boolean? = true        // показывать ли текст для Иры
    var textIlyaSetting : Boolean? = true       // показывать ли текст для Ильи
    var textAnnaSetting : Boolean? = true       // показывать ли текст для Ани
    var randomSetting : Boolean? = false        // листать ли картинки рендомом
    var notifySetting : Boolean? = false        // показывать ли нотификации
    ////
    // Списки картинок:
    ////
    lateinit var funnyList: List<Int>           // список идентификаторов всех смешных картинок (R.id.<...>)
    lateinit var photoList: List<Int>           // список идентификаторов всех фотографий (R.id.<...>)
    lateinit var textList: List<Int>            // список идентификаторов мотивации (R.id.<...>)
    lateinit var textFatherList: List<Int>      // список идентификаторов текстов для папы (R.id.<...>)
    lateinit var textMotherList: List<Int>      // список идентификаторов текстов для мамы (R.id.<...>)
    lateinit var textIraList: List<Int>         // список идентификаторов текстов для Иры (R.id.<...>)
    lateinit var textIlyaList: List<Int>        // список идентификаторов текстов для Ильи (R.id.<...>)
    lateinit var textAnnaList: List<Int>        // список идентификаторов текстов для Ани (R.id.<...>)
    lateinit var selectedImageList: List<Int>   // текущий список всех картинок, соответсвующий настройкам
    var currentImageID = 0                      // ID текущей картинки
    var selectedID = 0                          // ID картинки для нотификации
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
        val sharedPreferences = this.getSharedPreferences(getString(R.string.file_name), Context.MODE_PRIVATE)
        photoSetting = sharedPreferences.getBoolean(getString(R.string.photo), true)
        textSetting = sharedPreferences.getBoolean(getString(R.string.text), true)
        textFatherSetting = sharedPreferences.getBoolean(getString(R.string.father), true)
        textMotherSetting = sharedPreferences.getBoolean(getString(R.string.mother), true)
        textIraSetting = sharedPreferences.getBoolean(getString(R.string.ira), true)
        textIlyaSetting = sharedPreferences.getBoolean(getString(R.string.ilya), true)
        textAnnaSetting = sharedPreferences.getBoolean(getString(R.string.anna), true)
        randomSetting = sharedPreferences.getBoolean(getString(R.string.random), false)
        notifySetting = sharedPreferences.getBoolean(getString(R.string.notification), false)

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

        // Если запустились из Notification
        val startFromNotificationIntent = intent
        var extra1 = 0
        extra1 = startFromNotificationIntent.getIntExtra("selectedID", 0)
        var imageSource = 0

        if (extra1 != 0) {
            //val myToast = Toast.makeText(applicationContext, "extra1 = $extra1", Toast.LENGTH_LONG)
            //myToast.show()
            if (extra1 >= selectedImageList.size) extra1 = 0
        }
        imageSource = selectedImageList[extra1]
        smileImageView.setImageResource(imageSource)

        // Запускаем нотификацию на 19:00
        if (notifySetting == true) {
            setAlarmReceiver()
        } else {
            cancelAlarmReceiver()
        }


    }

    ////
    // Нотификации:
    ////

    private fun setAlarmReceiver() {

        val calendar: Calendar = Calendar.getInstance()
        calendar.timeInMillis = System.currentTimeMillis()
        calendar.set(Calendar.HOUR_OF_DAY, 19)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 1)

        //val intervalInMs = 3000
        val context = this
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val intent = Intent(context, Receiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        Log.d("MainActivity", "Create: " + Date().toString())
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, AlarmManager.INTERVAL_DAY, pendingIntent)
        //alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)

    }

    private fun cancelAlarmReceiver() {
        val context = this
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val intent = Intent(context, Receiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        alarmManager.cancel(pendingIntent)
    }



    ////
    // Создание списков:
    ////
    private fun updateSelectedImageList() {
        selectedImageList = funnyList
        if (photoSetting == true) selectedImageList += photoList
        if (textSetting == true) selectedImageList += textList
        if (textFatherSetting == true) selectedImageList += textFatherList
        if (textMotherSetting == true) selectedImageList += textMotherList
        if (textIraSetting == true) selectedImageList += textIraList
        if (textIlyaSetting == true) selectedImageList += textIlyaList
        if (textAnnaSetting == true) selectedImageList += textAnnaList
    }

    private fun initialListMap() {
        funnyList = listOf(R.drawable.funny1, R.drawable.funny2, R.drawable.funny3, R.drawable.funny4, R.drawable.funny5,
            R.drawable.funny6, R.drawable.funny7, R.drawable.funny8, R.drawable.funny9, R.drawable.funny10,
            R.drawable.funny11, R.drawable.funny12, R.drawable.funny13, R.drawable.funny14, R.drawable.funny15,
            R.drawable.funny16, R.drawable.funny17)
        // 5, 13, 20, 26 - убрала
        photoList = listOf(R.drawable.family1, R.drawable.family2, R.drawable.family3, R.drawable.family4,
            R.drawable.family6, R.drawable.family7, R.drawable.family8, R.drawable.family9,
            R.drawable.family10, R.drawable.family11, R.drawable.family12, R.drawable.family14,
            R.drawable.family15, R.drawable.family16, R.drawable.family17, R.drawable.family18, R.drawable.family19,
            R.drawable.family21, R.drawable.family22, R.drawable.family23, R.drawable.family24,
            R.drawable.family25, R.drawable.family27, R.drawable.family28, R.drawable.family29,
            R.drawable.family30, R.drawable.family31, R.drawable.family32, R.drawable.family33, R.drawable.family34,
            R.drawable.family35, R.drawable.family36, R.drawable.family37, R.drawable.family38, R.drawable.family39,
            R.drawable.family40, R.drawable.family41, R.drawable.family42, R.drawable.family43, R.drawable.family44,
            R.drawable.family45, R.drawable.family46, R.drawable.family47, R.drawable.family48, R.drawable.family49,
            R.drawable.family50, R.drawable.family51, R.drawable.family52, R.drawable.family53, R.drawable.family54,
            R.drawable.family55, R.drawable.family56, R.drawable.family57, R.drawable.family58)
        textList = listOf(R.drawable.text1, R.drawable.text2, R.drawable.text3, R.drawable.text4, R.drawable.text5,
            R.drawable.text6, R.drawable.text26)
        textFatherList = listOf(R.drawable.text8, R.drawable.text11, R.drawable.text14, R.drawable.text19,
            R.drawable.text29, R.drawable.text31)
        textMotherList = listOf(R.drawable.text7, R.drawable.text11, R.drawable.text13, R.drawable.text21,
            R.drawable.text22, R.drawable.text29)
        textIraList = listOf(R.drawable.text9, R.drawable.text12, R.drawable.text25, R.drawable.text28,
            R.drawable.text30)
        textIlyaList = listOf(R.drawable.text10, R.drawable.text12, R.drawable.text20, R.drawable.text23)
        textAnnaList = listOf(R.drawable.text15, R.drawable.text16, R.drawable.text17, R.drawable.text18,
            R.drawable.text24, R.drawable.text27, R.drawable.text32)

    }

    ////
    // Если вернулись (с ответом) из SettingActivity
    ////
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == R.integer.requestID && resultCode == Activity.RESULT_OK) {
            photoSetting = data?.getBooleanExtra(getString(R.string.photo), true)
            textSetting = data?.getBooleanExtra(getString(R.string.text), true)
            textFatherSetting = data?.getBooleanExtra(getString(R.string.father), true)
            textMotherSetting = data?.getBooleanExtra(getString(R.string.mother), true)
            textIraSetting = data?.getBooleanExtra(getString(R.string.ira), true)
            textIlyaSetting = data?.getBooleanExtra(getString(R.string.ilya), true)
            textAnnaSetting = data?.getBooleanExtra(getString(R.string.anna), true)
            randomSetting = data?.getBooleanExtra(getString(R.string.random), false)
            notifySetting = data?.getBooleanExtra(getString(R.string.notification), false)

            // Обновляем список изображений:
            updateSelectedImageList()
            if (currentImageID >= selectedImageList.size) currentImageID = 0
            //val toast = Toast.makeText(applicationContext, "Values: " + photoSetting.toString() + "/" + textSetting.toString(), Toast.LENGTH_LONG)
            //toast.show()

            // Устанавливаем, какие кнопки отображать
            updateButtonsVisibility()

            if (notifySetting == true) {
                setAlarmReceiver()
            }

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
                intent.putExtra(getString(R.string.photo), photoSetting)
                intent.putExtra(getString(R.string.text), textSetting)
                intent.putExtra(getString(R.string.father), textFatherSetting)
                intent.putExtra(getString(R.string.mother), textMotherSetting)
                intent.putExtra(getString(R.string.ira), textIraSetting)
                intent.putExtra(getString(R.string.ilya), textIlyaSetting)
                intent.putExtra(getString(R.string.anna), textAnnaSetting)
                intent.putExtra(getString(R.string.random), randomSetting)
                intent.putExtra(getString(R.string.notification), notifySetting)
                startActivityForResult(intent, R.integer.requestID)
            }
            R.id.action_about -> {
                val intentAbout = Intent(this@MainActivity, AboutActivity::class.java)
                intentAbout.putExtra("funnySize", funnyList.size)
                intentAbout.putExtra("photoSize", photoList.size)
                intentAbout.putExtra("textSize", textList.size)
                intentAbout.putExtra("textFatherSize", textFatherList.size)
                intentAbout.putExtra("textMotherSize", textMotherList.size)
                intentAbout.putExtra("textIraSize", textIraList.size)
                intentAbout.putExtra("textIlyaSize", textIlyaList.size)
                intentAbout.putExtra("textAnnaSize", textAnnaList.size)
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

    ////
    // Receiver для нотификаций
    ////
    class Receiver : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            Log.d("MainActivity", "Received: " + Date().toString())

            registerNotificationChannel(context)
            showNotification(context)

        }

        private fun registerNotificationChannel(context: Context?) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val name = R.string.channel_name.toString()
                val descriptionText = R.string.channel_description.toString()
                val importance = NotificationManager.IMPORTANCE_DEFAULT
                val channel = NotificationChannel(R.string.channel_ID.toString(), name, importance).apply {
                    description = descriptionText
                }
                // Register the channel with the system
                val notificationManager: NotificationManager =
                    context!!.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                notificationManager.createNotificationChannel(channel)
            }
        }

        private fun showNotification(context: Context?) {
            lateinit var funnyList: List<Int>

            funnyList = listOf(R.drawable.funny1, R.drawable.funny2, R.drawable.funny3, R.drawable.funny4, R.drawable.funny5,
                R.drawable.funny6, R.drawable.funny7, R.drawable.funny8, R.drawable.funny9, R.drawable.funny10,
                R.drawable.funny11, R.drawable.funny12, R.drawable.funny13, R.drawable.funny14, R.drawable.funny15,
                R.drawable.funny16, R.drawable.funny17)

            val intent = Intent(context, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
             val selectedID = java.util.Random().nextInt(funnyList.size)
            intent.putExtra("selectedID", selectedID)
            //val myToast = Toast.makeText(applicationContext, "selectedID = $selectedID", Toast.LENGTH_LONG)
            //myToast.show()
            val pendingIntent: PendingIntent = PendingIntent.getActivity(context!!, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
            var notificationImageID = funnyList[selectedID]
            //var notificationImageID = R.drawable.funny1
            var resources = context.resources

            val builder = NotificationCompat.Builder(context!!, R.string.channel_ID.toString())
            builder.setSmallIcon(R.drawable.ic_baseline_leaf_24)
            //builder.setContentTitle("My Title")
            //builder.setContentText("This is the Body")
            builder.setAutoCancel(true)
            builder.setPriority(NotificationCompat.PRIORITY_DEFAULT)
            builder.setDefaults(Notification.DEFAULT_ALL)
            builder.setStyle(NotificationCompat.BigPictureStyle().bigPicture(BitmapFactory.decodeResource(resources,notificationImageID )))
            builder.setContentIntent(pendingIntent)

            NotificationManagerCompat.from(context!!).notify(R.integer.notificationID, builder.build())
        }
    }

}