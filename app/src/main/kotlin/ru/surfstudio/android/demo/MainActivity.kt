/**
 * Copyright © 2022 Surf. All rights reserved.
 */
package ru.surfstudio.android.demo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import dagger.hilt.android.AndroidEntryPoint
import ru.surfstudio.android.demo.components.compose.overload.buttons.SubscribeButton
import ru.surfstudio.android.demo.components.compose.overload.navigation.AnimatedBottomNavigation
import ru.surfstudio.android.demo.components.compose.overload.navigation.AnimatedNavigationBarItem
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
            Box(modifier = Modifier.fillMaxSize()) {

                SubscribeButton(
                    text = "SUBSCRIBE",
                    onClick = { /* TODO("Don't implementation") */}
                )

                AnimatedBottomNavigation(
                    selectedTab.ordinal,
                    Tabs.values().size,
                    backgroundColor = Color(0xFFFA6364),
                    modifier = Modifier.align(Alignment.BottomCenter)
                ) {
                    Tabs.values().forEach { tab ->
                        AnimatedNavigationBarItem(
                            selected = selectedTab == tab,
                            onClick = { selectedTab = tab },
                            iconResId = tab.iconResId,
                            selectedContentColor = Color.White,
                            unselectedContentColor = Color(0xFFFFB2B2),
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


@Preview(
    showBackground = true
)
@Composable
fun PreviewActivity() {
    var selectedTab by remember { mutableStateOf(Tabs.Smile) }
    Box {
        AnimatedBottomNavigation(
            selectedTab.ordinal,
            Tabs.values().size,
            modifier = Modifier.align(Alignment.BottomCenter)
        ) {
            Tabs.values().forEach { tab ->
                AnimatedNavigationBarItem(
                    selected = selectedTab == tab,
                    onClick = { selectedTab = tab },
                    iconResId = tab.iconResId,
                )
            }
        }
    }
}

enum class Tabs(val iconResId: Int) {
    Smile(ru.surfstudio.android.demo.resources.R.drawable.ic_profile),
    Star(ru.surfstudio.android.demo.resources.R.drawable.ic_star),
    Graph(ru.surfstudio.android.demo.resources.R.drawable.ic_dashboard),
    Menu(ru.surfstudio.android.demo.resources.R.drawable.ic_menu),
}