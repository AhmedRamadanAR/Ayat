package com.example.ayat

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.icu.text.SimpleDateFormat
import android.net.ConnectivityManager
import android.os.Looper
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ayat.DB.AyatDB
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.ParseException
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Locale
import androidx.lifecycle.*
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.time.Duration
import java.time.LocalDate
import java.time.format.DateTimeParseException
import java.util.Date

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")


class AzanViewModel : ViewModel() {
   private var apiService: AyatApiService = RetrofitClient.apiServiceInstanceAzan()
   val azanDao = AyatDB.getDaoInstance(AyatApplication.getApplicationContext())
   var prayingTime by mutableStateOf(MonthlyPrayingTime(dateGregorian = ""))
   val save = intPreferencesKey("month")
    val _countdownTime = MutableStateFlow("")
 //  var latitude by mutableStateOf("")
  // var longitude by mutableStateOf("")
     var nextFajrTime by mutableStateOf("")




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
     //    startLocationUpdates()
         delay(5000)

         if (isNetworkAvailable(AyatApplication.getApplicationContext())) {
            getMonthlyPrayingTime()
         }
         else{
getPrayingTimeDB()
         }
      }
   }


   fun getMonthlyPrayingTime(){
      viewModelScope.launch(Dispatchers.IO){
         val current = LocalDateTime.now()
         val year = current.year.toString()
         val month = current.monthValue.toString()
         val storedMonth = AyatApplication.getApplicationContext().dataStore.data.first()[save] ?: 0
         if (month.toInt() != storedMonth) {
            azanDao.deleteAll()
         }
         val data = apiService.getPrayingTime(year, month, "31.2001", "29.9187").data
         val monthlyPrayingTimes = data.map { mapDaumToMonthlyPrayingTime(it) }
         azanDao.insertAll(monthlyPrayingTimes)
         AyatApplication.getApplicationContext().dataStore.edit {
            it[save] = month.toInt()
         }
         getPrayingTimeDB()
      }
   }

   private fun startCountdown(prayerTimes: List<String>) {
      viewModelScope.launch {
         while (true) { // Keep looking for the next prayer time
            val now = LocalDateTime.now()
            val futurePrayerTimes = prayerTimes
               .map { convertTo12HoursFormat(it) }
               .map { LocalTime.parse(it.trim('\u200E'), DateTimeFormatter.ofPattern("hh:mm a", Locale.getDefault())) }
               .mapIndexed { index, it ->
                  if (index == prayerTimes.size - 1 && now.toLocalTime().isAfter(it)) {
                     // If the time is for Fajr of the next day and the current time is after it, add a day to the date
                     LocalDateTime.of(LocalDate.now().plusDays(1), it)
                  } else {
                     LocalDateTime.of(LocalDate.now(), it)
                  }
               }
               .filter { it.isAfter(now) } // Only consider future prayer times

            if (futurePrayerTimes.isEmpty()) {
               // If there are no future prayer times today, fetch prayer times for the next day
               getPrayingTimeDB()
               continue
            }

            val nextPrayerTime = futurePrayerTimes.minOrNull()!!
            while (LocalDateTime.now().isBefore(nextPrayerTime)) {
               delay(1000L)
               val totalMinutes = Duration.between(LocalDateTime.now(), nextPrayerTime).toMinutes()
               val hours = totalMinutes / 60
               val minutes = totalMinutes % 60
               val seconds = Duration.between(LocalDateTime.now(), nextPrayerTime).seconds % 60

               _countdownTime.value = "${hours.toString().padStart(2, '0')}:${minutes.toString().padStart(2, '0')}:${seconds.toString().padStart(2, '0')}"
            }
         }
      }
   }



   fun getPrayingTimeDB() {
      viewModelScope.launch(Dispatchers.IO) {
         val current = LocalDateTime.now()
         val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
         val currentDate = current.format(formatter)
         val nextDate = current.plusDays(1).format(formatter)

         val currentPrayingTime = azanDao.getPrayingTimeByDate(currentDate)
         val nextPrayingTime = azanDao.getPrayingTimeByDate(nextDate)

         withContext(Dispatchers.Main) {
            this@AzanViewModel.prayingTime = currentPrayingTime
            this@AzanViewModel.nextFajrTime = nextPrayingTime.Fajr
            val prayerTimes = listOf(
               currentPrayingTime.Fajr,
               currentPrayingTime.Dhuhr,
               currentPrayingTime.Asr,
               currentPrayingTime.Maghrib,
               currentPrayingTime.Isha,
               nextFajrTime
            )
            startCountdown(prayerTimes)
         }
      }
   }

   private fun mapDaumToMonthlyPrayingTime(daum: Daum): MonthlyPrayingTime {

      return MonthlyPrayingTime(
         Fajr = convertTo12HoursFormat(daum.timings.Fajr),
         Sunrise = convertTo12HoursFormat(daum.timings.Sunrise),
         Dhuhr = convertTo12HoursFormat(daum.timings.Dhuhr),
         Asr = convertTo12HoursFormat(daum.timings.Asr),
         Sunset = convertTo12HoursFormat(daum.timings.Sunset),
         Maghrib = convertTo12HoursFormat(daum.timings.Maghrib),
         Isha = convertTo12HoursFormat(daum.timings.Isha),
         dateHijri = daum.date.hijri.date,
         dayHijri = daum.date.hijri.day,
         monthNumberHijri = daum.date.hijri.month.number,
         monthNumberGregorian = daum.date.gregorian.month.number,
         monthArabicHijri = daum.date.hijri.month.ar,
         yearGregorian = daum.date.gregorian.year,
         dateGregorian = daum.date.gregorian.date,
         formatGregorian = daum.date.gregorian.format,
         dayGregorian = daum.date.gregorian.day,
         yearHijri = daum.date.hijri.year,
         today=daum.date.hijri.weekday.ar
      )
   }

   fun convertTo12HoursFormat(time: String): String {
      val _24HourSDF = SimpleDateFormat("HH:mm", Locale.getDefault())
      val _12HourSDF = SimpleDateFormat("hh:mm a", Locale.getDefault())
      return try {
         val _24HourDt = _24HourSDF.parse(time)
         val time12 = _12HourSDF.format(_24HourDt)

         "\u200E$time12\u200E" // Add Left-to-Right Mark (LRM) before and after the time string
      } catch (e: ParseException) {
         time
      }
   }

}
@SuppressLint("ServiceCast")
fun isNetworkAvailable(context: Context): Boolean {
   val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
   val networkInfo = connectivityManager.activeNetworkInfo
   return networkInfo != null && networkInfo.isConnected
}
