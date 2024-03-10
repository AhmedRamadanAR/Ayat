package com.example.ayat.presentation.azkar

import android.util.Log

import androidx.compose.foundation.layout.PaddingValues

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items

import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.ayat.data.localdata.MorningEveningAzkar
import com.example.ayat.presentation.doaa.DoaaText
import com.example.ayat.R
import com.example.ayat.ui.theme.Purple40


@Composable
fun MorningEveningScreen(List:List<MorningEveningAzkar>) {

    LazyColumn(Modifier.fillMaxSize()) {
        items(List) {item->

            Item(item)
        }
    }

}

@Composable
fun Item(item: MorningEveningAzkar) {
    val count = remember { mutableStateOf(item.count) }

    Card(
        elevation = CardDefaults.cardElevation(
            defaultElevation = 10.dp,
            pressedElevation = 10.dp
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            CountdownButton(modifier= Modifier
                .size(50.dp)
                .align(Alignment.CenterVertically),item=item,count=count)
            DoaaText(
                modifier = Modifier.padding(horizontal = 10.dp)
                    .align(Alignment.Top),
                text = item.content
            )

        }
    }
}

@Composable
fun CountdownButton(modifier: Modifier, item: MorningEveningAzkar, count: MutableState<Int>) {


Button(
        modifier = modifier,
        onClick = {
            if (count.value > 0) {
                Log.d("dec", "CountdownButton:${item.count} ")
                count.value--
            }
            if (count.value== 0) {
                item.showAnimation = true
            }
        },
        colors = ButtonDefaults.buttonColors(Color.Transparent),
        contentPadding = PaddingValues(0.dp)
    ) {
        if (!item.showAnimation) {
            Text(text = "${count.value}", fontSize = 20.sp,
                modifier = modifier
                    .wrapContentSize()
                    .align(Alignment.CenterVertically),
                style = MaterialTheme.typography.titleSmall,
                color = Purple40
            )
        } else {
            val composition by rememberLottieComposition(spec = LottieCompositionSpec.RawRes(R.raw.rightcheck))
            LottieAnimation(
                modifier = modifier.fillMaxSize(),
                composition = composition,
                iterations = 1
            )
        }
    }
}

