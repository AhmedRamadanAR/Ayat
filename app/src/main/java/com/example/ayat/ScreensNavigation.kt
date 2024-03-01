package com.example.ayat
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.ayat.presentation.quran.SurahListScreen
import com.example.ayat.presentation.quran.SurahScreen
@Composable
fun SetScreen(){
    val navController= rememberNavController()

    NavHost(navController = navController, startDestination = Screens.SurahsListScreen.name ){

        composable(route=Screens.SurahsListScreen.name){
            SurahListScreen{surahId->
                navController.navigate(Screens.SurahsListScreen.name+"/$surahId")
            }
        }
        composable(route=Screens.SurahsListScreen.name+"/{surahId}", arguments = listOf(
            navArgument("surahId"){
                type= NavType.IntType
            }

        )){
            SurahScreen()
        }


    }
}
