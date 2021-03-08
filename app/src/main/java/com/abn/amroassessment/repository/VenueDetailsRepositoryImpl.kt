package com.abn.amroassessment.repository

import com.abn.amroassessment.model.venuesearchresponse.VenueDetails
import com.abn.amroassessment.networkmanager.RestApi
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