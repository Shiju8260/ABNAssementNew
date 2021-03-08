package com.places.assessment.model.venuesearchresponse


import com.google.gson.annotations.SerializedName

data class Source(
    @SerializedName("name")
    var name: String = "",
    @SerializedName("url")
    var url: String = ""
)