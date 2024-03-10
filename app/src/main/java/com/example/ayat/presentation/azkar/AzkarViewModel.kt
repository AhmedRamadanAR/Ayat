package com.example.ayat.presentation.azkar
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ayat.data.repositories.AzkarRepository
import com.example.ayat.data.localdata.MyZekr
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
@HiltViewModel

class AzkarViewModel @Inject constructor(private val azkarRepository:AzkarRepository):ViewModel() {

    var zekrList = MutableStateFlow(listOf<MyZekr>())
    var selectedItem by  mutableStateOf(MyZekr(""))
    var showAddDialog by mutableStateOf(false)
    var showUpdateDialog by mutableStateOf(false)
    var showDeleteDialog by mutableStateOf(false)





    init {
        getAllAzkar()
    }

    private fun getAllAzkar() {
        viewModelScope.launch {
            azkarRepository.getAllAzkar().collect { list ->
                zekrList.value = list
            }
        }
    }

    fun addZekr(zekr: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                azkarRepository.addZekr(zekr)
            }
        }
    }

    fun deleteZekr(zekr: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                azkarRepository.deleteZekr(zekr)
            }
        }
    }
    fun updateZekr(oldZekr: String, newZekr: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                azkarRepository.updateZekr(oldZekr, newZekr)
            }
        }
    }
}