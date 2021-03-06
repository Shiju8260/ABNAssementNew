package com.abn.amroassessment.networkmanager

import com.abn.amroassessment.model.venuesearchresponse.VenueDetails
import com.abn.amroassessment.model.venuesearchresponse.VenueSearchResponse
import com.abn.amroassessment.utils.Constants
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.QueryMap

interface RestApi {

    @GET(Constants.API_VENUE_SEARCH)
    suspend fun getVenueList(@QueryMap paramMap: Map<String, String>): Response<VenueSearchResponse>

    @GET(Constants.API_VENUE_DETAILS)
    suspend fun getVenueDetails(
        @Path(Constants.API_VENUE_ID_PATH_NAME) venueId: String,
        @QueryMap paramMap: Map<String, String>
    ): Response<VenueDetails>

}