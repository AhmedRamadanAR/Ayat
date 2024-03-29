package com.example.ayat

import android.app.Application
import android.content.Context
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class AyatApplication:Application(){

    init {
        application=this
    }
    companion object{
        private lateinit var application: Application
        fun getApplicationContext(): Context = application.applicationContext
    }
}