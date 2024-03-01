import android.annotation.SuppressLint
import android.widget.Toast

import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.ayat.presentation.azan.AzanViewModel
import com.example.ayat.MonthlyPrayerTime
import com.example.ayat.R
import com.example.ayat.ui.theme.softPurple

data class x(val x: String, val b: String)

@SuppressLint("SuspiciousIndentation")
@Composable
fun AzanScreen() {
    val vm: AzanViewModel = viewModel()
    val prayerTime by vm.prayerTime.collectAsState()
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

                val composition by rememberLottieComposition(spec = LottieCompositionSpec.RawRes(R.raw.azan1))
                if (prayerTime.isLoading) CircularProgressIndicator(modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(10.dp))

                Image(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp),
                    contentScale = ContentScale.FillBounds, // Crop the image to fill the screen
                    painter = painterResource(id = R.drawable.test),
                    contentDescription = "", alpha = 0.8f
                )
                if (prayerTime.error!=null) {
                    Toast.makeText(context,"من فضلك يرجى الاتصال بالانترنت",Toast.LENGTH_LONG).show()
                }

            LottieAnimation(
                modifier = Modifier.fillMaxWidth().size(600.dp),
                composition = composition,
                iterations = 1,
                contentScale = ContentScale.Crop
            )
                Column (modifier = Modifier.fillMaxSize()){
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ){
                        Text(prayerTime.monthlyPrayerTime.today +" "+prayerTime.monthlyPrayerTime.dayHijri+" "+ prayerTime.monthlyPrayerTime.monthArabicHijri, fontSize = 20.sp, fontWeight = FontWeight.ExtraBold, color = Color.Black, fontFamily = FontFamily.Monospace)
                        Text(prayerTime.monthlyPrayerTime.dateGregorian,fontSize = 20.sp, fontWeight = FontWeight.Medium, color = Color.Black)

                    }
                    Spacer(modifier = Modifier.padding(20.dp))

                    Row (modifier=Modifier.fillMaxSize(),horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.Top){
                        Text(text = "متبقى على صلاة ${prayerTime.nextPrayerTime}  ${prayerTime.countDownTime} ",fontSize = 20.sp, fontWeight = FontWeight.ExtraBold, color = Color.DarkGray, letterSpacing = 0.10.sp)

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




                        item(prayerTime.monthlyPrayerTime,prayerTime.nextPrayerTime)
                    }
                }

            }


            }















@Composable
    fun item(timings: MonthlyPrayerTime, nextPrayerName: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        PrayerTimeItem(
            icon = painterResource(id = R.drawable.fajr),
            prayerName = "الفجر",
            prayerTime = timings.Fajr,
            isNext = nextPrayerName == "الفجر"

        )
        PrayerTimeItem(
            icon = painterResource(id = R.drawable.sunrise),
            prayerName = "الشروق",
            prayerTime = timings.Sunrise,
            isNext = nextPrayerName == "الشروق"

        )
        PrayerTimeItem(
            icon = painterResource(id = R.drawable.duhricon),
            prayerName = "الظهر",
            prayerTime = timings.Dhuhr,
            isNext = nextPrayerName == "الظهر"

        )
        PrayerTimeItem(
            icon = painterResource(id = R.drawable.asricon),
            prayerName = "العصر",
            prayerTime = timings.Asr,
            isNext = nextPrayerName == "العصر"

        )
        PrayerTimeItem(
            icon = painterResource(id = R.drawable.magreb),
            prayerName = "المغرب",
            prayerTime = timings.Maghrib,
            isNext = nextPrayerName == "المغرب"

        )
        PrayerTimeItem(
            icon = painterResource(id = R.drawable.ishaaicon),
            prayerName = "العشاء",
            prayerTime = timings.Isha,
            isNext = nextPrayerName == "العشاء"

        )


    }

}


@Composable
fun PrayerTimeItem(icon: Painter, prayerName: String, prayerTime: String, isNext: Boolean) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = if (isNext) softPurple else Color.Transparent,
                shape = RoundedCornerShape(100.dp)
            )
            .padding(15.dp)
            ,
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



