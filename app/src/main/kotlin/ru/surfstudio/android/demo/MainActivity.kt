/**
 * Copyright © 2022 Surf. All rights reserved.
 */
package ru.surfstudio.android.demo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material.BottomNavigation
import androidx.compose.material.Text
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import dagger.hilt.android.AndroidEntryPoint
import ru.surfstudio.android.demo.components.compose.overload.navigation.AnimatedBottomNavigation
import ru.surfstudio.android.demo.components.compose.overload.navigation.AnimatedNavigationBarItem
import ru.surfstudio.android.demo.components.compose.overload.navigation.NavigationBarWithHill
import ru.surfstudio.android.demo.components.compose.overload.navigation.Tabs
import ru.surfstudio.android.demo.core.snackbar.IconMessageController
import javax.inject.Inject

@ExperimentalAnimationApi
@ExperimentalMaterialNavigationApi
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var messageController: IconMessageController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            var selectedTab by remember { mutableStateOf(Tabs.Smile) }
            Box {
                Text("Where?")
//            SurfMviDemoTheme {
                NavigationBarWithHill()

                AnimatedBottomNavigation(
                    selectedTab.ordinal,
                    Tabs.values().size,
                    modifier = Modifier.align(Alignment.BottomCenter)
                ) {
                    Tabs.values().forEach { tab ->
                        AnimatedNavigationBarItem(
                            selected = selectedTab == tab ,
                            onClick = { selectedTab = tab },
                            iconResId = tab.iconResId,
                        )
                    }
                }

//                MainScreen { message, succeed ->
//                    if (succeed) {
//                        messageController.show(message)
//                    } else {
//                        messageController.showErrorMessage(message)
//                    }
//                }
//            }
            }
        }
    }
}