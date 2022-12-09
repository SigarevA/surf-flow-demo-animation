/**
 * Copyright © 2022 Surf. All rights reserved.
 */
package ru.surfstudio.android.demo.navhost.navigation

import com.ramcosta.composedestinations.dynamic.within
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.spec.NavGraphSpec
import ru.surfstudio.android.demo.catalog.destinations.BottomSheetScreenDestination
import ru.surfstudio.android.demo.catalog.destinations.CategoryScreenDestination
import ru.surfstudio.android.demo.catalog.destinations.OpenResultRecipientScreenDestination
import ru.surfstudio.android.demo.catalog.destinations.ResultBusScreenDestination
import ru.surfstudio.android.demo.feed.StackNavigationType
import ru.surfstudio.android.demo.feed.destinations.FeedScreenDestination
import ru.surfstudio.android.demo.feed.destinations.ResultScreenDestination
import ru.surfstudio.android.demo.feed.destinations.StackScreenDestination
import ru.surfstudio.android.demo.feed.feed.FeedNavigationType
import ru.surfstudio.android.demo.feed.navigation.FeedScreenNavigation
import ru.surfstudio.android.demo.feed.navigation.StackScreenNavigation
import kotlin.random.Random

/** Base navigation for `feed` module */
interface FeedNavigation : StackScreenNavigation, FeedScreenNavigation {
    val navGraph: NavGraphSpec
    val destinationsNavigator: DestinationsNavigator

    override fun navigate(type: StackNavigationType) {
        when (type) {
            StackNavigationType.ClearStack -> {
                destinationsNavigator.popBackStack(
                    route = (FeedScreenDestination within navGraph).route,
                    inclusive = false
                )
            }
            StackNavigationType.OpenNext -> {
                val number = Random.nextInt()
                destinationsNavigator.navigate(
                    direction = StackScreenDestination(title = "Stack $number") within navGraph,
                )
            }
        }
    }

    override fun navigate(type: FeedNavigationType) {
        val destination = when (type) {
            FeedNavigationType.OpenCategory -> CategoryScreenDestination(name = "Simple category")
            FeedNavigationType.OpenForResultSimpleRecipient -> ResultScreenDestination
            FeedNavigationType.OpenForResultOpenRecipient -> OpenResultRecipientScreenDestination
            FeedNavigationType.OpenNext -> StackScreenDestination(title = "Start stack")
            is FeedNavigationType.OpenForResultBus -> ResultBusScreenDestination(parentId = type.parentId)
            is FeedNavigationType.OpenBottomSheet -> BottomSheetScreenDestination(parentId = type.parentId)
        }
        destinationsNavigator.navigate(
            direction = destination within navGraph,
        )
    }
}