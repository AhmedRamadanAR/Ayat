package com.example.ayat

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import java.io.BufferedReader
import java.io.InputStreamReader

class SurahsListViewModel(private val app: Application, saveStateHandle: SavedStateHandle) : ViewModel() {
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
            app.applicationContext.assets.open(path).use { inputStream ->
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