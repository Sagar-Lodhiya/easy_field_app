package com.easyfield.utils.extension

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.Intent.*
import android.net.Uri
import android.os.Build

fun Context.openWebPage(url: String) {
    try {
        val intent = Intent(ACTION_VIEW, Uri.parse(url)).apply {
            // The URL should either launch directly in a non-browser app
            // (if it’s the default), or in the disambiguation dialog
            addCategory(CATEGORY_BROWSABLE)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                flags = FLAG_ACTIVITY_NEW_TASK or FLAG_ACTIVITY_REQUIRE_NON_BROWSER
            }
        }
        startActivity(intent)
    } catch (e: ActivityNotFoundException) {
        // Only browser apps are available, or a browser is the default app for this intent
    }

}