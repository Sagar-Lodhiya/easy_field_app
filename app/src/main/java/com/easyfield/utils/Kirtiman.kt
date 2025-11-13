package com.easyfield.utils

import android.app.Application
import android.content.Context
import com.easyfield.utils.PreferenceHelper.initPreferenceHelper


class Kirtiman : Application() {

    init {
        instance = this
    }

    companion object {

        private var instance: Kirtiman? = null


        fun applicationContext(): Context {
            return instance!!.applicationContext
        }
    }

    override fun onCreate() {
        super.onCreate()
        initPreferenceHelper()
    }

}