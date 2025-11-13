package com.codeflixweb.callenza.network

import android.util.Log

open class Event<out T>(private val content: T) {

    var hasBeenHandled = false
        private set

    fun getContentIfNotHandled() = if (hasBeenHandled) {
        null
    } else {
        Log.e("vishaldone","yes")

        hasBeenHandled = true
        content
    }

    fun peekContent() = content
}
