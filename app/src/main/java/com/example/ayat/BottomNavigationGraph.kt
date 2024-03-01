package com.example.ayat

import AzanScreen
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

import com.example.ayat.presentation.azkar.AzkarScreen

@Composable
fun BottomNavigationGraph(navController:NavHostController){

    NavHost(navController = navController, startDestination = Screens.PrayingTimeScreen.name ){

        composable(route=Screens.PrayingTimeScreen.name){
            AzanScreen()
        }
        composable(route=Screens.SurahsListScreen.name){
               SetScreen()
            }
        composable(route=Screens.AzkarScreen.name){
AzkarScreen()
        }




    }
}