package com.abn.amroassessment.database

import androidx.room.TypeConverter
import com.abn.amroassessment.model.venuesearchresponse.BestPhoto
import com.abn.amroassessment.model.venuesearchresponse.Contact
import com.abn.amroassessment.model.venuesearchresponse.Location
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {

//    @TypeConverter
//    fun fromBestPhoto(bestPhoto: BestPhoto?): String? {
//        val type = object : TypeToken<BestPhoto>() {}.type
//        return Gson().toJson(bestPhoto, type)
//    }
//
//    @TypeConverter
//    fun toBestPhoto(bestPhotoString: String?): BestPhoto? {
//        val type = object : TypeToken<BestPhoto>() {}.type
//        return Gson().fromJson<BestPhoto>(bestPhotoString, type)
//    }
//
//    @TypeConverter
//    fun fromContact(contact: Contact?): String? {
//        val type = object : TypeToken<Contact>() {}.type
//        return Gson().toJson(contact, type)
//    }
//
//    @TypeConverter
//    fun toContact(bestPhotoString: String?): Contact? {
//        val type = object : TypeToken<Contact>() {}.type
//        return Gson().fromJson<Contact>(bestPhotoString, type)
//    }
//
//    @TypeConverter
//    fun fromLocation(location: Location?): String? {
//        val type = object : TypeToken<Location>() {}.type
//        return Gson().toJson(location, type)
//    }
//
//    @TypeConverter
//    fun toLocation(location: String?): Location? {
//        val type = object : TypeToken<Location>() {}.type
//        return Gson().fromJson<Location>(location, type)
//    }


    @TypeConverter
    fun stringListToJson(value: List<String>?) = Gson().toJson(value)

    @TypeConverter
    fun jsonToStringList(value: String) = Gson().fromJson(value, Array<String>::class.java).toList()
}