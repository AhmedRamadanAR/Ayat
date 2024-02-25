package com.example.ayat

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface AyatApiService {
    @GET(".json")
    suspend fun getDoaa(): List<Doaa>

//    @GET("audio/128/{edition}/{number}.mp3")
//    suspend fun getSurah(@Path("number") number: String,@Path("edition") edition:String)
//

    @GET("calendar/{year}/{month}")
    suspend fun getPrayingTime(
        @Path("year") year: String,
        @Path("month") month: String,
        @Query("latitude") latitude: String,
        @Query("longitude") longitude: String,
        @Query("method") method: Int = 5
    ): Root

}