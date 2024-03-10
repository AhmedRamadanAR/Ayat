package com.example.ayat.presentation.quran
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ayat.data.localdata.References
import com.example.ayat.ui.theme.darkGrey
import com.example.ayat.ui.theme.softPurple


@Composable
fun SurahListScreen(
    state: List<References>,
    onItemClick: (Int) -> Unit
) {

    LazyColumn(Modifier.fillMaxSize().padding(bottom = 60.dp)){
        item {
        }
        items(state){
            SurahItem(surah = it){ id->
                onItemClick(id)
            }
        }
    }
}

@Composable
fun SurahItem(surah: References, onItemClick:(Int)->Unit) {

    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        Card(
            Modifier
                .padding(8.dp)
                .clickable { onItemClick(surah.number) },
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
                    text = surah.name,
                    textAlign = TextAlign.Right,
                    style = TextStyle(
                        color = darkGrey,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Normal
                    )
                )


            }
            Row(modifier=Modifier.fillMaxSize(), horizontalArrangement = Arrangement.End){
                Text(text = " ترتيب السورة : "+"${surah.number}", fontWeight = FontWeight.Normal,modifier=Modifier.weight(0.5f),textAlign = TextAlign.Start)

                Text(text = "عدد اياتها  : "+"${surah.numberOfAyahs}", fontWeight = FontWeight.Normal,modifier=Modifier.weight(0.5f),textAlign = TextAlign.Center)
                Spacer(modifier = Modifier.padding(horizontal = 10.dp))

                Text(text = surah.revelationType, fontWeight = FontWeight.Normal,modifier=Modifier.weight(0.4f),textAlign = TextAlign.Center)
                Spacer(modifier = Modifier.padding(horizontal = 3.dp))

            }
        }

    }
}