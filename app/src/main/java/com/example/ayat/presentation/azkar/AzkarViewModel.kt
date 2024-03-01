package com.example.ayat.presentation.azkar

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ayat.AyatApplication
import com.example.ayat.data.AyatDB
import com.example.ayat.MyZekr
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AzkarViewModel:ViewModel() {
    var zekrList = MutableStateFlow(listOf<MyZekr>())
    var selectedItem by  mutableStateOf(MyZekr(""))
    var showAddDialog by mutableStateOf(false)
    var showUpdateDialog by mutableStateOf(false)
    var showDeleteDialog by mutableStateOf(false)



    private val errorHandler = CoroutineExceptionHandler { _, throwable ->
        throwable.printStackTrace()
        Log.d("boom", ": hhh")
    }

    val azkarDao = AyatDB.getDaoInstance(AyatApplication.getApplicationContext())

    init {
        getAllAzkar()
    }

    private fun getAllAzkar() {
        viewModelScope.launch(errorHandler) {
            azkarDao.getAllZekr().collect { list ->
                zekrList.value = list
            }
        }
    }

    fun addZekr(zekr: String) {
        viewModelScope.launch(errorHandler) {
            withContext(Dispatchers.IO) {
                azkarDao.addZekr(MyZekr(zekr))
            }
        }
    }

    fun deleteZekr(zekr: String) {
        viewModelScope.launch(errorHandler) {
            withContext(Dispatchers.IO) {
                azkarDao.deleteZekr(zekr)
            }
        }
    }
    fun updateZekr(oldZekr: String, newZekr: String) {
        viewModelScope.launch(errorHandler) {
            withContext(Dispatchers.IO) {
                azkarDao.updateZekr(oldZekr, newZekr)
            }
        }
    }
}