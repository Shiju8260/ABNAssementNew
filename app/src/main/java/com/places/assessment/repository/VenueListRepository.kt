package com.places.assessment.repository

import com.places.assessment.database.VenueDao
import com.places.assessment.model.venuesearchresponse.Venue
import com.places.assessment.model.venuesearchresponse.VenueSearchResponse
import retrofit2.Response

//Repository for venue list view
interface VenueListRepository {

    suspend fun getVenueList(query: Map<String, String>): Response<VenueSearchResponse>

    suspend fun getVenueListFromDB(dao: VenueDao): List<Venue>

    suspend fun updateVenueListToDB(
        dao: VenueDao,
        venueList: MutableList<Venue>
    ): Void

}