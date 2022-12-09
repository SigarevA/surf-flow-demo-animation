/**
 * Copyright © 2022 Surf. All rights reserved.
 */
package ru.surfstudio.android.demo.navhost.app

import ru.surfstudio.android.demo.navhost.navigation.CatalogNavigation
import ru.surfstudio.android.demo.navhost.navigation.FeedNavigation

/** Base app navigation for all modules */
interface AppNavigation : CatalogNavigation, FeedNavigation