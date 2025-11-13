package com.easyfield.menu.response

data class MenuResponse(
    val `data`: Data,
    val message: String,
    val status: Int,
    val success: Boolean
)