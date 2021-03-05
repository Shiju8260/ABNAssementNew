package com.abn.amroassessment.model.venuesearchresponse


import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Entity(tableName = "venue_table")
data class Venue(
    @PrimaryKey(autoGenerate = true)
    var venueId: Int=0,
    @SerializedName("id")
    @Expose
    var venueUniqId: String = "",
    @SerializedName("rating")
    @Expose
    var rating: Int = 0,
    @SerializedName("ratingColor")
    @Expose
    var ratingColor: String = "",
    @SerializedName("description")
    @Expose
    var description: String? = null,
    @SerializedName("location")
    @Expose
    @Embedded
    var location: Location = Location(),
    @SerializedName("contact")
    @Expose
    @Embedded
    var contact: Contact = Contact(),
    @SerializedName("bestPhoto")
    @Expose
    @Embedded
    var bestPhoto: BestPhoto = BestPhoto(),
    @SerializedName("name")
    @Expose
    var name: String = ""
)

@BindingAdapter("android:setAddress")
fun setAddress(textView: TextView, model: Venue) {
    textView.text = model.location.formattedAddress.joinToString()
}
