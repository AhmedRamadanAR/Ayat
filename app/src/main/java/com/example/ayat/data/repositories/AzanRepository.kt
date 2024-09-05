package com.example.ayat.data.repositories

import android.app.NotificationManager
import android.content.Context
import android.icu.text.SimpleDateFormat
import android.media.MediaPlayer
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.ayat.AyatApplication
import com.example.ayat.R
import com.example.ayat.data.localdata.Azan
import com.example.ayat.data.localdata.MonthlyPrayerTime
import com.example.ayat.data.retrofit.AyatApiService
import com.example.ayat.data.roomdb.AyatDB
import com.example.ayat.data.roomdb.RoomDao
import kotlinx.coroutines.flow.first
import java.text.ParseException
import java.util.Locale
import javax.inject.Inject

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")


class AzanRepository @Inject constructor( private val azanDao:RoomDao, private var apiService: AyatApiService) {

    companion object {
        private const val TIME_FORMAT_24H = "HH:mm"
        private const val TIME_FORMAT_12H = "hh:mm a"
    }
    private val save = intPreferencesKey("month")
    private val latitudeSave = stringPreferencesKey("latitude")
    private val longitudeSave = stringPreferencesKey("longitude")
    suspend fun getStoredMonth():Int=
        AyatApplication.getApplicationContext().dataStore.data.first()[save] ?: 0


    suspend fun getLatitude():String=
        AyatApplication.getApplicationContext().dataStore.data.first()[latitudeSave] ?: ""


    suspend fun getLongitude():String=
        AyatApplication.getApplicationContext().dataStore.data.first()[longitudeSave] ?: ""


    fun deleteFromDb(month:Long){

        azanDao.deleteMonth(month)

    }
    suspend fun saveLocation(latitude:String,longitude: String){
        AyatApplication.getApplicationContext().dataStore.edit {
            it[latitudeSave] = latitude
            it[longitudeSave] = longitude
        }
    }
    suspend fun saveMonth(month:Int){
        AyatApplication.getApplicationContext().dataStore.edit {
            it[save] = month
        }
    }


    suspend fun callFromApitoDb(year:String,month:String,latitude: String,longitude: String){
        val data = apiService.getPrayerTime(year, month, latitude, longitude).data
        val monthlyPrayerTimes = data.map { mapToMonthlyPrayerTime(it) }
        azanDao.insertAll(monthlyPrayerTimes)

    }
    fun convertTo12HoursFormat(time: String): String {
        val formatTwentyFour = SimpleDateFormat(TIME_FORMAT_24H, Locale.getDefault())
        val formatTwelve = SimpleDateFormat(TIME_FORMAT_12H, Locale.getDefault())
        return try {
            val formatting = formatTwentyFour.parse(time)
            "\u200E${formatTwelve.format(formatting)}\u200E"
        } catch (e: ParseException) {
            time
        }
    }
   fun getprayerbydate(date:String): MonthlyPrayerTime {

        return  azanDao.getPrayerTimeByDate(date)

    }
    fun getDataFromDB(): List<MonthlyPrayerTime> {
        return azanDao.getAll()
    }
    suspend fun callForNextMonthApitoDb(year:String,nextMonth:String,latitude:String,longitude:String){
        val nextMonthData =
            apiService.getPrayerTime(year, nextMonth, latitude, longitude).data
        val nextMonthPrayerTimes = nextMonthData.map { mapToMonthlyPrayerTime(it) }
        azanDao.insertAll(nextMonthPrayerTimes)
    }

    private fun mapToMonthlyPrayerTime(azan: Azan): MonthlyPrayerTime {
        return MonthlyPrayerTime(
            Fajr = convertTo12HoursFormat(azan.timings.Fajr),
            Sunrise = convertTo12HoursFormat(azan.timings.Sunrise),
            Dhuhr = convertTo12HoursFormat(azan.timings.Dhuhr),
            Asr = convertTo12HoursFormat(azan.timings.Asr),
            Sunset = convertTo12HoursFormat(azan.timings.Sunset),
            Maghrib = convertTo12HoursFormat(azan.timings.Maghrib),
            Isha = convertTo12HoursFormat(azan.timings.Isha),
            dateHijri = azan.date.hijri.date,
            dayHijri = azan.date.hijri.day,
            monthNumberHijri = azan.date.hijri.month.number,
            monthNumberGregorian = azan.date.gregorian.month.number,
            monthArabicHijri = azan.date.hijri.month.ar,
            yearGregorian = azan.date.gregorian.year,
            dateGregorian = azan.date.gregorian.date,
            formatGregorian = azan.date.gregorian.format,
            dayGregorian = azan.date.gregorian.day,
            yearHijri = azan.date.hijri.year,
            today = azan.date.hijri.weekday.ar
        )
    }


}