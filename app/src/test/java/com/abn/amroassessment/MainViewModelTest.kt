package com.abn.amroassessment

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import com.abn.amroassessment.database.VenueRoomDatabase
import com.abn.assessment.viewmodel.MainViewModel
import com.staralliance.networkframework.NetworkManager
import junit.framework.Assert.assertNotNull
import junit.framework.Assert.assertTrue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import kotlin.coroutines.CoroutineContext

@ExperimentalCoroutinesApi
class MainViewModelTest {

    @get:Rule
    val rule: TestRule = InstantTaskExecutorRule()

    private val testDispatcher = TestCoroutineDispatcher()

    private val coroutineContext: CoroutineContext get() = testDispatcher
    private val coroutinescope: CoroutineScope = CoroutineScope(coroutineContext)

    private lateinit var viewModel: MainViewModel

    private lateinit var venueRoomDatabase: VenueRoomDatabase

    @Before
    fun setup() {
        NetworkManager.setBaseUrl(BuildConfig.BASE_URL)
        NetworkManager.setConnectionTimeOut(2)
        viewModel = MainViewModel()
        Dispatchers.setMain(testDispatcher)
        viewModel.setCoroutineScope(coroutinescope)
    }

    @Before
    fun CreateDb(){
        val context= ApplicationProvider.getApplicationContext<Context>()
        venueRoomDatabase = VenueRoomDatabase.getDatabase(context)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }

    @Test
    fun `should pass when venue list api response is not null`() {
        val result = viewModel.getVenueList(venueRoomDatabase.venueDao(), true)
        assertNotNull(result)
        assertTrue(result.toString().isNotEmpty())
    }
}