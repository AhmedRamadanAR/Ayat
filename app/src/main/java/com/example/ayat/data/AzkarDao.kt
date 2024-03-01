package com.example.ayat.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.ayat.MonthlyPrayerTime
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

    @Query("SELECT * FROM MonthlyPrayerTime")
    fun getAll(): List<MonthlyPrayerTime>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(monthlyPrayerTimes: List<MonthlyPrayerTime>)

    @Query("DELETE FROM MonthlyPrayerTime")
    fun deleteAll()
    @Query("DELETE FROM MonthlyPrayerTime WHERE monthNumberGregorian=:month")
    fun deleteMonth(month:Long)
    @Query("SELECT * FROM MonthlyPrayerTime WHERE dateGregorian = :date")
    fun getPrayerTimeByDate(date: String): MonthlyPrayerTime
    @Query("SELECT * FROM MonthlyPrayerTime")
    fun getPrayerTime(): MonthlyPrayerTime

    @Query("SELECT * FROM MonthlyPrayerTime WHERE timestamp = :timestamp")
    fun getPrayerTimeByTimestamp(timestamp: Long): MonthlyPrayerTime


}