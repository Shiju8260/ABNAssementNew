package com.places.assessment.model.venuesearchresponse


import com.google.gson.annotations.SerializedName

data class Item(
    @SerializedName("height")
    var height: Int = 0,
    @SerializedName("prefix")
    var prefix: String = "",
    @SerializedName("suffix")
    var suffix: String = "",
    @SerializedName("width")
    var width: Int = 0
)