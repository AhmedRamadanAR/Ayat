package com.example.ayat.data

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private fun instance(baseUrl:String):Retrofit =
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

fun apiServiceInstanceAzan(): AyatApiService = instance("https://api.aladhan.com/v1/").create(
    AyatApiService::class.java)
    fun apiServiceInstanceDoaa() : AyatApiService = instance("https://ayat-1-default-rtdb.firebaseio.com/").create(
        AyatApiService::class.java)
    fun apiServiceInstanceSurah(): AyatApiService = instance("https://cdn.islamic.network/quran/").create(
        AyatApiService::class.java)
}

