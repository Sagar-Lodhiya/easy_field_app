package com.easyfield.utils

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.google.gson.GsonBuilder

object PreferenceHelper {

    lateinit var preferences: SharedPreferences

    private inline fun SharedPreferences.edit(operation: (SharedPreferences.Editor) -> Unit) {
        val editor = this.edit()
        operation(editor)
        editor.apply()
    }

    /**
     * Call this first before retrieving or saving object.
     *
     */
    fun Application.initPreferenceHelper() {
        preferences = getSharedPreferences(packageName, Context.MODE_PRIVATE)
    }

    /**
     * puts a key value pair in shared prefs if doesn't exists, otherwise updates value on given [key]
     */
    operator fun set(key: String, value: Any?) {
        when (value) {
            is String? -> preferences.edit { it.putString(key, value) }
            is Int -> preferences.edit { it.putInt(key, value) }
            is Boolean -> preferences.edit { it.putBoolean(key, value) }
            is Float -> preferences.edit { it.putFloat(key, value) }
            is Long -> preferences.edit { it.putLong(key, value) }
            else -> throw UnsupportedOperationException("Not yet implemented")
        }
    }

    /**
     * finds value on given key.
     * [T] is the type of value
     * @param defaultValue optional default value - will take null for strings, false for bool and -1 for numeric values if [defaultValue] is not specified
     */
    inline operator fun <reified T : Any> get(
        key: String,
        defaultValue: T? = null
    ): T? {
        return when (T::class) {
            String::class -> preferences.getString(key, defaultValue as? String) as T?
            Int::class -> preferences.getInt(key, defaultValue as? Int ?: -1) as T?
            Boolean::class -> preferences.getBoolean(key, defaultValue as? Boolean ?: false) as T?
            Float::class -> preferences.getFloat(key, defaultValue as? Float ?: -1f) as T?
            Long::class -> preferences.getLong(key, defaultValue as? Long ?: -1) as T?
            else -> throw UnsupportedOperationException("Not yet implemented")
        }
    }

    /**
     * Saves object into the Preferences.
     *
     * @param `object` Object of model class (of type [T]) to save
     * @param key Key with which Shared preferences to
     **/
    fun <T> saveObject(`object`: T, key: String) {
        //Convert object to JSON String.
        val jsonString = GsonBuilder().create().toJson(`object`)
        //Save that String in SharedPreferences
        set(key, jsonString)
    }

    /**
     * Used to retrieve object from the Preferences.
     *
     * @param key Shared Preference key with which object was saved.
     **/
    inline fun <reified T> getObject(key: String): T? {
        //We read JSON String which was saved.
        val value = get(key, "")
        //JSON String was found which means object can be read.
        //We convert this JSON String to model object. Parameter "c" (of
        //type Class < T >" is used to cast.
        return if (value.isNullOrEmpty()) null else GsonBuilder().create()
            .fromJson(value, T::class.java)
    }

    fun clear() {
        val termsAndConditionFlag = get(AppConstants.PREF_KEY_SHOW_AGREEMENT, true)
        preferences.edit { it.clear() }
        set(AppConstants.PREF_KEY_SHOW_AGREEMENT, termsAndConditionFlag)
    }
}