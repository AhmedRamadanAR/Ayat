package com.example.ayat.presentation.azkar

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ayat.data.localdata.MorningEveningAzkar
import com.example.ayat.data.repositories.SurahReopsitory
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel

class MorningEveningAzkarViewModel @Inject constructor(  private val surahReopsitory:SurahReopsitory):ViewModel() {
    var monringList by mutableStateOf(emptyList<MorningEveningAzkar>())
    var eveningList by mutableStateOf(emptyList<MorningEveningAzkar>())

    init {
        getAzkarList()
    }
    private  fun getAzkarList(){
        viewModelScope.launch {
                val azkar=surahReopsitory.getAzkarListFromJSON()
                monringList=azkar.MorningAzkar
               eveningList=azkar.EveningAzkar
        }
    }





}