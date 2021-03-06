package com.abn.amroassessment.repository

import com.abn.amroassessment.database.VenueRoomDatabase
import com.abn.amroassessment.model.venuesearchresponse.Venue
import com.abn.amroassessment.model.venuesearchresponse.VenueSearchResponse
import retrofit2.Response

interface VenueListRepository {

    suspend fun getVenueList(query: Map<String, String>): Response<VenueSearchResponse>

    suspend fun getVenueListFromDB(db: VenueRoomDatabase): List<Venue>

    suspend fun updateVenueListToDB(
        db: VenueRoomDatabase,
        venueList: MutableList<Venue>
    ): Void

}