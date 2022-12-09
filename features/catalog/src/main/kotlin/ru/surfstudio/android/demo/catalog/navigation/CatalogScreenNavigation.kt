/**
 * Copyright © 2022 Surf. All rights reserved.
 */
package ru.surfstudio.android.demo.catalog.navigation

import ru.surfstudio.android.demo.catalog.CatalogNavigationType

interface CatalogScreenNavigation {

    fun navigate(type: CatalogNavigationType)
}