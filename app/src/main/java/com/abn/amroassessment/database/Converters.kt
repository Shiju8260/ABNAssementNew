package com.abn.amroassessment.database

import androidx.room.TypeConverter
import com.abn.amroassessment.model.venuesearchresponse.BestPhoto
import com.abn.amroassessment.model.venuesearchresponse.Contact
import com.abn.amroassessment.model.venuesearchresponse.Location
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {

    @TypeConverter
    fun stringListToJson(value: List<String>?) = Gson().toJson(value)

    @TypeConverter
    fun jsonToStringList(value: String) = Gson().fromJson(value, Array<String>::class.java).toList()
}