package com.easyfield.profile.response

data class ProfileResponse(
    val `data`: Data,
    val message: String,
    val status: Int,
    val success: Boolean
)