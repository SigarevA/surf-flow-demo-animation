/**
 * Copyright © 2022 Surf. All rights reserved.
 */
package ru.surfstudio.android.demo.feed.navigation

import ru.surfstudio.android.demo.feed.feed.FeedNavigationType

interface FeedScreenNavigation {

    fun navigate(type: FeedNavigationType)
}