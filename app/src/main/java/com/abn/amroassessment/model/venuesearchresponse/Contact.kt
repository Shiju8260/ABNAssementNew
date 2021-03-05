package com.abn.amroassessment.model.venuesearchresponse


import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Entity(tableName = "contact_table")
data class Contact(
    @PrimaryKey(autoGenerate = true)
    var contactId: Int=0,
    @SerializedName("facebook")
    @Expose
    var facebook: String = "",
    @SerializedName("facebookName")
    @Expose
    var facebookName: String = "",
    @SerializedName("facebookUsername")
    @Expose
    var facebookUsername: String? = null,
    @SerializedName("formattedPhone")
    @Expose
    var formattedPhone: String? = null,
    @SerializedName("instagram")
    @Expose
    var instagram: String? = null,
    @SerializedName("phone")
    @Expose
    var phone: String? = null,
    @SerializedName("twitter")
    @Expose
    var twitter: String? = null
)