package com.abn.amroassessment.repository

import com.abn.amroassessment.model.venuesearchresponse.Venue
import com.abn.amroassessment.model.venuesearchresponse.VenueSearchResponse
import com.abn.amroassessment.networkmanager.RestApi
import com.staralliance.networkframework.NetworkManager
import retrofit2.Response

class VenueListRepositoryImpl : VenueListRepository {

    override suspend fun getVenueList(query: Map<String, String>): Response<VenueSearchResponse> {
        return NetworkManager.getApi(RestApi::class.java).getVenueList(query)
    }

    override suspend fun getVenueListFromDB(): Response<List<Venue>> {
        TODO("Not yet implemented")
    }

}