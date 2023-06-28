package com.example.musttodolist.receiver

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat.getSystemService
import com.example.musttodolist.R
import java.util.Calendar

class NotificationReceiver : BroadcastReceiver() {
    private val TAG = javaClass.simpleName
    private lateinit var manager: NotificationManager
    private lateinit var builder: NotificationCompat.Builder

    // 오레오 이상은 반드시 채널을 설정해줘야 Notification이 작동함
    private val CHANNEL_ID = "channel1"
    private val CHANNEL_NAME = "Channel1"
    override fun onReceive(context: Context?, intent: Intent?) {
        Log.e(TAG, "onReceive 알람이 들어옴!!")
        var contentValue = intent?.getStringExtra("content") ?: ""
        val requestCode = intent?.getIntExtra("requestCode", 0)
        val id = intent?.getIntExtra("id", 0)
        val day =  intent?.getIntExtra("day", 0)
        val year =  intent?.getIntExtra("year", 0)
        val month =  intent?.getIntExtra("month", 0)
        val hour =  intent?.getIntExtra("hour", 0)
        val minute =  intent?.getIntExtra("minute", 0)


        Log.e(TAG, "onReceive 리시버 확인 : $contentValue")
        manager = context!!.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        builder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            manager.createNotificationChannel(
                NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT)
            )
            NotificationCompat.Builder(context!!, CHANNEL_ID)
        } else {
            NotificationCompat.Builder(context!!)
        }

        builder.setContentTitle("할 일")
        builder.setContentText(contentValue)
        builder.setSmallIcon(R.drawable.fire_icon)
        builder.setAutoCancel(true)

        val notification = builder.build()
        manager.notify(id!!, notification)



    }

}