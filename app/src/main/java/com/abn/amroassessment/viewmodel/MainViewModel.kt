package com.abn.assessment.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.abn.amroassessment.BuildConfig
import com.abn.amroassessment.database.VenueRoomDatabase
import com.abn.amroassessment.model.venuesearchresponse.Venue
import com.abn.amroassessment.model.venuesearchresponse.VenueSearchResponse
import com.abn.amroassessment.repository.MainFragmentRepositoryImpl
import com.abn.amroassessment.utils.Constants
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel : BaseViewModel() {

    private val mVenueSearchResponse: MutableLiveData<MutableList<Venue>> = MutableLiveData()
    val mVenueSearchResponseLiveData: LiveData<MutableList<Venue>> by lazy { mVenueSearchResponse }

    private val mainFragmentRepository = MainFragmentRepositoryImpl()

    fun getVenueList(db: VenueRoomDatabase, networkAvailable: Boolean) {
        val handler = CoroutineExceptionHandler { _, exception ->

        }

        if (networkAvailable) {
            getCoroutineScope().launch(handler) {
                val result = withContext(coroutineContext) {
                    mainFragmentRepository.getVenueList(getRequestParamForVenueList())
                }
                if (result.isSuccessful) {
                    val venueSearchResponse: VenueSearchResponse? = result.body()
                    mVenueSearchResponse.value = venueSearchResponse?.response?.venues
                }
            }
        } else {
            getCoroutineScope().launch(handler) {
                val result = withContext(coroutineContext) {
                    try {
                        db.venueDao().getVenues()
                    } catch (e: Exception) {
                        Log.v("errorDB", e.message ?: "unknown error")
                    }
                }

                mVenueSearchResponse.value = result as MutableList<Venue>

            }

        }
    }

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

    fun storeVenueListInDB(db: VenueRoomDatabase, venueList: MutableList<Venue>?) {
        val handler = CoroutineExceptionHandler { _, exception ->

        }

        getCoroutineScope().launch(handler) {
            withContext(coroutineContext) {
                try {
                    db.venueDao().deleteVenues()
                    venueList?.let { db.venueDao().insertAll(it) }
                } catch (e: Exception) {
                    Log.v("errorDB", e.message ?: "unknown error")
                }
            }

        }
    }


}