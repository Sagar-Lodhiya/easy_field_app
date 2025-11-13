package com.easyfield.dashboard.response

data class HomeResponse(
    val `data`: Data,
    val message: String,
    val status: Int,
    val success: Boolean
)