package com.abn.amroassessment.viewmodel

import androidx.lifecycle.MutableLiveData
import com.abn.amroassessment.BuildConfig
import com.abn.amroassessment.model.venuesearchresponse.Venue
import com.abn.amroassessment.model.venuesearchresponse.VenueDetails
import com.abn.amroassessment.repository.VenueDetailsFragmentRepositoryImpl
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
    val rating = MutableLiveData<Int>()
    val photo = MutableLiveData<String>()


    private val venueDetailsFragmentRepository = VenueDetailsFragmentRepositoryImpl()

    fun getVenueDetails(venueId: String?) {
        val handler = CoroutineExceptionHandler { _, exception ->

        }
        getCoroutineScope().launch(handler) {
            val result = withContext(coroutineContext) {
                venueDetailsFragmentRepository.getVenueDetails(
                    venueId ?: "",
                    getRequestParamForVenueDetails()
                )
            }
            if (result.isSuccessful) {
                val venueDetails: VenueDetails? = result.body()
                title.value = venueDetails?.response?.venue?.name
                description.value =
                    venueDetails?.response?.venue?.description ?: Constants.TXT_NOT_AVAILABLE
                phone.value = venueDetails?.response?.venue?.contact?.formattedPhone
                    ?: Constants.TXT_NOT_AVAILABLE
                facebook.value = venueDetails?.response?.venue?.contact?.facebookUsername
                    ?: Constants.TXT_NOT_AVAILABLE
                twitter.value =
                    venueDetails?.response?.venue?.contact?.twitter ?: Constants.TXT_NOT_AVAILABLE
                instagram.value =
                    venueDetails?.response?.venue?.contact?.instagram ?: Constants.TXT_NOT_AVAILABLE
                address.value =
                    venueDetails?.response?.venue?.location?.formattedAddress?.joinToString()
                rating.value = venueDetails?.response?.venue?.rating
                val bestPhoto = venueDetails?.response?.venue?.bestPhoto
                val sb = StringBuilder()
                sb.append(bestPhoto?.prefix).append(bestPhoto?.width).append("x").append(bestPhoto?.height).append(bestPhoto?.suffix)
                photo.value = sb.toString()
                mVenueDetails.value = venueDetails?.response?.venue
            }
        }

    }

    private fun getRequestParamForVenueDetails(): Map<String, String> {
        val query: HashMap<String, String> = HashMap()
        query[Constants.PARAM_CLIENTID] = BuildConfig.CLIENT_ID
        query[Constants.PARAM_CLIENTSECRET] = BuildConfig.CLIENT_SECRET
        query[Constants.PARAM_V] = "20210302"
        return query
    }


}