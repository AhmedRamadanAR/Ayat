package com.example.ayat.presentation.quran

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ayat.AyatApplication
import com.example.ayat.NameList
import com.example.ayat.References
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import java.io.BufferedReader
import java.io.InputStreamReader

class SurahsListViewModel() : ViewModel() {
    var surahDataState by mutableStateOf(emptyList<References>())
    private val errorHandler = CoroutineExceptionHandler { _, _ ->
    }
    init {
        getSurahsLists()
    }
    private fun getSurahsLists(){
        viewModelScope.launch (errorHandler){
            val surahsList=getSurahListFromJSON()
            surahDataState=surahsList.references
        }
    }
    private fun getSurahListFromJSON(): NameList {
        val jsonString = readJSONFromAssets("References.json")
        val gson = Gson()
        val NameList = gson.fromJson(jsonString, NameList::class.java)
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