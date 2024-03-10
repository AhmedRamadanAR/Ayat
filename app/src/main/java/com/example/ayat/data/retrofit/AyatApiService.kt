package com.example.ayat.data.retrofit

import com.example.ayat.data.localdata.Root
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface AyatApiService {
//    @GET(".json")
//    suspend fun getDoaa(): List<Doaa>


    @GET("calendar/{year}/{month}")
    suspend fun getPrayerTime(
        @Path("year") year: String,
        @Path("month") month: String,
        @Query("latitude") latitude: String,
        @Query("longitude") longitude: String,
        @Query("method") method: Int = 5
    ): Root

}