package com.easyfield.utils

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object GsonUtils {

    val gson = Gson()
    val mapType = object : TypeToken<Map<String, Any>>() {}.type

    fun getMap(any: Any): Map<String, String> {
        return gson.fromJson(gson.toJson(any), mapType)
    }
}