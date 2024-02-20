package com.example.ayat.DB

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.ayat.MyZekr
import kotlinx.coroutines.flow.Flow

@Dao
interface AzkarDao {
    @Query("SELECT * FROM MyZekr")
     fun getAllZekr(): Flow<List<MyZekr>>
    @Insert
    suspend fun addZekr(zekr: MyZekr)
    @Query("UPDATE myzekr SET zekr = :newZekr WHERE zekr = :oldZekr")
    suspend fun updateZekr(oldZekr: String, newZekr: String)
    @Query("DELETE FROM myzekr WHERE zekr = :zekr")
    suspend fun deleteZekr(zekr: String)

}