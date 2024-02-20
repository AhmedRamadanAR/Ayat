package com.example.ayat.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
 import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ayat.Doaa
import com.example.ayat.DoaaViewModel
import com.example.ayat.R
import com.example.ayat.ui.theme.AyatTheme
import com.example.ayat.ui.theme.PuropleDark
import com.example.ayat.ui.theme.Purple40


@Composable
fun DoaaScreen(){
    val vm= viewModel<DoaaViewModel>()
    LazyColumn (modifier = Modifier
        .fillMaxSize()
        .padding(4.dp),
        contentPadding = PaddingValues(bottom = 20.dp))
    {
     items(vm.state){doaa->
         DoaaItem(doaa =doaa ){
         vm.toggleFavouriteState(it)
         }

     }
    }
}
@Composable
fun DoaaItem(doaa: Doaa, onClick: (String) -> Unit){
    val icon = if(doaa.isFavouite) {
        Icons.Filled.Favorite
    }
    else{
        Icons.Filled.FavoriteBorder
    }
    Card(

        elevation = CardDefaults.cardElevation(defaultElevation = 20.dp),
        modifier = Modifier.padding(10.dp), border = BorderStroke(1.dp, Purple40)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically){
            DoaaText(modifier = Modifier
                .padding(3.dp)
                .weight(0.80f)
                .padding(4.dp),doaa.doaatext)

            Column(
                modifier = Modifier
                    .weight(0.20f)
                    .height(320.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                DoaaIcon(
                    painter = painterResource(id = R.drawable.praying),
                    "doaaIcon",
                    modifier = Modifier
                        .padding(10.dp)
                        .size(90.dp)
                        .align(Alignment.End)
                )
                Spacer(modifier = Modifier.weight(1f))


                FavouriteIcon(icon,
                    modifier = Modifier
                        .align(Alignment.End)
                        .padding(20.dp)
                        .size(40.dp))
                {onClick(doaa.doaatext)}

            }




        }


    }
}

@Composable
fun FavouriteIcon(
    icon: ImageVector,
    modifier: Modifier,
    onClick :()->Unit) {

    Image( imageVector = icon,
        contentDescription ="Favourite doaa icon",
        modifier = modifier.clickable(
            indication = null,
            interactionSource = remember { MutableInteractionSource() },
            onClick = onClick
        ),
        colorFilter = ColorFilter.tint(Purple40))
}

@Composable
fun DoaaText(
    modifier: Modifier,
    text:String) {

    Text(text = text,
        modifier.fillMaxSize(1f), style = MaterialTheme.typography.headlineSmall,
        textAlign = TextAlign.Right, color = Color.DarkGray

    )
}

@Composable
fun DoaaIcon(
    painter: Painter,
    description:String,
    modifier: Modifier){
    Image(painter = painter,description,modifier,
        colorFilter = ColorFilter.tint(PuropleDark))

}

@Preview(showBackground = true)
@Composable
fun DoaaPreview() {
    AyatTheme {
      //  DoaaItem(doaa = listofDoaa.get(0)){}
    }
}