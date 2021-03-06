package com.example.smileapp

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat.getSystemService
import java.util.*

class DeviceBootReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent!!.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            val scheduleAR : MainActivity.ScheduleAlarmReceiver =
                MainActivity.ScheduleAlarmReceiver()
            scheduleAR.schedule(context)

            /*
            val sharedPreferences = context?.getSharedPreferences(context.getString(R.string.file_name), Context.MODE_PRIVATE)
            val notifySetting : Boolean = sharedPreferences?.getBoolean(context.getString(R.string.notification), false)!!
            val timeSetting : Int = sharedPreferences?.getInt(context.getString(R.string.time), 0)!!

            if (notifySetting) {

                val alarmIntent: Intent = Intent(context, MainActivity.Receiver::class.java)
                val pendingIntent: PendingIntent = PendingIntent.getBroadcast(
                    context,
                    0,
                    alarmIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT
                )
                val alarmManager = context!!.getSystemService(Context.ALARM_SERVICE) as AlarmManager

                val calendar: Calendar = Calendar.getInstance()
                //calendar.timeInMillis = System.currentTimeMillis()
                calendar.set(Calendar.HOUR_OF_DAY, timeSetting)
                calendar.set(Calendar.MINUTE, 0)
                calendar.set(Calendar.SECOND, 1)

                alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
                //alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, AlarmManager.INTERVAL_DAY, pendingIntent)
            }*/
        }
    }

}
