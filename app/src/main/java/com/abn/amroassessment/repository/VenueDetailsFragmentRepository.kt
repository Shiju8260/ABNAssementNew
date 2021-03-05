package com.abn.amroassessment.repository

import com.abn.amroassessment.model.venuesearchresponse.VenueDetails
import retrofit2.Response

interface VenueDetailsFragmentRepository {
    suspend fun getVenueDetails(venueId: String, query: Map<String, String>): Response<VenueDetails>
}