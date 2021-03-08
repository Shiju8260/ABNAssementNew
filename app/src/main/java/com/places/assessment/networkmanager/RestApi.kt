package com.places.assessment.networkmanager

import com.places.assessment.model.venuesearchresponse.VenueDetails
import com.places.assessment.model.venuesearchresponse.VenueSearchResponse
import com.places.assessment.utils.Constants
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.QueryMap

//API calls thoughout application
interface RestApi {

    @GET(Constants.API_VENUE_SEARCH)
    suspend fun getVenueList(@QueryMap paramMap: Map<String, String>): Response<VenueSearchResponse>

    @GET(Constants.API_VENUE_DETAILS)
    suspend fun getVenueDetails(
        @Path(Constants.API_VENUE_ID_PATH_NAME) venueId: String,
        @QueryMap paramMap: Map<String, String>
    ): Response<VenueDetails>

}