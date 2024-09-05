package com.example.ayat.presentation.azan


import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaPlayer
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

import com.example.ayat.AyatApplication
import com.example.ayat.R

class CancelAzanReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        context?.let {
            it.stopService(Intent(it, AzanService::class.java))
            val notificationManager = NotificationManagerCompat.from(it)
            notificationManager.cancel(1)
        }
    }

}

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        context?.let {
            val prayerName = intent?.getStringExtra("PRAYER_NAME") ?: "الصلاة"
            val serviceIntent = Intent(it, AzanService::class.java).apply {
                putExtra("PRAYER_NAME", prayerName)
            }
            it.startForegroundService(serviceIntent)

        }
    }
}

class AzanService : Service() {

    private var mediaPlayer: MediaPlayer? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        createNotificationChannel()

        val prayerName = intent?.getStringExtra("PRAYER_NAME") ?: "الصلاة"
        notificationCreation(prayerName)
        startAzan()

        return START_NOT_STICKY
    }

    fun startAzan() {
        mediaPlayer = MediaPlayer.create(this, R.raw.azann)
        mediaPlayer?.setOnCompletionListener {
            stopSelf()
        }
        Log.d("newupdate", "onStartCommand: azan")
        mediaPlayer?.start()

    }

    fun notificationCreation(prayerName: String) {
        val cancelIntent = Intent(this, CancelAzanReceiver::class.java)
        val cancelPendingIntent = PendingIntent.getBroadcast(
            this,
            0,
            cancelIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        val myBitmap = BitmapFactory.decodeResource(
            AyatApplication.getApplicationContext().resources,
            R.drawable.azannotifcation
        )
        val notificationText = " حان الآن موعد صلاة $prayerName"
        val notification = NotificationCompat.Builder(this, "AzanChannel")
            .setSmallIcon(R.drawable.azannotifcation)
            .setContentTitle("حى على الصلاة")
            .setContentText(notificationText)
            .setLargeIcon(myBitmap)
            .setDeleteIntent(cancelPendingIntent)
            .addAction(R.drawable.check_ic, "تم", cancelPendingIntent)
            .setStyle(
                NotificationCompat.BigPictureStyle()
                    .bigPicture(myBitmap)
                    .bigLargeIcon(myBitmap as Bitmap?)
            )
            .build()


        startForeground(1, notification)
    }

    override fun onDestroy() {
        mediaPlayer?.release()
        mediaPlayer = null
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    private fun createNotificationChannel() {
        val serviceChannel = NotificationChannel(
            "AzanChannel",
            "Azan Service Channel",
            NotificationManager.IMPORTANCE_HIGH
        )
        serviceChannel.lockscreenVisibility = NotificationCompat.VISIBILITY_PUBLIC
        val manager = getSystemService(NotificationManager::class.java)
        manager.createNotificationChannel(serviceChannel)
    }
}

