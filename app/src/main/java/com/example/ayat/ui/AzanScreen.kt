import android.Manifest
import android.content.pm.PackageManager
import android.text.Layout
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.ayat.AzanViewModel
import com.example.ayat.Daum
import com.example.ayat.MonthlyPrayingTime
import com.example.ayat.R
import com.example.ayat.Timings
import com.example.ayat.ui.theme.Purple40
import com.example.ayat.ui.theme.Purple80
import com.example.ayat.ui.theme.darkGrey
import com.example.ayat.ui.theme.softPurple
import kotlinx.coroutines.delay

data class x(val x: String, val b: String)

@Composable
fun AzanScreen() {
    //("صلاة الفجر","صلاة الظهر","صلاة العصر","صلاة المغرب","صلاة العشاء")
    //  val x= listOf<x>(x("صلاة الفجر","11:00"),x("صلاة الظهر","12:00"),x("صلاة الفجر","11:00"),x("صلاة الفجر","11:00"),x("صلاة الفجر","11:00"))
    //   val y = listOf("11:00","12:40","10:40","20:00","31:30")
    val vm: AzanViewModel = viewModel()
    val countdownTime by vm._countdownTime.collectAsState("")
    val context= LocalContext.current

//    val permissions = arrayOf(
//        Manifest.permission.ACCESS_FINE_LOCATION,
//        Manifest.permission.ACCESS_COARSE_LOCATION
//    )
//    val launcherMultiplePermissions = rememberLauncherForActivityResult(
//        ActivityResultContracts.RequestMultiplePermissions()
//    ) { permissionsMap ->
//        val areGranted = permissionsMap.values.reduce { acc, next -> acc && next }
//        if (areGranted) {
//            // All permissions are granted
//            vm.startLocationUpdates()
//        } else {
//            // At least one permission is denied
//            Toast.makeText(context, "Permission Denied", Toast.LENGTH_SHORT).show()
//        }
//    }
//    Button(onClick = {
//        if (permissions.all {
//                ContextCompat.checkSelfPermission(
//                    context,
//                    it
//                ) == PackageManager.PERMISSION_GRANTED
//            }) {
//            // All permissions are already granted, start location updates
//            vm.startLocationUpdates()
//        } else {
//            // At least one permission is not granted, request permissions
//            launcherMultiplePermissions.launch(permissions)
//        }
//    }) {
//        Text("Request Location Permissions")
//    }
//

            Box(modifier = Modifier.fillMaxSize()){

                Image(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp),
                    contentScale = ContentScale.FillBounds, // Crop the image to fill the screen
                    painter = painterResource(id = R.drawable.test),
                    contentDescription = "", alpha = 0.8f
                )
                         val composition by rememberLottieComposition(spec = LottieCompositionSpec.RawRes(R.raw.azan1))
            LottieAnimation(
                modifier = Modifier.fillMaxWidth(),
                composition = composition,
                iterations = 1
            )
                Column (modifier = Modifier.fillMaxSize()){
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ){
                        Text(vm.prayingTime.today +" "+vm.prayingTime.dayHijri+" "+ vm.prayingTime.monthArabicHijri, fontSize = 20.sp, fontWeight = FontWeight.ExtraBold, color = Purple40, fontFamily = FontFamily.Monospace)
                        Text(vm.prayingTime.dateGregorian,fontSize = 20.sp, fontWeight = FontWeight.Medium, color = Purple40)

                    }
                    Spacer(modifier = Modifier.padding(20.dp))


                    Row (modifier=Modifier.fillMaxSize(),horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.Top){
                        Text(text = "متبقى على الصلاة القادمة ${countdownTime}",fontSize = 20.sp, fontWeight = FontWeight.ExtraBold, color = Purple40, letterSpacing = 0.10.sp)

                    }


                }



            }







        Column (modifier= Modifier
            .fillMaxSize()
            .padding(vertical = 120.dp),Arrangement.Center){
            Card(
                shape = RoundedCornerShape(30.dp),
                modifier = Modifier
                    .padding(vertical = 10.dp, horizontal = 10.dp)
                    .fillMaxWidth(),
                elevation = CardDefaults.cardElevation(30.dp)
            ) {




                item(vm.prayingTime)
            }
        }

        }








@Composable
fun item(timings: MonthlyPrayingTime) {

    Column(
        modifier = Modifier.fillMaxWidth().verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        PrayerTimeItem(
            icon = painterResource(id = R.drawable.fajr),
            prayerName = "الفجر",
            prayerTime = timings.Fajr
        )
        PrayerTimeItem(
            icon = painterResource(id = R.drawable.sunrise),
            prayerName = "الشروق",
            prayerTime = timings.Sunrise
        )
        PrayerTimeItem(
            icon = painterResource(id = R.drawable.duhricon),
            prayerName = "الظهر",
            prayerTime = timings.Dhuhr
        )
        PrayerTimeItem(
            icon = painterResource(id = R.drawable.asricon),
            prayerName = "العصر",
            prayerTime = timings.Asr
        )
        PrayerTimeItem(
            icon = painterResource(id = R.drawable.magreb),
            prayerName = "المغرب",
            prayerTime = timings.Maghrib
        )
        PrayerTimeItem(
            icon = painterResource(id = R.drawable.ishaaicon),
            prayerName = "العشاء",
            prayerTime = timings.Isha
        )


    }

}


@Composable
fun PrayerTimeItem(icon: Painter, prayerName: String, prayerTime: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
    ) {
        Icon(painter = icon, contentDescription = prayerName, modifier = Modifier.size(30.dp))
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = prayerName,
            color = Color.DarkGray,
            fontWeight = FontWeight.ExtraBold,
            fontFamily = FontFamily.Default,
            fontSize = 20.sp,
            modifier = Modifier.weight(1f)
        )
        Spacer(modifier = Modifier.width(100.dp))
        Text(
            text = prayerTime,
            color = Color.DarkGray,
            fontWeight = FontWeight.ExtraBold,
            fontFamily = FontFamily.Default,
            fontSize = 20.sp,

        )

    }
}


