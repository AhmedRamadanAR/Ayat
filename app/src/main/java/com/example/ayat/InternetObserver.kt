package com.example.ayat

import kotlinx.coroutines.flow.Flow

interface InternetObserver {
    fun observe() :Flow<Status>
    enum class Status(){
         Available,
        Not_Available
    }
}