package com.example.reserves.converters

import androidx.room.TypeConverter
import com.example.reserves.entities.CommentData
import com.google.gson.Gson

class ListConverter {

    var gson = Gson()

    @TypeConverter
    fun someObjectListToString(someObjects: List<CommentData>?): String? {
        return gson.toJson(someObjects)
    }

}