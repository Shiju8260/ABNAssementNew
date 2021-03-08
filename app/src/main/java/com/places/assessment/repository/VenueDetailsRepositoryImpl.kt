package com.places.assessment.repository

import com.places.assessment.model.venuesearchresponse.VenueDetails
import com.places.assessment.networkmanager.RestApi
import com.staralliance.networkframework.NetworkManager
import retrofit2.Response

//Implementation for venue details repository
class VenueDetailsRepositoryImpl : VenueDetailsRepository {
    override suspend fun getVenueDetails(
        venueId: String,
        query: Map<String, String>
    ): Response<VenueDetails> {
        return NetworkManager.getApi(RestApi::class.java).getVenueDetails(venueId, query)
    }
}