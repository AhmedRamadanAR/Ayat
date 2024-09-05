package com.example.ayat.data.repositories

import android.app.NotificationManager
import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import com.example.ayat.AyatApplication
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

class SettingsRepository @Inject constructor(private val sharedPreferences: SharedPreferences) {
    private var _isAzanEnabled = MutableStateFlow(isAzanEnabledFromPrefs())

    val isAzanEnabled: StateFlow<Boolean> = _isAzanEnabled.asStateFlow()

    suspend fun setAzanEnabled(enabled: Boolean) {
        sharedPreferences.edit().putBoolean("azan_enabled", enabled).apply()
        _isAzanEnabled.value = enabled
    }

    private fun isAzanEnabledFromPrefs(): Boolean {
        return sharedPreferences.getBoolean("azan_enabled", false)
    }

     fun isNotificationPermissionGranted(): Boolean {
        val notificationManager = AyatApplication.getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = notificationManager.getNotificationChannel("AzanChannel")
            notificationManager.areNotificationsEnabled() && channel?.importance != NotificationManager.IMPORTANCE_NONE
        } else {
            notificationManager.areNotificationsEnabled()
        }
    }
}