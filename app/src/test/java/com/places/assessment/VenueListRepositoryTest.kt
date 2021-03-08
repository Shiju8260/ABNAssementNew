package com.places.assessment

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.places.assessment.repository.VenueListRepositoryImpl
import com.places.assessment.utils.Constants
import com.places.assessment.viewmodel.MainViewModel
import com.staralliance.networkframework.NetworkManager
import junit.framework.Assert.assertNotNull
import junit.framework.Assert.assertTrue
import kotlinx.coroutines.*
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import java.util.concurrent.CountDownLatch
import kotlin.coroutines.CoroutineContext

@ExperimentalCoroutinesApi
class VenueListRepositoryTest {
    @get:Rule
    val rule: TestRule = InstantTaskExecutorRule()

    private val testDispatcher = TestCoroutineDispatcher()

    private val coroutineContext: CoroutineContext get() = testDispatcher
    private val coroutinescope: CoroutineScope = CoroutineScope(coroutineContext)

    private lateinit var repository: VenueListRepositoryImpl
    private lateinit var viewModel: MainViewModel

    @Before
    fun setup() {
        NetworkManager.setBaseUrl(BuildConfig.BASE_URL)
        NetworkManager.setConnectionTimeOut(2)
        repository = VenueListRepositoryImpl()
        viewModel = MainViewModel()
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
                repository.getVenueList(getRequestParamForVenueList())
            }
            latch.countDown()
        }

        latch.await()
        assertNotNull(result)
        assertTrue(result.toString().isNotEmpty())
    }

    //Request params for venue list
    private fun getRequestParamForVenueList(): Map<String, String> {
        val query: HashMap<String, String> = HashMap()
        query[Constants.PARAM_CLIENTID] = BuildConfig.CLIENT_ID
        query[Constants.PARAM_CLIENTSECRET] = BuildConfig.CLIENT_SECRET
        query[Constants.PARAM_INTENT] = "browse"
        query[Constants.PARAM_NEAR] = "Rotterdam"
        query[Constants.PARAM_V] = "20210302"
        query[Constants.PARAM_RADIUS] = "10000"
        query[Constants.PARAM_LIMIT] = "10"
        return query
    }
}