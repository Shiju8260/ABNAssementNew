package com.abn.amroassessment.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.abn.amroassessment.BuildConfig
import com.abn.amroassessment.model.venuesearchresponse.Venue
import com.abn.amroassessment.model.venuesearchresponse.VenueDetails
import com.abn.amroassessment.repository.VenueDetailsRepositoryImpl
import com.abn.amroassessment.utils.Constants
import com.abn.assessment.viewmodel.BaseViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class VenueDetailsViewModel : BaseViewModel() {

    val mVenueDetails: MutableLiveData<Venue> = MutableLiveData()

    val title = MutableLiveData<String>()
    val description = MutableLiveData<String>()
    val phone = MutableLiveData<String>()
    val twitter = MutableLiveData<String>()
    val instagram = MutableLiveData<String>()
    val facebook = MutableLiveData<String>()
    val address = MutableLiveData<String>()
    val rating = MutableLiveData<Float>()
    val photo = MutableLiveData<String>()

    private val mResultSuccess: MutableLiveData<Boolean> = MutableLiveData()
    val mResultSuccessLiveData: LiveData<Boolean> by lazy { mResultSuccess }

    private val mApiErrorMessage: MutableLiveData<String> = MutableLiveData()
    val mApiErrorMessageLiveData: LiveData<String> by lazy { mApiErrorMessage }


    private val venueDetailsRepository = VenueDetailsRepositoryImpl()

    fun getVenueDetails(venueId: String?) {
        val handler = CoroutineExceptionHandler { _, exception ->
            dismissProgress()
            mApiErrorMessage.value = exception.localizedMessage
        }
        showProgress()
        getCoroutineScope().launch(handler) {
            val result = withContext(coroutineContext) {
                venueDetailsRepository.getVenueDetails(
                    venueId ?: "",
                    getRequestParamForVenueDetails()
                )
            }
            dismissProgress()
            if (result.isSuccessful) {
                val venueDetails: VenueDetails? = result.body()
                mResultSuccess.value = true
                venueDetails?.response?.venue?.let {
                    title.value = it.name
                    description.value = it.description ?: Constants.TXT_NOT_AVAILABLE
                    phone.value = it.contact.formattedPhone
                        ?: Constants.TXT_NOT_AVAILABLE
                    facebook.value = it.contact.facebookUsername
                        ?: Constants.TXT_NOT_AVAILABLE
                    twitter.value = it.contact.twitter ?: Constants.TXT_NOT_AVAILABLE
                    instagram.value = it.contact.instagram ?: Constants.TXT_NOT_AVAILABLE
                    address.value = it.location.formattedAddress.joinToString()
                    rating.value = it.rating.toFloat()
                    val bestPhoto = it.bestPhoto
                    val sb = StringBuilder()
                    sb.append(bestPhoto.prefix).append(bestPhoto.width).append("x")
                        .append(bestPhoto.height).append(bestPhoto.suffix)
                    photo.value = sb.toString()
                    mVenueDetails.value = it
                }

            } else {
                mResultSuccess.value = false
            }
        }

    }

    override fun onCleared() {
        super.onCleared()
    }

    private fun getRequestParamForVenueDetails(): Map<String, String> {
        val query: HashMap<String, String> = HashMap()
        query[Constants.PARAM_CLIENTID] = BuildConfig.CLIENT_ID
        query[Constants.PARAM_CLIENTSECRET] = BuildConfig.CLIENT_SECRET
        query[Constants.PARAM_V] = "20210302"
        return query
    }


}