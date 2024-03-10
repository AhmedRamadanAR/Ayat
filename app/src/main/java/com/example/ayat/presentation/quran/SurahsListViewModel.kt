package com.example.ayat.presentation.quran

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ayat.data.localdata.References
import com.example.ayat.data.repositories.SurahReopsitory
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SurahsListViewModel @Inject constructor(private val surahRepo:SurahReopsitory) : ViewModel() {
    var surahDataState by mutableStateOf(emptyList<References>())

    init {
        getSurahsLists()
    }
    private fun getSurahsLists(){
        viewModelScope.launch (){
            val surahsList=surahRepo.getSurahListFromJSON()
            surahDataState=surahsList.references
        }
    }


}