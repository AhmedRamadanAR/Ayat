package com.example.ayat.data.di

import android.content.Context
import androidx.room.Room
import com.example.ayat.data.retrofit.AyatApiService
import com.example.ayat.data.roomdb.AyatDB
import com.example.ayat.data.roomdb.RoomDao
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