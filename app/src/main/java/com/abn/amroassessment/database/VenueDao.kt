package com.abn.amroassessment.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.abn.amroassessment.model.venuesearchresponse.Venue

@Dao
interface VenueDao {

    @Query("SELECT * from venue_table")
    fun getVenues(): List<Venue>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    @JvmSuppressWildcards
    fun insertAll(venueList: MutableList<Venue>)

    @Query("DELETE from venue_table")
    fun deleteVenues()

}