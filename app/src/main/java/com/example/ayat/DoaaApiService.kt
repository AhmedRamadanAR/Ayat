package com.example.ayat

import retrofit2.http.GET


interface DoaaApiService {
    @GET(".json")
    suspend fun getDoaa():List<Doaa>

//    @GET("audio/128/{edition}/{number}.mp3")
//    suspend fun getSurah(@Path("number") number: String,@Path("edition") edition:String)
//


}