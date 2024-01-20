package com.example.ayat.ui

import android.app.Application
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ayat.References
import com.example.ayat.SurahViewModel
import com.example.ayat.SurahViewModelFactory
import com.example.ayat.SurahsListViewModel
import com.example.ayat.SurahsListViewModelFactory
import com.example.ayat.ui.theme.darkGrey
import com.example.ayat.ui.theme.softPurple


@Composable
fun SurahListScreen(onItemClick: (Int) -> Unit) {
    val app = LocalContext.current.applicationContext as Application
    val vm: SurahsListViewModel = viewModel(factory = SurahsListViewModelFactory(app, SavedStateHandle()))

    LazyColumn(){
        item {
        }
        items(vm.surahDataState){
            SurahItem(Surah = it){id->
                onItemClick(id)
            }
        }
    }
}

@Composable
fun SurahItem(Surah:References, onItemClick:(Int)->Unit) {

    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        Card(
            Modifier
                .padding(8.dp)
                .clickable { onItemClick(Surah.number) },
            elevation = CardDefaults.cardElevation(4.dp),
            shape = RoundedCornerShape(10.dp),
            border = BorderStroke(1.dp, softPurple)

        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {

                Text(
                    text = Surah.name,
                    textAlign = TextAlign.Right,
                    style = TextStyle(
                        color = darkGrey,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Normal
                    )
                )


            }
            Row(modifier=Modifier.fillMaxSize(), horizontalArrangement = Arrangement.End){
                Text(text = " ترتيب السورة : "+"${Surah.number}", fontWeight = FontWeight.Normal,modifier=Modifier.weight(0.5f),textAlign = TextAlign.Start)

                Text(text = "عدد اياتها  : "+"${Surah.numberOfAyahs}", fontWeight = FontWeight.Normal,modifier=Modifier.weight(0.5f),textAlign = TextAlign.Center)
                Spacer(modifier = Modifier.padding(horizontal = 10.dp))

                Text(text = "${Surah.revelationType}", fontWeight = FontWeight.Normal,modifier=Modifier.weight(0.4f),textAlign = TextAlign.Center)
                Spacer(modifier = Modifier.padding(horizontal = 3.dp))

            }
        }

    }
}