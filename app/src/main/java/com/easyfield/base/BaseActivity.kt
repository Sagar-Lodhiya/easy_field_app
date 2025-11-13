package com.easyfield.base

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.easyfield.activities.MainActivity
import com.easyfield.location.NewLocationService
import com.easyfield.utils.AppConstants
import com.easyfield.utils.PreferenceHelper
import com.easyfield.viewmodels.UserViewModel

abstract class BaseActivity : AppCompatActivity() {

    val userViewModel: UserViewModel by viewModels()

    override fun onResume() {
        super.onResume()
    }

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
    }


    public fun doLogout() {


        PreferenceHelper.clear()
        finish()
        startActivity(Intent(this, MainActivity::class.java))

        PreferenceHelper[AppConstants.PREF_KEY_IS_HELP_SCREEN] = true


        stopService(
            Intent(
                this,
                NewLocationService::class.java
            )
        )



    }
}
