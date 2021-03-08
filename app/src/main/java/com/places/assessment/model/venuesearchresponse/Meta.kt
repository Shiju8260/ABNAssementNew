package com.places.assessment.model.venuesearchresponse


import com.google.gson.annotations.SerializedName

data class Meta(
    @SerializedName("code")
    var code: Int = 0,
    @SerializedName("requestId")
    var requestId: String = ""
)