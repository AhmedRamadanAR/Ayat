package com.example.ayat

import android.app.Application
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class SurahViewModelFactory(private val application: Application,
                            private val savedStateHandle: SavedStateHandle) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SurahViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SurahViewModel(application, savedStateHandle) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
class SurahsListViewModelFactory(private val application: Application,
                            private val savedStateHandle: SavedStateHandle) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SurahsListViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SurahsListViewModel(application, savedStateHandle) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}