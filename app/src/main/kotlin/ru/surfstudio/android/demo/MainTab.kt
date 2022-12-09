/**
 * Copyright © 2022 Surf. All rights reserved.
 */
package ru.surfstudio.android.demo

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.ramcosta.composedestinations.spec.NavGraphSpec
import ru.surfstudio.android.demo.resources.R

enum class MainTab(
    val screen: NavGraphSpec,
    @DrawableRes val iconResId: Int,
    @StringRes val titleResId: Int
) {

    Feed(
        NavGraphs.feed,
        R.drawable.ic_feed_tab,
        R.string.bottom_bar_feed
    ),

    Catalog(
        NavGraphs.catalog,
        R.drawable.ic_catalog_tab,
        R.string.bottom_bar_catalog
    ),

    Basket(
        NavGraphs.basket,
        R.drawable.ic_basket_tab,
        R.string.bottom_bar_basket
    ),

    Shops(
        NavGraphs.shops,
        R.drawable.ic_shops_tab,
        R.string.bottom_bar_shops
    ),

    Profile(
        NavGraphs.profile,
        R.drawable.ic_profile_tab,
        R.string.bottom_bar_profile
    );
}