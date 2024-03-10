package com.example.ayat.data.repositories

import com.example.ayat.AyatApplication
import com.example.ayat.data.localdata.DataManger
import com.example.ayat.data.localdata.MEAzkar
import com.example.ayat.data.localdata.NameList
import com.example.ayat.data.localdata.Quran
import com.google.gson.Gson
import java.io.BufferedReader
import java.io.InputStreamReader
import javax.inject.Inject

class SurahReopsitory @Inject  constructor() {
    val lista = DataManger().lista

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
    fun getAzkarListFromJSON(): MEAzkar {
        val jsonString = readJSONFromAssets("morningazkar.json")
        val gson = Gson()
        return gson.fromJson(jsonString, MEAzkar::class.java)
    }

    fun getSurahListFromJSON(): NameList {
        val jsonString = readJSONFromAssets("References.json")
        val gson = Gson()
        return gson.fromJson(jsonString, NameList::class.java)
    }
    fun getSurahFromJSON(): Quran {
        val jsonString = readJSONFromAssets("quran.json")
        val gson = Gson()
        return gson.fromJson(jsonString, Quran::class.java)
    }
     private fun readJSONFromAssets(path:String): String {
        try {
            AyatApplication.getApplicationContext().assets.open(path).use { inputStream ->
                BufferedReader(InputStreamReader(inputStream)).use { reader ->
                    return reader.readText()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return ""
        }
    }
}