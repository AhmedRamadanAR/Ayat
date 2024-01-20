package com.example

class DataManger {
    val lista = listOf(
        "عبد الباسط (المرتل)",
        "عبد الباسط",
        "الحصرى",
        "الحصري (المجود)",
        " المنشاوي",
        "المنشاوي (المجود)",
        "عبدالرحمن السديس",
        "مشاري العفاسي",
        "ماهر المعيقلي",
        "أحمد بن علي العجمي",
        "محمد أيوب",
        "محمد جبريل",
        "علي الحذيفي",
        "سعود الشريم"
    )
    fun qarieNameToUrl(name:String):String {
        val convertion: String = when (name) {
            "عبد الباسط (المرتل)" -> "ar.abdulbasitmurattal"
            "عبد الباسط" -> "ar.abdulsamad"
            "الحصرى" -> "ar.husary"
            "الحصري (المجود)" -> "ar.husarymujawwad"
            "عبدالرحمن السديس" -> "ar.abdurrahmaansudais"
            "أحمد بن علي العجمي" -> "ar.ahmedajamy"
            "مشاري العفاسي" -> "ar.alafasy"
            "علي الحذيفي" -> "ar.hudhaify"
            "ماهر المعيقلي" -> "ar.mahermuaiqly"
            " المنشاوي" -> "ar.minshawi"
            "المنشاوي (المجود)" -> "ar.minshawimujawwad"
            "محمد أيوب" -> "ar.muhammadayyoub"
            "محمد جبريل" -> "ar.muhammadjibreel"
            "سعود الشريم" -> "ar.saoodshuraym"

            else -> {
                ""
            }
        }
return convertion
    }


}



