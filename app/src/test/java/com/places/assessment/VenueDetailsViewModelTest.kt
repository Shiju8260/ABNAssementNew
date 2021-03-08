package com.places.assessment

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.places.assessment.database.VenueRoomDatabase
import com.places.assessment.viewmodel.VenueDetailsViewModel
import com.staralliance.networkframework.NetworkManager
import junit.framework.Assert.assertNotNull
import junit.framework.Assert.assertTrue
import junit.framework.TestCase
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
class VenueDetailsViewModelTest {
    @get:Rule
    val rule: TestRule = InstantTaskExecutorRule()

    private val testDispatcher = TestCoroutineDispatcher()

    private val coroutineContext: CoroutineContext get() = testDispatcher
    private val coroutinescope: CoroutineScope = CoroutineScope(coroutineContext)

    private lateinit var viewModel: VenueDetailsViewModel

    private lateinit var venueRoomDatabase: VenueRoomDatabase

    @Before
    fun setup() {
        NetworkManager.setBaseUrl(BuildConfig.BASE_URL)
        NetworkManager.setConnectionTimeOut(2)
        viewModel = VenueDetailsViewModel()
        Dispatchers.setMain(testDispatcher)
        viewModel.setCoroutineScope(coroutinescope)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }

    @Test
    fun `should pass when venue details api response is not null`() {
        val result = viewModel.getVenueDetails("4ec376860aafcf9a808cd4d1")
        assertNotNull(result)
        assertTrue(result.toString().isNotEmpty())
    }

    @Test
    fun `should pass when venue details api response params are not empty`() {
        val result = viewModel.getRequestParamForVenueDetails()
        TestCase.assertTrue(result.isNotEmpty())
    }
}