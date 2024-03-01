@file:OptIn(InternalCoroutinesApi::class)

package com.example.ayat.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.ayat.MonthlyPrayerTime
import com.example.ayat.MyZekr
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.internal.synchronized


@Database(entities = [MyZekr::class,MonthlyPrayerTime::class], version = 1, exportSchema = false)
abstract class AyatDB : RoomDatabase() {
    abstract val dao: AzkarDao
    companion object {
        @Volatile
        private var daoInstance : AyatDB?=null
       private fun BuildDatabase(context: Context) : AyatDB =
            Room.databaseBuilder(context, AyatDB::class.java, "AyatDB")
                .fallbackToDestructiveMigration().build()

        @OptIn(InternalCoroutinesApi::class)
        fun getDaoInstance(context: Context): AzkarDao {
            synchronized(this) {
                if (daoInstance == null) {
                    daoInstance = BuildDatabase(context)
                }
                return daoInstance!!.dao
            }
        }

    }

}