/**
 * Copyright © 2022 Surf. All rights reserved.
 */
package ru.surfstudio.android.demo.test_utils

import android.annotation.SuppressLint
import android.net.Uri
import androidx.annotation.CallSuper
import androidx.room.Room
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import ru.surfstudio.android.demo.core.util.SecurityUtil
import ru.surfstudio.android.demo.network.room.database.AppEncryptedDatabase
import ru.surfstudio.android.demo.network.session.SessionChangedInteractor
import java.io.IOException

/** Base class for app unit tests */
@OptIn(ExperimentalCoroutinesApi::class)
abstract class BaseUnitTest {

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    protected val testDispatcher = Dispatchers.Main

    private lateinit var db: AppEncryptedDatabase
    protected lateinit var sessionChangedInteractor: SessionChangedInteractor

    private val testEncrypted = "testEncrypted"
    private val testDecrypted = "testDecrypted"
    private val dispatchTimeoutMs = 1000L

    @SuppressLint("VisibleForTests")
    @Before
    @CallSuper
    open fun setUp() {
        db = Room.inMemoryDatabaseBuilder(mockk(), AppEncryptedDatabase::class.java).build()
        mockkConstructor(SessionChangedInteractor::class)
        every { anyConstructed<SessionChangedInteractor>().insertTokens(any()) } returns Unit
        every { anyConstructed<SessionChangedInteractor>().updateTokens(any()) } returns Unit
        every { anyConstructed<SessionChangedInteractor>().clearTokens() } returns Unit
        sessionChangedInteractor = SessionChangedInteractor(db, testDispatcher)

        mockkStatic(Uri::class)
        every { Uri.decode(any()) } returns ""

        mockkObject(SecurityUtil)
        every { SecurityUtil.encrypt(any(), any()) } returns testEncrypted
        every { SecurityUtil.decrypt(any(), any()) } returns testDecrypted
    }

    @After
    @CallSuper
    @Throws(IOException::class)
    open fun tearDown() {
        db.close()
    }

    /** `runTest` util with defined `dispatchTimeoutMs` for safety */
    protected fun runTimeoutTest(block: suspend () -> Unit) {
        runTest(dispatchTimeoutMs = dispatchTimeoutMs) {
            block()
        }
    }
}