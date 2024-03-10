package com.example.ayat.data.repositories

import android.util.Log
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.ayat.AyatApplication
import javax.inject.Inject

class  LocationRepository @Inject constructor() {

    private val latitudeSave = stringPreferencesKey("latitude")
    private val longitudeSave = stringPreferencesKey("longitude")
    suspend fun saveLocation(latitude:String,longitude: String){
        AyatApplication.getApplicationContext().dataStore.edit {
            Log.d("error", "saveLocation:$latitude ")
            it[latitudeSave] = latitude
            it[longitudeSave] = longitude
        }
    }
}