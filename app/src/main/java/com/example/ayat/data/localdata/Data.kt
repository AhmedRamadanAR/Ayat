package com.example.ayat.data.localdata

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName


@Entity (tableName = "MyZekr")
data class MyZekr(
    @PrimaryKey
    var zekr:String
)
data class MEAzkar (
    @SerializedName("Morning_Azkar" ) var MorningAzkar : ArrayList<MorningEveningAzkar> = arrayListOf(),
    @SerializedName("Evening_Azkar" ) var EveningAzkar : ArrayList<MorningEveningAzkar> = arrayListOf()
)
data class MorningEveningAzkar (
    var showAnimation : Boolean = false,
    @SerializedName("count"       ) var count: Int = 0,
    @SerializedName("description" ) var description : String = "",
    @SerializedName("content"     ) var content     : String = ""

)
data class Doaa(
@SerializedName("arab")
val doaatext:String,
var isFavouite:Boolean=false
)



data class Ayahs (

    @SerializedName("number"         ) var number         : Int=0,
    @SerializedName("audio"          ) var audio          : String="",
    @SerializedName("audioSecondary" ) var audioSecondary : ArrayList<String> = arrayListOf(),
    @SerializedName("text"           ) var text           : String="",
    @SerializedName("numberInSurah"  ) var numberInSurah  : Int =0,
    @SerializedName("juz"            ) var juz            : Int=0,
    @SerializedName("manzil"         ) var manzil         : Int =0,
    @SerializedName("page"           ) var page           : Int =0,
    @SerializedName("ruku"           ) var ruku           : Int =0,
    @SerializedName("hizbQuarter"    ) var hizbQuarter    : Int =0,

)

data class Edition (

    @SerializedName("identifier"  ) var identifier  : String="",
    @SerializedName("language"    ) var language    : String="",
    @SerializedName("name"        ) var name        : String="",
    @SerializedName("englishName" ) var englishName : String="",
    @SerializedName("format"      ) var format      : String="",
    @SerializedName("type"        ) var type        : String="",
    @SerializedName("direction"   ) var direction   : String=""

)
data class Quran (

    @SerializedName("code"   ) var code   : Int,
    @SerializedName("status" ) var status : String,
    @SerializedName("data"   ) var data   : Data

)
data class Data(
    @SerializedName("surahs"                 ) var surahs                 : List<Surahs> = listOf(),

    )
data class Surahs (

    @SerializedName("number"                 ) var number                 : Int =0,
    @SerializedName("name"                   ) var name                   : String="",
    @SerializedName("englishName"            ) var englishName            : String="",
    @SerializedName("englishNameTranslation" ) var englishNameTranslation : String="",
    @SerializedName("revelationType"         ) var revelationType         : String="",
    @SerializedName("numberOfAyahs"          ) var numberOfAyahs          : Int=0,
    @SerializedName("ayahs"                  ) var ayahs                  : ArrayList<Ayahs> = arrayListOf(),
    @SerializedName("edition"                ) var edition                : Edition = Edition()
)
data class NameList (

    @SerializedName("references" ) var references : ArrayList<References> = arrayListOf()

)
data class References (

    @SerializedName("number"                 ) var number                 : Int    = 0,
    @SerializedName("name"                   ) var name                   : String = "",
    @SerializedName("englishName"            ) var englishName            : String = "",
    @SerializedName("englishNameTranslation" ) var englishNameTranslation : String = "",
    @SerializedName("numberOfAyahs"          ) var numberOfAyahs          : Int    = 0,
    @SerializedName("revelationType"         ) var revelationType         : String = ""

)
data class HomeDoaaList(val homedoaa: List<HomeDoaa> = listOf<HomeDoaa>())

data class HomeDoaa(
    val text: String="",
)
data class Location(
    val latitude: Double,
    val longitude: Double,
)
data class Method(
    val location: Location,
)
data class Meta(
    val latitude: Double,
    val longitude: Double,
    val timezone: String,
    val method: Method,
)
data class Month2(
    val number: Long,
    val en: String,
    val ar: String,
)
data class Weekday2(
    val en: String,
    val ar: String,
)
data class Hijri(
    val date: String,
    val format: String,
    val day: String,
    val weekday: Weekday2,
    val month: Month2,
    val year: String,
)
data class Weekday(
    val en: String,
)

data class Month(
      val number: Long,
    val en: String,
)
data class Gregorian(
    @SerializedName("date"  )  val date: String,
    @SerializedName("format"  )   val format: String,
    @SerializedName("day"  )  val day: String,
    @SerializedName("weekday"  )   val weekday: Weekday,
    @SerializedName("month"  )  val month: Month,
    @SerializedName("year"  )  val year: String,
)
data class Date(
    @SerializedName("readable"  )  val readable: String,
    @SerializedName("timestamp"  )   val timestamp: String,
    @SerializedName("gregorian"  ) val gregorian: Gregorian,
    @SerializedName("hijri"  )   val hijri: Hijri,
)

data class Timings (

    @SerializedName("Fajr"       ) var Fajr       : String= "",
    @SerializedName("Sunrise"    ) var Sunrise    : String= "",
    @SerializedName("Dhuhr"      ) var Dhuhr      : String= "",
    @SerializedName("Asr"        ) var Asr        : String= "",
    @SerializedName("Sunset"     ) var Sunset     : String= "",
    @SerializedName("Maghrib"    ) var Maghrib    : String= "",
    @SerializedName("Isha"       ) var Isha       : String= "",
    @SerializedName("Imsak"      ) var Imsak      : String= "",
    @SerializedName("Midnight"   ) var Midnight   : String= "",
    @SerializedName("Firstthird" ) var Firstthird : String= "",
    @SerializedName("Lastthird"  ) var Lastthird  : String= ""

)

data class Root(
    @SerializedName("code"  )    val code: Long,
    @SerializedName("status"  )    val status: String,
    @SerializedName("data"  )  val data: List<Azan>,
)
data class Azan(
    @SerializedName("timings"  )    val timings: Timings,
    @SerializedName("date"  )  val date: Date,
    @SerializedName("meta"  )    val meta: Meta,
)
@Entity
data class MonthlyPrayerTime(
 var Fajr       : String= "",
   var Sunrise    : String= "",
   var Dhuhr      : String= "",
  var Asr        : String= "",
 var Sunset     : String= "",
 var Maghrib    : String= "",
  var Isha       : String= "",
 val dateHijri: String="",
 val dayHijri: String="",
 val timestamp: String="",
 val monthNumberHijri: Long=0,
 val monthNumberGregorian: Long=0,
 val monthArabicHijri: String="",
 val yearGregorian: String="",
 @PrimaryKey
 val dateGregorian: String,
 val formatGregorian: String="",
 val dayGregorian: String="",
 val yearHijri: String="",
    val today:String=""
)
data class AzanState(val monthlyPrayerTime: MonthlyPrayerTime,
                     val isLoading:Boolean, val countDownTime:String, var nextPrayerTime:String,
                     var nextFajrTime:String, var error:String?=null)



