package com.example.ayat.presentation.azan

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ayat.AyatApplication
import com.example.ayat.data.localdata.AzanState
import com.example.ayat.data.localdata.MonthlyPrayerTime
import com.example.ayat.data.repositories.AzanRepository
import com.google.android.gms.location.LocationServices
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Locale
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.time.Duration
import java.time.LocalDate
import java.time.format.DateTimeParseException
import javax.inject.Inject

@HiltViewModel

class AzanViewModel @Inject constructor(private val azanRepo : AzanRepository) : ViewModel() {

    private var _prayerTime = MutableStateFlow(
        AzanState(
            monthlyPrayerTime = MonthlyPrayerTime(dateGregorian = ""),
            isLoading = true,
            countDownTime = "",
            nextPrayerTime = "",
            nextFajrTime = ""
        )
    )
    val prayerTime: StateFlow<AzanState> = _prayerTime

    private val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
    init {
            startLocationUpdates()


    }
    @SuppressLint("MissingPermission")
    fun startLocationUpdates() {
        viewModelScope.launch(Dispatchers.IO) {
            val fusedLocationClient =
                LocationServices.getFusedLocationProviderClient(AyatApplication.getApplicationContext())
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                if (location != null) {
                    viewModelScope.launch(Dispatchers.IO) {
                        azanRepo.saveLocation(
                            location.latitude.toString(),
                            location.longitude.toString()
                        )

                        getMonthlyPrayerTime()
                    }
                } else {

                    getMonthlyPrayerTime()
                }
            }
        }
    }


    private fun getMonthlyPrayerTime() {
        viewModelScope.launch(Dispatchers.IO) {
             val current: LocalDateTime = LocalDateTime.now()
             val year = current.year.toString()
             val month = current.monthValue.toString()
            val nextMonth = current.plusMonths(1).monthValue.toString()
            val storedMonth = azanRepo.getStoredMonth()
            val latitude = azanRepo.getLatitude()
            val longitude = azanRepo.getLongitude()
            if (month.toInt() != storedMonth) {
                azanRepo.deleteFromDb(storedMonth.toLong())
            }
            try {
                azanRepo.callFromApitoDb(year, month, latitude, longitude)
                azanRepo.saveMonth(month.toInt())
                azanRepo.callForNextMonthApitoDb(year, nextMonth, latitude, longitude)
                getPrayerTimeDB()
            } catch (e: Exception) {
                getPrayerTimeDB()
            }
        }
    }


    private fun getPrayerTimeDB() {
        viewModelScope.launch(Dispatchers.IO) {
            Log.d("testing", "azanviewmodel: db")

            try {
                 val current: LocalDateTime = LocalDateTime.now()

                val currentDate = current.format(formatter)
                val nextDate = current.plusDays(1).format(formatter)
                val currentPrayerTime = azanRepo.getprayerbydate(currentDate)
                val nextPrayerTime = azanRepo.getprayerbydate(nextDate)

                withContext(Dispatchers.Main) {
                    _prayerTime.value = _prayerTime.value.copy(
                        monthlyPrayerTime = currentPrayerTime,
                        isLoading = true,
                        nextPrayerTime = getNextPrayerName(_prayerTime.value)
                    )
                    _prayerTime.value.nextFajrTime = nextPrayerTime.Fajr
                    val prayerTimes = listOf(
                        currentPrayerTime.Fajr,
                        currentPrayerTime.Dhuhr,
                        currentPrayerTime.Asr,
                        currentPrayerTime.Maghrib,
                        currentPrayerTime.Isha,
                        _prayerTime.value.nextFajrTime
                    )
                    startCountdown(prayerTimes)
                }
            } catch (ex: Exception) {
                Log.d("bla", "getPrayerTimeDB: $ex")
            }


        }
    }


    private fun startCountdown(prayerTimes: List<String>) {
        viewModelScope.launch {
            while (true) {
                val now = LocalDateTime.now()
                val futurePrayerTimes = prayerTimes
                    .map { azanRepo.convertTo12HoursFormat(it) }
                    .map {
                        LocalTime.parse(
                            it.trim('\u200E'),
                            DateTimeFormatter.ofPattern("hh:mm a", Locale.getDefault())
                        )
                    }
                    .mapIndexed { index, it ->
                        if (index == prayerTimes.size - 1 && now.toLocalTime().isAfter(it)) {
                            LocalDateTime.of(LocalDate.now().plusDays(1), it)
                        } else {
                            LocalDateTime.of(LocalDate.now(), it)
                        }
                    }
                    .filter { it.isAfter(now) }

                if (futurePrayerTimes.isEmpty()) {
                    getPrayerTimeDB()
                    continue
                }

                val nextPrayerTime = futurePrayerTimes.minOrNull()!!
                while (LocalDateTime.now().isBefore(nextPrayerTime)) {
                    delay(1000L)
                    val totalMinutes =
                        Duration.between(LocalDateTime.now(), nextPrayerTime).toMinutes()
                    val hours = totalMinutes / 60
                    val minutes = totalMinutes % 60
                    val seconds = Duration.between(LocalDateTime.now(), nextPrayerTime).seconds % 60
                    _prayerTime.value = _prayerTime.value.copy(
                        countDownTime = "${
                            hours.toString().padStart(2, '0')
                        }:${minutes.toString().padStart(2, '0')}:${
                            seconds.toString().padStart(2, '0')
                        }",
                        monthlyPrayerTime = _prayerTime.value.monthlyPrayerTime,
                        isLoading = false,
                        nextPrayerTime = getNextPrayerName(_prayerTime.value)
                    )


                }

                _prayerTime.value.nextPrayerTime = getNextPrayerName(_prayerTime.value)
            }
        }
    }

    private fun getNextPrayerName(prayerTime: AzanState): String {
        val now = LocalTime.now()
        val prayerTimes = listOf(
            "الفجر" to parseTime(prayerTime.monthlyPrayerTime.Fajr),
            "الظهر" to parseTime(prayerTime.monthlyPrayerTime.Dhuhr),
            "العصر" to parseTime(prayerTime.monthlyPrayerTime.Asr),
            "المغرب" to parseTime(prayerTime.monthlyPrayerTime.Maghrib),
            "العشاء" to parseTime(prayerTime.monthlyPrayerTime.Isha)
        )

        val futurePrayerTimes = prayerTimes.filter { it.second.isAfter(now) }
        return futurePrayerTimes.minByOrNull { it.second }?.first ?: "الفجر"
    }

    private fun parseTime(time: String): LocalTime {
        return try {
            LocalTime.parse(
                time.trim('\u200E'),
                DateTimeFormatter.ofPattern("hh:mm a", Locale.getDefault())
            )
        } catch (e: DateTimeParseException) {
            LocalTime.MIN
        }
    }

}