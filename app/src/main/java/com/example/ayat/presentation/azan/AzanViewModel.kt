package com.example.ayat.presentation.azan
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ayat.AyatApplication
import com.example.ayat.AzanState
import com.example.ayat.MonthlyPrayerTime
import com.example.ayat.data.AzanRepository
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
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

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class AzanViewModel : ViewModel() {
    private val azanRepo = AzanRepository()
   private var _prayerTime = MutableStateFlow(
        AzanState(
            monthlyPrayerTime = MonthlyPrayerTime(dateGregorian = ""),
            isLoading = true,
            countDownTime = "",
            nextPrayerTime = "",
            nextFajrTime = ""
        )
    )
    val prayerTime : StateFlow<AzanState> =_prayerTime

    private val current: LocalDateTime = LocalDateTime.now()
    private val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
    private val year = current.year.toString()
    private val month = current.monthValue.toString()
    private val errorHandler = CoroutineExceptionHandler { _, throwable ->
        throwable.printStackTrace()
        _prayerTime.value = _prayerTime.value.copy(isLoading = false, error = throwable.message)
    }
    private val save = intPreferencesKey("month")
    //  var latitude by mutableStateOf("")
    // var longitude by mutableStateOf("")
    
    // private val fusedLocationClient = LocationServices.getFusedLocationProviderClient(AyatApplication.getApplicationContext())
//   @SuppressLint("MissingPermission")
//   fun startLocationUpdates() {
//
//      val locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 10000)
//         .setMinUpdateIntervalMillis(5000)
//         .build()
//
//      val locationCallback = object : LocationCallback() {
//         override fun onLocationResult(locationResult: LocationResult) {
//            locationResult ?: return
//            for (location in locationResult.locations){
//                    this@AzanViewModel.latitude=location.latitude.toString()
//               this@AzanViewModel.longitude=location.longitude.toString()
//            }
//         }
//      }
//
//      fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
//   }


    init {
        viewModelScope.launch {
            getMonthlyPrayerTime()
        }
    }


    private fun getMonthlyPrayerTime() {
        viewModelScope.launch(Dispatchers.IO) {
            val nextMonth = current.plusMonths(1).monthValue.toString()
            val storedMonth =
                AyatApplication.getApplicationContext().dataStore.data.first()[save] ?: 0
            if (month.toInt() != storedMonth) {
                azanRepo.deleteFromDb(storedMonth.toLong())
            }
            try {
                azanRepo.callFromApitoDb(year, month)
                AyatApplication.getApplicationContext().dataStore.edit {
                    it[save] = month.toInt()
                }
                azanRepo.callForNextMonthApitoDb(year, nextMonth)
                getPrayerTimeDB()
            } catch (e: Exception) {
                getPrayerTimeDB()


            }
        }
    }


    private fun getPrayerTimeDB() {
        viewModelScope.launch(Dispatchers.IO + errorHandler) {
            try {
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
                if (azanRepo.checking()) {
                    throw Exception("please connect to the internet")
                }
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