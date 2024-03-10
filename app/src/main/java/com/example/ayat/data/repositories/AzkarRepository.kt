package com.example.ayat.data.repositories

import com.example.ayat.AyatApplication
import com.example.ayat.data.roomdb.AyatDB
import com.example.ayat.data.localdata.MyZekr
import com.example.ayat.data.roomdb.RoomDao
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AzkarRepository  @Inject constructor(private val azkarDao:RoomDao){
    fun getAllAzkar(): Flow<List<MyZekr>> {
        return azkarDao.getAllZekr()
    }

    suspend fun addZekr(zekr: String) {
        azkarDao.addZekr(MyZekr(zekr))

    }

    suspend fun deleteZekr(zekr: String) {
        azkarDao.deleteZekr(zekr)

    }

    suspend fun updateZekr(oldZekr: String, newZekr: String) {

        azkarDao.updateZekr(oldZekr, newZekr)

    }

}