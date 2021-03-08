package com.places.assessment.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.places.assessment.model.venuesearchresponse.Venue

//Data Access class for database
@Dao
interface VenueDao {

    @Query("SELECT * from venue_table")
    fun getVenues(): List<Venue>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    @JvmSuppressWildcards
    fun insertAll(venueList: MutableList<Venue>): Void

    @Query("DELETE from venue_table")
    fun deleteVenues()

}