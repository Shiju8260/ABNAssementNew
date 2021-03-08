package com.places.assessment.model.venuesearchresponse


import com.google.gson.annotations.SerializedName

data class Group(
    @SerializedName("items")
    var items: List<Item> = listOf()
)