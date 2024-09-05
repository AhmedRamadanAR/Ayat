package com.example.ayat.presentation.azan

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import com.example.ayat.AyatApplication
import com.example.ayat.data.repositories.AzanRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import javax.inject.Inject


@HiltWorker
class MyWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParameters: WorkerParameters,
    private val azanRepository: AzanRepository

) : CoroutineWorker(context, workerParameters) {

    override suspend fun doWork(): Result {
        return try {
            getFire()
            Result.success()
        } catch (ex: Exception) {
            Result.retry()
        }
    }

    private fun convertTimeStringToMillis(timeStrings: List<String>): List<Long> {
        val currentDate = LocalDate.now()
        val currentTime = LocalTime.now()
        return timeStrings.map { timeString ->
            val cleanedTimeString = timeString.replace(Regex("[^\\x20-\\x7E]"), "").trim()
            val time = LocalTime.parse(cleanedTimeString, DateTimeFormatter.ofPattern("h:mm a"))
            var dateTime = LocalDateTime.of(currentDate, time)

            if (time.isBefore(currentTime)) {
                dateTime = dateTime.plusDays(1)
            }

            val zonedDateTime = dateTime.atZone(ZoneId.systemDefault())
            zonedDateTime.toEpochSecond() * 1000L
        }
    }

    private  suspend fun getFire() {
        val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
        val alarmScheduler = AlarmSchedularrImp(AyatApplication.getApplicationContext())
        val current: LocalDateTime = LocalDateTime.now()
        val currentDate = current.format(formatter)
        val item = withContext(Dispatchers.IO) {
            azanRepository.getprayerbydate(currentDate)
        }
            val alarmTimeInMillis =
                convertTimeStringToMillis(
                    listOf(
                        item.Fajr,
                        item.Dhuhr,
                        item.Asr,
                        item.Maghrib,
                        item.Isha
                    )
                )
            alarmScheduler.scheduleAlarm(alarmTimeInMillis)

    }
}
class MyWorkerFactory @Inject constructor(
    private val azanRepository: AzanRepository
) : WorkerFactory() {

    override fun createWorker(
        appContext:Context,
        workerClassName: String,
        workerParameters: WorkerParameters
    ): ListenableWorker? {
        return when (workerClassName) {
            MyWorker::class.java.name -> MyWorker(appContext, workerParameters, azanRepository)
            else -> null
        }
    }
}