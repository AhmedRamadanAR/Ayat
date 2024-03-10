package com.example.ayat.data.retrofit

import kotlinx.coroutines.flow.Flow

interface InternetObserver {
    fun observe() :Flow<Status>
    enum class Status{
         Available,
        NotAvailable
    }
}