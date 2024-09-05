package com.example.ayat

import android.app.Application
import android.content.Context
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.work.Configuration
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.example.ayat.data.repositories.SettingsRepository
import com.example.ayat.presentation.azan.MyWorker
import com.example.ayat.presentation.azan.MyWorkerFactory
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltAndroidApp
class AyatApplication : Application(), Configuration.Provider {

    @Inject
    lateinit var workerFactory: MyWorkerFactory

    @Inject
    lateinit var settingsRepository: SettingsRepository
    override lateinit var workManagerConfiguration: Configuration


    override fun onCreate() {
        super.onCreate()
        workManagerConfiguration = Configuration.Builder().setWorkerFactory(workerFactory).build()

        observeAzanEnablePreference()
    }

    private fun observeAzanEnablePreference() {
        ProcessLifecycleOwner.get().lifecycleScope.launch {
            settingsRepository.isAzanEnabled.collect {
                if (it) {
                    initWork(it)
                } else {
cancelWork()
                }
            }
        }
    }

    companion object {
        private lateinit var application: Application
        fun getApplicationContext(): Context = application.applicationContext
    }

    init {
        application = this
    }
    private fun cancelWork() {
        WorkManager.getInstance(this).cancelUniqueWork("PrayerTimeWorker")
    }

    private fun initWork(isEnabled: Boolean) {
        val inputData = workDataOf("azan_enabled" to isEnabled)
        val dailyWorkRequest = PeriodicWorkRequestBuilder<MyWorker>(24, TimeUnit.HOURS)
            .setInputData(inputData)
            .build()
        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "PrayerTimeWorker",
            ExistingPeriodicWorkPolicy.UPDATE,
            dailyWorkRequest
        )
    }
}

