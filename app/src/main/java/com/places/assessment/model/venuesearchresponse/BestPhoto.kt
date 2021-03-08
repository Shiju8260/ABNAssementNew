package com.places.assessment.model.venuesearchresponse


import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Entity(tableName = "bestphoto_table")
data class BestPhoto(
    @PrimaryKey(autoGenerate = true)
    var photoId: Int=0,
    @SerializedName("createdAt")
    @Expose
    var createdAt: Int = 0,
    @SerializedName("height")
    @Expose
    var height: Int = 0,
    @SerializedName("prefix")
    @Expose
    var prefix: String = "",
    @SerializedName("suffix")
    @Expose
    var suffix: String = "",
    @SerializedName("visibility")
    @Expose
    var visibility: String = "",
    @SerializedName("width")
    @Expose
    var width: Int = 0
)