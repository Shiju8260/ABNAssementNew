package com.abn.assessment

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.abn.amroassessment.R
import com.abn.amroassessment.database.VenueRoomDatabase
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
    }
}
fun View.snack(message: String, duration: Int = Snackbar.LENGTH_LONG) {
    Snackbar.make(this, message, duration).show()
}