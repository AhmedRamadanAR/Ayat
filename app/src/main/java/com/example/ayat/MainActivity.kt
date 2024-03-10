package com.example.ayat

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.graphics.painter.Painter
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen

import com.example.ayat.ui.theme.AyatTheme
import dagger.hilt.android.AndroidEntryPoint


data class BottomNavigationItem(
    val route:String,
    val title:String,
    val unSelectedIcon:Painter,
    val selectedIcon: Painter
)
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
installSplashScreen()
        setContent {

            AyatTheme {

                CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {

                   MyApp()

                }
            }
        }
    }
}
