package com.example.ayat.presentation.navigation

import AzanScreen
import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.ayat.presentation.azan.AzanViewModel

import com.example.ayat.presentation.azkar.AzkarScreen
import com.example.ayat.presentation.azkar.AzkarViewModel
import com.example.ayat.presentation.azkar.MorningEveningAzkarViewModel
import com.example.ayat.presentation.location.LocationScreen
import com.example.ayat.presentation.location.LocationViewModel


@SuppressLint("ComposableDestinationInComposeScope")

@Composable
fun OnBoardingNavigationGraph(navController: NavHostController){
    val context = LocalContext.current
    val sharedPreferences = context.getSharedPreferences("onBoarding", Context.MODE_PRIVATE)
    val isFirstLaunch = sharedPreferences.getBoolean("isFirstLaunch", true)

    NavHost(
        navController = navController,
        startDestination = if (isFirstLaunch) Screens.LocationScreen.name else Screens.AzanScreen.name
    ) {
        composable(Screens.LocationScreen.name) {
            val vm:LocationViewModel= hiltViewModel()
            LocationScreen(vm) {
                sharedPreferences.edit().putBoolean("isFirstLaunch", false).apply()
                navController.navigate(Screens.AzanScreen.name)
            }
        }
        composable(route = Screens.AzanScreen.name) {
            val vm:AzanViewModel= hiltViewModel()
            AzanScreen(vm)
        }
        composable(route = Screens.SurahsListScreen.name) {
            SetScreen()
        }
        composable(route = Screens.AzkarScreen.name) {
            val vm : MorningEveningAzkarViewModel= hiltViewModel()
            val vmME : AzkarViewModel= hiltViewModel()

            AzkarScreen(vm,vmME)
        }
    }
}


