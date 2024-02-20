package com.example.ayat

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.InputStreamReader

class MorningEveningAzkarViewModel:ViewModel() {
    var monringList by mutableStateOf(emptyList<MorningEveningAzkar>())
    var EveningList by mutableStateOf(emptyList<MorningEveningAzkar>())
    private val errorHandler = CoroutineExceptionHandler { _, _ ->
    }

    init {
        getAzkarList()
    }
    private  fun getAzkarList(){
        viewModelScope.launch (errorHandler){
                val x=getAzkarListFromJSON()
                monringList=x.MorningAzkar
               EveningList=x.EveningAzkar
        }
    }
    private fun getAzkarListFromJSON():  MEAzkar {
        val jsonString = readJSONFromAssets("morningazkar.json")
        val gson = Gson()
        val NameList = gson.fromJson(jsonString, MEAzkar::class.java)
        return NameList
    }


    private fun readJSONFromAssets(path: String): String {
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