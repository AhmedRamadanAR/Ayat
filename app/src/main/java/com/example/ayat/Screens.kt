package com.example.ayat

import java.lang.IllegalArgumentException

enum class Screens {

  SurahsListScreen,
  SurahScreen;
  companion object {
    fun fromRoute(route: String):Screens=
      when(route.substringBefore("/")){
           SurahsListScreen.name->SurahsListScreen
            SurahScreen.name->SurahScreen

        else -> throw  IllegalArgumentException("Route $route is not recognized !")
      }

  }
}