package com.places.assessment.model.venuesearchresponse

import com.google.gson.annotations.SerializedName

data class VenueDetails(
    @SerializedName("meta")
    var meta: Meta = Meta(),
    @SerializedName("response")
    var response: VenueDetailsResponse = VenueDetailsResponse()
)
