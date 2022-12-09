/**
 * Copyright © 2022 Surf. All rights reserved.
 */
package ru.surfstudio.android.demo.core

import javax.inject.Qualifier

/**
 * Dispatcher annotation for MVI screen
 */
@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class ScreenDispatcher

/**
 * Load on start annotation for MVI screen
 */
@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class ScreenLoadOnStart