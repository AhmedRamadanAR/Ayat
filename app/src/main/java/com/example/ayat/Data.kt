package com.example.ayat

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

    @SerializedName("number"         ) var number         : Int,
    @SerializedName("audio"          ) var audio          : String,
    @SerializedName("audioSecondary" ) var audioSecondary : ArrayList<String> = arrayListOf(),
    @SerializedName("text"           ) var text           : String,
    @SerializedName("numberInSurah"  ) var numberInSurah  : Int ,
    @SerializedName("juz"            ) var juz            : Int,
    @SerializedName("manzil"         ) var manzil         : Int ,
    @SerializedName("page"           ) var page           : Int ,
    @SerializedName("ruku"           ) var ruku           : Int ,
    @SerializedName("hizbQuarter"    ) var hizbQuarter    : Int ,

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
    @SerializedName("edition"                ) var edition                : Edition= Edition()
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

