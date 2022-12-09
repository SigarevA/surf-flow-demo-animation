/**
 * Copyright © 2022 Surf. All rights reserved.
 */
package ru.surfstudio.android.demo.navhost.navigation

import com.ramcosta.composedestinations.dynamic.within
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.spec.NavGraphSpec
import ru.surfstudio.android.demo.catalog.CatalogNavigationType
import ru.surfstudio.android.demo.catalog.destinations.FullCategoryScreenDestination
import ru.surfstudio.android.demo.catalog.destinations.ProductDetailsScreenDestination
import ru.surfstudio.android.demo.catalog.destinations.ProductsCatalogScreenDestination
import ru.surfstudio.android.demo.catalog.navigation.CatalogScreenNavigation
import ru.surfstudio.android.demo.catalog.navigation.ProductsCatalogScreenNavigation
import ru.surfstudio.android.demo.catalog.products.ProductsCatalogNavigationType

/** Base navigation for `catalog` module */
interface CatalogNavigation :
    CatalogScreenNavigation,
    ProductsCatalogScreenNavigation {

    val navGraph: NavGraphSpec
    val destinationsNavigator: DestinationsNavigator

    override fun navigate(type: CatalogNavigationType) {
        val destination = when (type) {
            CatalogNavigationType.OpenCategory -> FullCategoryScreenDestination(name = "Full screen category")
            CatalogNavigationType.OpenDogs -> ProductsCatalogScreenDestination(categoryId = "98")
            CatalogNavigationType.OpenCats -> ProductsCatalogScreenDestination(categoryId = "96")
        }
        destinationsNavigator.navigate(
            direction = destination within navGraph,
        )
    }

    override fun navigate(type: ProductsCatalogNavigationType) {
        when (type) {
            is ProductsCatalogNavigationType.OpenProductDetails -> {
                destinationsNavigator.navigate(
                    direction = ProductDetailsScreenDestination(type.productId) within navGraph,
                )
            }
        }
    }
}