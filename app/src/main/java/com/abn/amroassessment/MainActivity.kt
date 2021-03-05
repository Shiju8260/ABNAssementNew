package com.abn.assessment

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.abn.amroassessment.R
import com.abn.amroassessment.database.VenueRoomDatabase

class MainActivity : AppCompatActivity() {

   lateinit var db: VenueRoomDatabase
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        db= VenueRoomDatabase.getDatabase(this)
    }
}
