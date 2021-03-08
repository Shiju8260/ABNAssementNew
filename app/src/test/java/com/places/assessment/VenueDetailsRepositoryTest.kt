package com.places.assessment

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.places.assessment.repository.VenueDetailsRepositoryImpl
import com.places.assessment.viewmodel.VenueDetailsViewModel
import com.staralliance.networkframework.NetworkManager
import junit.framework.Assert.assertNotNull
import junit.framework.Assert.assertTrue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import kotlinx.coroutines.withContext
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import java.util.concurrent.CountDownLatch
import kotlin.coroutines.CoroutineContext

class VenueDetailsRepositoryTest {
    @get:Rule
    val rule: TestRule = InstantTaskExecutorRule()

    private val testDispatcher = TestCoroutineDispatcher()

    private val coroutineContext: CoroutineContext get() = testDispatcher
    private val coroutinescope: CoroutineScope = CoroutineScope(coroutineContext)

    private lateinit var repository: VenueDetailsRepositoryImpl
    private lateinit var viewModel: VenueDetailsViewModel

    @Before
    fun setup() {
        NetworkManager.setBaseUrl(BuildConfig.BASE_URL)
        NetworkManager.setConnectionTimeOut(2)
        repository = VenueDetailsRepositoryImpl()
        viewModel = VenueDetailsViewModel()
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }


    @Test
    fun `should pass when venue list api response is not null`() {
        val latch = CountDownLatch(1)
        var result: Any? = null
        coroutinescope.launch {
            result = withContext(coroutineContext) {
                repository.getVenueDetails(
                    "4ec376860aafcf9a808cd4d1",
                    viewModel.getRequestParamForVenueDetails()
                )
            }
            latch.countDown()
        }

        latch.await()
        assertNotNull(result)
        assertTrue(result.toString().isNotEmpty())
    }


}