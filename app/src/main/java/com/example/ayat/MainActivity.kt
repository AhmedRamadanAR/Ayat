package com.example.ayat

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButtonDefaults.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.LayoutDirection
import com.example.ayat.ui.AzkarScreen

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.runtime.Composable

import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController


import com.example.ayat.ui.theme.AyatTheme
import com.example.ayat.ui.theme.Purple40
import com.example.ayat.ui.theme.Purple80
import com.example.ayat.ui.theme.darkGrey

data class BottomNavigationItem(
    val route:String,
    val title:String,
    val unSelectedIcon:Painter,
    val selectedIcon: Painter
)
class MainActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {

            AyatTheme {


                CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
                    //DoaaScreen()
                    //SurahScreen()
                    MyApp()
                    // SurahListScreen()
                    //   setScreen()
                   // MorningEveningScreen()
                    //ItemsScreen()
                    // AnimatedCrescentMoon()
                }
            }
        }
    }
}
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MyApp(){
    val navController= rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val items= listOf(
        BottomNavigationItem(route=Screens.PrayingTimeScreen.name,title="مواقيت الصلاة", selectedIcon = painterResource(id = R.drawable.praying), unSelectedIcon = painterResource(id = R.drawable.praying)),
        BottomNavigationItem(route=Screens.SurahsListScreen.name,title="القرآن", selectedIcon = painterResource(id = R.drawable.filledquran), unSelectedIcon = painterResource(id = R.drawable.quran)),
        BottomNavigationItem(route =Screens.AzkarScreen.name, title="أذكار", selectedIcon = painterResource(id = R.drawable.filledopenhands), unSelectedIcon = painterResource(id = R.drawable.openhands))
    )
    Scaffold (
        modifier= Modifier.fillMaxSize(),
        bottomBar = {
            NavigationBar(contentColor = Purple80, modifier =Modifier.fillMaxWidth().height(65.dp)) {
                items.forEach { screen->
                    NavigationBarItem(selected = currentRoute==screen.title, onClick = {
                        navController.navigate(screen.route){
                            popUpTo(navController.graph.findStartDestination().id){saveState=true}
                            launchSingleTop=true
                            restoreState=true
                        }

                    },label={ Text(text = screen.title, fontSize = 15.sp)}, icon = { Icon(modifier=Modifier.size(30.dp),  tint = if(currentRoute==screen.route) Purple40 else darkGrey, //
                        painter = if(currentRoute==screen.route){
                            screen.selectedIcon
                        }else{
                            screen.unSelectedIcon
                             } , contentDescription = screen.title)})


                }
            }
        }
    ){
        // SetScreen()
        BottomNavigationGraph(navController)
//Text(text = "hello")
        //  AzkarScreen()

    }


}
