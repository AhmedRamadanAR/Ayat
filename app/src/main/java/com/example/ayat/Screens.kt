package com.example.ayat

import java.lang.IllegalArgumentException

enum class Screens {

  SurahsListScreen,
  SurahScreen,
    PrayingTimeScreen,
    AzkarScreen;
  companion object {
    fun fromRoute(route: String):Screens=
      when(route.substringBefore("/")){
           SurahsListScreen.name->SurahsListScreen
            SurahScreen.name->SurahScreen

        else -> throw  IllegalArgumentException("Route $route is not recognized !")
      }

  }
}