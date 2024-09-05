package com.example.ayat.presentation.azan

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import java.time.LocalDateTime

class AlarmSchedularrImp(
    private val context: Context
) : AlarmScheduler {
    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    @SuppressLint("ScheduleExactAlarm")
    override fun scheduleAlarm(alarmTime: List<Long>) {
        Log.d("AlarmSchedularrImp", "Scheduling alarms at: $alarmTime")
        val prayerNames = listOf("الفجر", "الظهر", "العصر", "المغرب", "العشاء")

        alarmTime.forEachIndexed { index, item ->
            val intent = Intent(context, AlarmReceiver::class.java).apply {
                putExtra("PRAYER_NAME", prayerNames[index])
            }
            val pendingIntent = PendingIntent.getBroadcast(
                context,
                index,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )


            val alarmClockInfo = AlarmManager.AlarmClockInfo(item, pendingIntent)
            alarmManager.setAlarmClock(alarmClockInfo,pendingIntent)

            Log.d("AlarmSchedularrImp", "Alarm set for: $item with request code: $index")
        }
    }

     fun cancelAlarms() {
        val prayerNames = listOf("الفجر", "الظهر", "العصر", "المغرب", "العشاء")
        prayerNames.forEachIndexed { index, _ ->
            val intent = Intent(context, AlarmReceiver::class.java)
            val pendingIntent = PendingIntent.getBroadcast(
                context,
                index,intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
            alarmManager.cancel(pendingIntent)
        }
    }

    override fun cancelAlarm(alarmTime: LocalDateTime) {
        // Not used in this implementation
    }

}