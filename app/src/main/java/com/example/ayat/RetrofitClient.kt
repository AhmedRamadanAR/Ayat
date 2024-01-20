package com.example.ayat

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private fun instance(baseUrl:String):Retrofit =
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()


    fun apiServiceInstanceDoaa() :DoaaApiService = instance("https://ayat-1-default-rtdb.firebaseio.com/").create(DoaaApiService::class.java)
    fun apiServiceInstanceSurah():DoaaApiService= instance("https://cdn.islamic.network/quran/").create(DoaaApiService::class.java)
}

