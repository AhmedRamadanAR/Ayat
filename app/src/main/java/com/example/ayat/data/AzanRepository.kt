package com.example.ayat.data
import android.icu.text.SimpleDateFormat
import com.example.ayat.AyatApplication
import com.example.ayat.Azan
import com.example.ayat.MonthlyPrayerTime
import java.text.ParseException
import java.util.Locale

class AzanRepository {
      private var apiService: AyatApiService = RetrofitClient.apiServiceInstanceAzan()
    private val azanDao = AyatDB.getDaoInstance(AyatApplication.getApplicationContext())
    companion object {
        private const val TIME_FORMAT_24H = "HH:mm"
        private const val TIME_FORMAT_12H = "hh:mm a"
    }

    fun deleteFromDb(month:Long){

        azanDao.deleteMonth(month)

    }
    suspend fun callFromApitoDb(year:String,month:String){
        val data = apiService.getPrayerTime(year, month, "31.2001", "29.9187").data
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
   fun getprayerbydate(date:String):MonthlyPrayerTime{

        return  azanDao.getPrayerTimeByDate(date)

    }
   fun checking():Boolean{
        return azanDao.getAll().isEmpty()
    }
    suspend fun callForNextMonthApitoDb(year:String,nextMonth:String){
        val nextMonthData =
            apiService.getPrayerTime(year, nextMonth, "31.2001", "29.9187").data
        val nextMonthPrayerTimes = nextMonthData.map { mapToMonthlyPrayerTime(it) }
        azanDao.insertAll(nextMonthPrayerTimes)
    }

    private  fun mapToMonthlyPrayerTime(azan: Azan): MonthlyPrayerTime {
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