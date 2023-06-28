package com.example.musttodolist.api

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import com.example.musttodolist.receiver.NotificationReceiver
import java.util.Calendar

class SetAlarmAPI {
    companion object {
        lateinit var alarmManager: AlarmManager
        lateinit var pendingIntent: PendingIntent
    }


    // 알람 매니저에 알람 등록 처리
    fun setNotice(
        context: Context,
        alarmManager: AlarmManager,
        year: Int,
        month: Int,
        day: Int,
        hour : Int,
        minute : Int,
        id: Int,
        content: String,
        requestCode: Int
    ) {

        val receiverIntent = Intent(context, NotificationReceiver::class.java).apply {
            putExtra("content", content)
            putExtra("requestCode", requestCode)
            putExtra("id", id)
            putExtra("year", year)
            putExtra("month", month)
            putExtra("day", day)
            putExtra("hour", hour)
            putExtra("minute", minute)
        }

        val flags = PendingIntent.FLAG_UPDATE_CURRENT or if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
            PendingIntent.FLAG_MUTABLE
        else
            0

        pendingIntent = PendingIntent.getBroadcast(context, requestCode, receiverIntent, flags)

        val calendar = Calendar.getInstance().apply {
            set(Calendar.YEAR, year)
            set(Calendar.MONTH, month - 1)
            set(Calendar.DATE,day)
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
        }

        val alarmType = when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.M -> AlarmManager.RTC_WAKEUP
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT -> AlarmManager.RTC_WAKEUP
            else -> AlarmManager.RTC_WAKEUP
        }

        alarmManager.setExact(alarmType, calendar.timeInMillis, pendingIntent)
    }
    fun cancelAlarm(context: Context, alarmManager: AlarmManager, requestCode: Int) {
        val receiverIntent = Intent(context, NotificationReceiver::class.java)
        val pendingIntent: PendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            PendingIntent.getBroadcast(
                context,
                requestCode,
                receiverIntent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
            )
        } else {
            PendingIntent.getBroadcast(
                context,
                requestCode,
                receiverIntent,
                PendingIntent.FLAG_UPDATE_CURRENT
            )
        }
        alarmManager.cancel(pendingIntent)
        pendingIntent.cancel()
    }

}