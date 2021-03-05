package com.abn.amroassessment.model.venuesearchresponse


import com.google.gson.annotations.SerializedName

data class Response(
    @SerializedName("venues")
    var venues: MutableList<Venue> = mutableListOf()
)