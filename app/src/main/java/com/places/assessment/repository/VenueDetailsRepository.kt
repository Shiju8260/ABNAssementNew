package com.places.assessment.repository

import com.places.assessment.model.venuesearchresponse.VenueDetails
import retrofit2.Response

//Repository for venue details view
interface VenueDetailsRepository {

    //Get venue details based on selected venueID through API call
    suspend fun getVenueDetails(venueId: String, query: Map<String, String>): Response<VenueDetails>
}