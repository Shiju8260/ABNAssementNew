package com.abn.amroassessment.networkmanager

import com.abn.amroassessment.model.venuesearchresponse.VenueDetails
import com.abn.amroassessment.model.venuesearchresponse.VenueSearchResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.QueryMap

interface RestApi {

    @GET("venues/search?")
    suspend fun getVenueList(@QueryMap paramMap: Map<String, String>): Response<VenueSearchResponse>

    @GET("venues/{venueId}?")
    suspend fun getVenueDetails(
        @Path("venueId") venueId: String,
        @QueryMap paramMap: Map<String, String>
    ): Response<VenueDetails>

}