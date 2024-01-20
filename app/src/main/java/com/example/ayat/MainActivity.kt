package com.example.ayat

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent

import com.example.ayat.ui.theme.AyatTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {

            AyatTheme {
                //DoaaScreen()
                //SurahScreen()
                // SurahListScreen()
            setScreen()
                // AnimatedCrescentMoon()
            }
        }
    }
}
