package com.abn.amroassessment.repository

import com.abn.amroassessment.database.VenueDao
import com.abn.amroassessment.database.VenueRoomDatabase
import com.abn.amroassessment.model.venuesearchresponse.Venue
import com.abn.amroassessment.model.venuesearchresponse.VenueSearchResponse
import com.abn.amroassessment.networkmanager.RestApi
import com.staralliance.networkframework.NetworkManager
import retrofit2.Response

class VenueListRepositoryImpl : VenueListRepository {

    override suspend fun getVenueList(query: Map<String, String>): Response<VenueSearchResponse> {
        return NetworkManager.getApi(RestApi::class.java).getVenueList(query)
    }

    override suspend fun getVenueListFromDB(dao: VenueDao): List<Venue> {
        return dao.getVenues()
    }

    override suspend fun updateVenueListToDB(
        dao: VenueDao,
        venueList: MutableList<Venue>
    ): Void {
        dao.deleteVenues()
        return dao.insertAll(venueList)
    }


}