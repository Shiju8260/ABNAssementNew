package com.abn.amroassessment.model.venuesearchresponse


import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Entity(tableName = "location_table")
data class Location(
    @PrimaryKey(autoGenerate = true)
    var locId: Int=0,
    @SerializedName("formattedAddress")
    @Expose
    var formattedAddress: List<String> = listOf()
)