/**
 * Copyright © 2022 Surf. All rights reserved.
 */
package ru.surfstudio.android.demo

import com.ramcosta.composedestinations.dynamic.routedIn
import com.ramcosta.composedestinations.spec.DestinationSpec
import com.ramcosta.composedestinations.spec.NavGraphSpec
import com.ramcosta.composedestinations.spec.Route
import ru.surfstudio.android.demo.basket.destinations.BasketScreenDestination
import ru.surfstudio.android.demo.catalog.destinations.*
import ru.surfstudio.android.demo.feed.destinations.FeedScreenDestination
import ru.surfstudio.android.demo.feed.destinations.ResultScreenDestination
import ru.surfstudio.android.demo.feed.destinations.StackScreenDestination
import ru.surfstudio.android.demo.profile.destinations.ProfileScreenDestination
import ru.surfstudio.android.demo.shops.destinations.ShopsScreenDestination

/** Main navigation for tabs and nested graphs */
object NavGraphs {

    val feed = object : NavGraphSpec {
        override val route: String = "feed"
        override val startRoute: Route = FeedScreenDestination routedIn this
        override val destinationsByRoute: Map<String, DestinationSpec<*>> =
            listOf(
                FeedScreenDestination,
                CategoryScreenDestination,
                StackScreenDestination,
                ResultScreenDestination,
                OpenResultRecipientScreenDestination,
                ResultBusScreenDestination,
                BottomSheetScreenDestination
            )
                .routedIn(this)
                .associateBy { it.route }
    }

    val catalog = object : NavGraphSpec {
        override val route: String = "catalog"
        override val startRoute: Route = CatalogScreenDestination routedIn this
        override val destinationsByRoute: Map<String, DestinationSpec<*>> =
            listOf(
                CatalogScreenDestination,
                FullCategoryScreenDestination,
                ProductsCatalogScreenDestination,
                ProductDetailsScreenDestination
            )
                .routedIn(this)
                .associateBy { it.route }
    }

    val basket = object : NavGraphSpec {
        override val route: String = "basket"
        override val startRoute: Route = BasketScreenDestination routedIn this
        override val destinationsByRoute: Map<String, DestinationSpec<*>> =
            listOf(
                BasketScreenDestination
            )
                .routedIn(this)
                .associateBy { it.route }
    }

    val shops = object : NavGraphSpec {
        override val route: String = "shops"
        override val startRoute: Route = ShopsScreenDestination routedIn this
        override val destinationsByRoute: Map<String, DestinationSpec<*>> =
            listOf(
                ShopsScreenDestination
            )
                .routedIn(this)
                .associateBy { it.route }
    }

    val profile = object : NavGraphSpec {
        override val route: String = "profile"
        override val startRoute: Route = ProfileScreenDestination routedIn this
        override val destinationsByRoute: Map<String, DestinationSpec<*>> =
            listOf(
                ProfileScreenDestination
            )
                .routedIn(this)
                .associateBy { it.route }
    }

    val root = object : NavGraphSpec {
        override val route = "root"
        override val startRoute = feed
        override val destinationsByRoute = emptyMap<String, DestinationSpec<*>>()
        override val nestedNavGraphs = listOf(
            feed,
            catalog,
            basket,
            shops,
            profile
        )
    }
}