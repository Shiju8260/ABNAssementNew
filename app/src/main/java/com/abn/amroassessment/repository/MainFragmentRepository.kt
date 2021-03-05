package com.abn.amroassessment.repository

import com.abn.amroassessment.model.venuesearchresponse.Venue
import com.abn.amroassessment.model.venuesearchresponse.VenueSearchResponse
import retrofit2.Response

interface MainFragmentRepository {

    suspend fun getVenueList(query: Map<String,String>): Response<VenueSearchResponse>

    suspend fun getVenueListFromDB(): Response<List<Venue>>

}