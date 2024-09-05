package com.example.ayat.setttings

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ayat.data.repositories.SettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor( private val  repo: SettingsRepository
):ViewModel() {

    var switchState by mutableStateOf(false)
    var notificationEnabled by mutableStateOf(false)
    init {

        viewModelScope.launch {
            updateNotificationState()
            if (notificationEnabled==false){
                repo.setAzanEnabled(false)
            }
            repo.isAzanEnabled.collect{
                switchState=it
                notificationEnabled=it
            }
        }
    }
    fun changeSwitchState(){
       viewModelScope.launch {

         val newState=!switchState
           repo.setAzanEnabled(newState)
       }
    }
      fun updateNotificationState(){
        notificationEnabled=repo.isNotificationPermissionGranted()
    }


}