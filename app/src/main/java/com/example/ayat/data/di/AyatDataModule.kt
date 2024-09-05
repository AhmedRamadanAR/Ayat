package com.example.ayat.data.di

import android.app.AlarmManager
import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.hilt.work.HiltWorkerFactory
import androidx.room.Room
import androidx.work.Configuration
import com.example.ayat.data.repositories.AzanRepository
import com.example.ayat.data.repositories.SettingsRepository
import com.example.ayat.data.retrofit.AyatApiService
import com.example.ayat.data.roomdb.AyatDB
import com.example.ayat.data.roomdb.RoomDao
import com.example.ayat.presentation.azan.MyWorker
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AyatDataModule {
    @Singleton
    @Provides
    fun provideRoomDatabase(
        @ApplicationContext
        context: Context
    ): AyatDB {
        return Room.databaseBuilder(context, AyatDB::class.java, "AyatDB")
            .fallbackToDestructiveMigration().build()

    }

    @Provides
    @Singleton
    fun provideSettingsRepository(
        sharedPreferences: SharedPreferences

    ): SettingsRepository {
        return SettingsRepository(sharedPreferences)
    }
    @Provides
    @Singleton
    fun provideSharedPreferences(application:Application
    ): SharedPreferences {
        return application.getSharedPreferences("ayat_prefs", Context.MODE_PRIVATE)
    }
    @Provides
    @Singleton
    fun provideAzanRepository(
        roomDao: RoomDao,
        apiService: AyatApiService
    ): AzanRepository {
        return AzanRepository(roomDao, apiService)
    }
    @Provides
    fun provideRoomDao(db: AyatDB): RoomDao {
        return db.dao
    }



    @Singleton
    @Provides
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://api.aladhan.com/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    }

    @Provides
    fun provideApiService(retrofit: Retrofit): AyatApiService {
        return retrofit.create(AyatApiService::class.java)
    }


}