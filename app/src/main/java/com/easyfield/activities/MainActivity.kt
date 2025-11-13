package com.easyfield.activities

import android.os.Bundle
import com.easyfield.base.BaseActivity
import com.easyfield.databinding.ActivitySplashBinding

class MainActivity : BaseActivity() {

    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)


    }
}