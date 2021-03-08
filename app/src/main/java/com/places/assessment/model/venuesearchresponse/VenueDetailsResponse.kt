package com.places.assessment.model.venuesearchresponse

import com.google.gson.annotations.SerializedName

data class VenueDetailsResponse(
    @SerializedName("venue")
    var venue: Venue = Venue()
)
