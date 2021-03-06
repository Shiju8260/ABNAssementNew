package com.abn.amroassessment.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.abn.amroassessment.model.venuesearchresponse.BestPhoto
import com.abn.amroassessment.model.venuesearchresponse.Contact
import com.abn.amroassessment.model.venuesearchresponse.Location
import com.abn.amroassessment.model.venuesearchresponse.Venue
import com.abn.amroassessment.utils.Constants

@Database(
    entities = [Venue::class, Location::class, BestPhoto::class, Contact::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class VenueRoomDatabase : RoomDatabase() {
    abstract fun venueDao(): VenueDao

    companion object {
        @Volatile
        private var INSTANCE: VenueRoomDatabase? = null


        fun getDatabase(
            context: Context
        ): VenueRoomDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE
                ?: synchronized(this) {
                    val instance = Room.databaseBuilder(
                        context.applicationContext,
                        VenueRoomDatabase::class.java,
                        Constants.VENUE_DATABASE
                    ).allowMainThreadQueries().build()
                    INSTANCE = instance
                    instance
                }
        }
    }
}