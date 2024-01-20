package com.example.ayat
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.ayat.ui.SurahListScreen
import com.example.ayat.ui.SurahScreen
@Composable
fun setScreen(){
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

        )){ backStackEntry ->
            val surahId = backStackEntry.arguments?.getInt("surahId")
            SurahScreen(surahId = surahId ?:0)
        }


    }
}
