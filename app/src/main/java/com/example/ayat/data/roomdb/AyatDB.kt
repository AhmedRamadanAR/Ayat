
package com.example.ayat.data.roomdb

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.ayat.data.localdata.MonthlyPrayerTime
import com.example.ayat.data.localdata.MyZekr


@Database(entities = [MyZekr::class, MonthlyPrayerTime::class], version = 1, exportSchema = false)
abstract class AyatDB : RoomDatabase() {
    abstract val dao: RoomDao



}