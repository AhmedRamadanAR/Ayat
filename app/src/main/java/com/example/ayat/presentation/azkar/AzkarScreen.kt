package com.example.ayat.presentation.azkar

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Note
import androidx.compose.material.icons.automirrored.outlined.Note
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Nightlight
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material.icons.outlined.Nightlight
import androidx.compose.material.icons.outlined.WbSunny
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.example.ayat.data.localdata.MyZekr
import com.example.ayat.R
import com.example.ayat.presentation.doaa.DoaaIcon
import com.example.ayat.presentation.doaa.DoaaText
import com.example.ayat.ui.theme.Purple40
import com.example.ayat.ui.theme.softPurple
import kotlinx.coroutines.launch

sealed class IconReusage {
    data class VectorIcon(val imageVector: ImageVector) : IconReusage()
    data class PainterIcon(val painter: Painter) : IconReusage()
}

data class TabItem(val title: String, val selectedIconReusage: IconReusage, val unselectedIconReusage: IconReusage)


@SuppressLint("SuspiciousIndentation")
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AzkarScreen(vm: MorningEveningAzkarViewModel,viewmodelME: AzkarViewModel) {
    val tabItem = listOf(
        TabItem(
            "أذكار الصباح",
            unselectedIconReusage = IconReusage.VectorIcon(Icons.Outlined.WbSunny),
            selectedIconReusage = IconReusage.VectorIcon(Icons.Filled.WbSunny)
        ),
        TabItem(
            "أذكار المساء",
            unselectedIconReusage = IconReusage.VectorIcon(Icons.Outlined.Nightlight),
            selectedIconReusage = IconReusage.VectorIcon(Icons.Filled.Nightlight)
        ),
        TabItem(
            "أذكارى",
            unselectedIconReusage = IconReusage.VectorIcon(Icons.AutoMirrored.Outlined.Note),
            selectedIconReusage = IconReusage.VectorIcon(Icons.AutoMirrored.Filled.Note)
        ),
//        TabItem(
//            "دعاء",
//            unselectedIcon = Icon.PainterIcon(painterResource(R.drawable.doaaoutline)),
//            selectedIcon = Icon.PainterIcon(painterResource(R.drawable.doaafill))
//        )
    )

    val pagerState = rememberPagerState {
        tabItem.size
    }
    val selectedTabIndex = remember {
        derivedStateOf { pagerState.currentPage }
    }

    val coroutineScope = rememberCoroutineScope()
    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {

        Column(modifier = Modifier.fillMaxSize()) {
            TabRow(
                selectedTabIndex = selectedTabIndex.value,

                modifier =
                Modifier.fillMaxWidth()
            ) {
                tabItem.forEachIndexed { index, item ->
                    Tab(
                        selected = index == selectedTabIndex.value,
                        selectedContentColor = Purple40,
                        unselectedContentColor = MaterialTheme.colorScheme.outline,
                        onClick = {
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(index)
                            }
                        },
                        text = {
                            Text(text = item.title)
                        },
                        icon = {
                            val icon =
                                if (index == selectedTabIndex.value) item.selectedIconReusage else item.unselectedIconReusage
                            when (icon) {
                                is IconReusage.VectorIcon -> Icon(
                                    imageVector = icon.imageVector,
                                    contentDescription = item.title,
                                    modifier = Modifier.size(25.dp),
                                )

                                is IconReusage.PainterIcon -> Image(
                                    painter = icon.painter,
                                    contentDescription = item.title,
                                    modifier = Modifier.size(25.dp)
                                )
                            }
                        }
                    )
                }
            }

            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) { index ->
                Column(
                    Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    when (index) {
                        0 -> MorningEveningScreen(List = vm.monringList)
                        1 -> MorningEveningScreen(List = vm.eveningList)
                        2 -> ItemsScreen(viewmodelME)
                        else -> Text(text = "hello nothing")
                    }
                }
            }
        }
    }
}

@Composable
fun ItemsScreen(
    vm: AzkarViewModel
) {

    val zekrList by vm.zekrList.collectAsState()
    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            Modifier
                .fillMaxSize()
                .padding(bottom = 60.dp)) {

            items(zekrList) { item ->
                Item(item = item, vm = vm)
            }

        }
        FloatingActionButton(
            onClick = { vm.showAddDialog = true },
            Modifier
                .align(Alignment.BottomEnd)
                .padding(horizontal = 10.dp, vertical = 100.dp), containerColor = softPurple
        ) {
            Icon(Icons.Filled.Add, "add_icon")
        }
    }
    if (vm.showAddDialog) {
        val textState = remember { mutableStateOf("") }

        ReusableDialog(
            titleText = "إضافة جديدة",
            confirmText = "تأكيد",
            dismissText = "إلغاء",
            onConfirm = { vm.addZekr(textState.value) },
            onDismiss = { vm.showAddDialog = false },
            textFieldLabel = "ذكر/دعاء جديد",
            textFieldValue = textState
        )

    }

    if (vm.showUpdateDialog) {
        val liveText = remember { mutableStateOf(vm.selectedItem.zekr) }

        ReusableDialog(
            titleText = "تعديل",
            confirmText = "تأكيد",
            dismissText = "إلغاء",
            onConfirm = {
                vm.updateZekr(vm.selectedItem.zekr, liveText.value)
                liveText.value = ""
            },
            onDismiss = { vm.showUpdateDialog = false },
            textFieldLabel = "",
            textFieldValue = liveText
        )

    }

}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Item(item: MyZekr, vm: AzkarViewModel) {
    Card(

        shape = RoundedCornerShape(topEnd = 15.dp, bottomEnd = 15.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 10.dp,
            pressedElevation = 40.dp
        ),
        modifier = Modifier
            .combinedClickable(onLongClick = {
                vm.selectedItem = item
                vm.showDeleteDialog = true


            }, onClick = {
                vm.selectedItem = item
                vm.showUpdateDialog = true


            })


            .wrapContentSize()
            .padding(10.dp),
        border = BorderStroke(1.dp, softPurple)
    ) {
        Row(verticalAlignment = Alignment.Top) {
            DoaaText(
                modifier = Modifier

                    .weight(1f)
                    .padding(20.dp),
                item.zekr
            )

            DoaaIcon(
                painter = painterResource(id = R.drawable.praying),
                "doaaIcon",
                modifier = Modifier
                    .padding(15.dp)
                    .size(40.dp)
                    .align(Alignment.Bottom)
            )


            if (vm.showDeleteDialog) {
                ReusableDialog(
                    titleText = "هل تريد حذف هذا ؟",
                    confirmText = "نعم",
                    dismissText = "لا",
                    onConfirm = {
                        vm.deleteZekr(vm.selectedItem.zekr)

                    },
                    onDismiss = { vm.showDeleteDialog = false })
            }


        }
    }
}




