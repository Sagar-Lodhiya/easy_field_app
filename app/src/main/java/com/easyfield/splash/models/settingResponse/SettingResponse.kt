package com.easyfield.splash.models.settingResponse

data class SettingResponse(
    val `data`: Data,
    val message: String,
    val status: Int,
    val success: Boolean
)