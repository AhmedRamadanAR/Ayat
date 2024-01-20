@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.ayat.ui


import android.app.Application

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSizeIn
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.MutableState

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.SavedStateHandle

import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ayat.Ayahs
import com.example.ayat.R
import com.example.ayat.SurahViewModel
import com.example.ayat.SurahViewModelFactory

import com.example.ayat.ui.theme.AyatTheme
import com.example.ayat.ui.theme.darkGrey
import com.example.ayat.ui.theme.softPurple


@Composable
fun SurahScreen(surahId:Int) {

    val app = LocalContext.current.applicationContext as Application
    val vm: SurahViewModel = viewModel(
        factory = SurahViewModelFactory(app, SavedStateHandle(mapOf("surahId" to surahId))))
    LazyColumn {
        item {
            Slider(
                value = vm.mediaProgress,
                valueRange = -1f..vm.mediaDuration,
                onValueChange = { newProgress ->
                    vm.mediaPlayer?.seekTo(newProgress.toInt())
                }
            )
            val minutes = (vm.mediaDuration / 60).toInt()
            val seconds = (vm.mediaDuration % 60).toInt()
            Text(text = String.format("%02d:%02d", minutes, seconds))
        }


        itemsIndexed(vm.state) { index, ayah ->
            SurahItem(ayah = ayah, vm = vm, expandedState = vm.expandedStates[index]) {
                vm.playAudio(ayah.number,vm.selectedQarie)
            }
        }
    }
}


@Composable
fun SurahItem(ayah: Ayahs, vm:SurahViewModel, expandedState: MutableState<Boolean>, onClick: () -> Unit) {
    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        Card(
            Modifier.padding(8.dp),
            elevation = CardDefaults.cardElevation(4.dp),
            shape = RoundedCornerShape(16.dp),
            border = BorderStroke(1.dp, softPurple)
        ) {
            Column(Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    AyahIcon(
                        painter = painterResource(id = R.drawable.ayahnum),
                        contentDescription = "AyahNumber",
                        number = ayah.numberInSurah,
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    IconButton(onClick = {
                        onClick()
                    }) {
                        Icon(Icons.Filled.PlayArrow, contentDescription="Play", tint = softPurple)
                    }
                    QuraaList(vm, expandedState)

                }

                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = ayah.text,
                    modifier = Modifier.fillMaxSize(),
                    textAlign = TextAlign.Right,
                    style = TextStyle(color = darkGrey, fontSize = 28.sp, fontWeight = FontWeight.Normal)
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuraaList(vm: SurahViewModel, expandedState: MutableState<Boolean>) {
    ExposedDropdownMenuBox(

        expanded = expandedState.value,
        onExpandedChange ={expandedState.value=! expandedState.value} ) {
        TextField(
            modifier = Modifier.menuAnchor(),
            readOnly = true,
            value=vm.selectedQarie,
            onValueChange = {},
            label = { Text(text = "استمع بصوت القارئ", fontSize = 13.sp, fontWeight = FontWeight.Bold)},
            leadingIcon = { Icon(painter = painterResource(id = R.drawable.listen_ic), contentDescription = "listen icon", modifier = Modifier.size(15.dp))},
            trailingIcon = {ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedState.value)},
            colors = ExposedDropdownMenuDefaults.textFieldColors()

        )
        ExposedDropdownMenu(
            modifier = Modifier.requiredSizeIn(maxHeight = 200.dp), // Set your desired maxHeight
            expanded = expandedState.value,
            onDismissRequest = {expandedState.value=false }) {
           vm.lista.forEach{selectionOption->
                DropdownMenuItem(
                    modifier = Modifier.wrapContentSize(),
                    text = { Text(selectionOption , fontSize = 15.sp, fontWeight = FontWeight.Normal) },
                    onClick = {
                        vm.selectedQarie = selectionOption
                        expandedState.value = false},
                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding

                )
            }
        }
    }

}

@Composable
fun AyahIcon(painter: Painter,contentDescription:String,number:Int) {
    Box(modifier = Modifier.size(50.dp)) {
        Image(
            painter = painter,
            contentDescription = contentDescription,
            modifier = Modifier
                .fillMaxSize()
                .padding(5.dp)
        )
        Text(
            text = number.toString(),
            modifier = Modifier.align(Alignment.Center),
            style = TextStyle(color = Color.Black, fontSize = 15.sp)
        )

    }
}




@Preview(showBackground = true)
@Composable
fun SurahPreview() {
    AyatTheme {
    }
}

