package com.example.ayat.presentation.azan

import java.time.LocalDateTime

interface AlarmScheduler {
    fun scheduleAlarm(alarmTime:List<Long> )
    fun cancelAlarm(alarmTime: LocalDateTime)
}