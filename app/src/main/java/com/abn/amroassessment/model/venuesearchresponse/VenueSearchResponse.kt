package com.abn.amroassessment.model.venuesearchresponse


import com.google.gson.annotations.SerializedName

data class VenueSearchResponse(
    @SerializedName("meta")
    var meta: Meta = Meta(),
    @SerializedName("response")
    var response: Response = Response()
)