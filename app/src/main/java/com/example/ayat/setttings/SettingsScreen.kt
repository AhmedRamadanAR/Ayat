package com.example.ayat.setttings

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.startActivity
import com.example.ayat.R

@Composable
fun SettingsScreen(vm: SettingsViewModel) {
    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        LaunchedEffect(vm.notificationEnabled) {
            vm.switchState = vm.notificationEnabled
        }
        val context = LocalContext.current
        Column(modifier = Modifier.fillMaxSize()) {

            Row (modifier = Modifier
                .padding(horizontal = 20.dp)
                .fillMaxWidth()
                .padding(top = 40.dp), horizontalArrangement = Arrangement.SpaceBetween){
                Text(style = TextStyle(fontWeight = FontWeight.Normal, fontSize = 20.sp), text = stringResource(id = R.string.enable_azan), modifier = Modifier.align(Alignment.CenterVertically))
                Switch(
                    modifier = Modifier,
                    checked = vm.switchState,
                    onCheckedChange = { isChecked ->
                        vm.changeSwitchState()
                        vm.updateNotificationState()

                        if (isChecked && !vm.notificationEnabled) {
                            openNotificationPermission(context)
                            vm.updateNotificationState()
                        }
                    }

                )

            }
            HorizontalDivider(modifier = Modifier.padding(top = 20.dp), color = Color.Gray, thickness = 1.dp)
        }





    }
}

fun openNotificationPermission(context:Context) {

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        val intent = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS)
        intent.putExtra(Settings.EXTRA_APP_PACKAGE, "com.example.ayat")
        startActivity(context, intent, null)
    } else {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri = Uri.fromParts("package", "com.example.ayat", null)
        intent.data = uri
        startActivity(context, intent, null)
    }
}
