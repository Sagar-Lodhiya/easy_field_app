package com.easyfield.punchin.response

data class PunchResponse(
    val `data`: Data,
    val message: String,
    val status: Int,
    val success: Boolean
)