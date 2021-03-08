package com.places.assessment.model.venuesearchresponse


import com.google.gson.annotations.SerializedName

data class Photos(
    @SerializedName("groups")
    var groups: List<Group> = listOf()
)