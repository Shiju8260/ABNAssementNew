package com.abn.assessment.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.abn.amroassessment.BuildConfig
import com.abn.amroassessment.database.VenueRoomDatabase
import com.abn.amroassessment.model.venuesearchresponse.Venue
import com.abn.amroassessment.model.venuesearchresponse.VenueSearchResponse
import com.abn.amroassessment.repository.VenueListRepositoryImpl
import com.abn.amroassessment.utils.Constants
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel : BaseViewModel() {

    private val mVenueSearchResponse: MutableLiveData<MutableList<Venue>> = MutableLiveData()
    val mVenueSearchResponseLiveData: LiveData<MutableList<Venue>> by lazy { mVenueSearchResponse }

    private val mDataErrorMessage: MutableLiveData<String> = MutableLiveData()
    val mDataErrorMessageLiveData: LiveData<String> by lazy { mDataErrorMessage }


    private val venueListRepository = VenueListRepositoryImpl()

    fun getVenueList(db: VenueRoomDatabase, networkAvailable: Boolean) {
        val handler = CoroutineExceptionHandler { _, exception ->
            dismissProgress()
            mDataErrorMessage.value = exception.localizedMessage
        }
        showProgress()
        if (networkAvailable) {
            getCoroutineScope().launch(handler) {
                val result = withContext(coroutineContext) {
                    venueListRepository.getVenueList(getRequestParamForVenueList())
                }
                if (result.isSuccessful) {
                    val venueSearchResponse: VenueSearchResponse? = result.body()
                    mVenueSearchResponse.value = venueSearchResponse?.response?.venues
                } else {
                    mDataErrorMessage.value = Constants.TXT_DATA_NOT_AVAILABLE
                }
            }
        } else {
            getCoroutineScope().launch(handler) {
                val result = withContext(coroutineContext) {
                    venueListRepository.getVenueListFromDB(db)
                }
                dismissProgress()
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
            dismissProgress()
            mDataErrorMessage.value = exception.localizedMessage
        }

        getCoroutineScope().launch(handler) {
            withContext(coroutineContext) {
                venueListRepository.updateVenueListToDB(db, venueList ?: mutableListOf())
            }
            dismissProgress()

        }
    }


}