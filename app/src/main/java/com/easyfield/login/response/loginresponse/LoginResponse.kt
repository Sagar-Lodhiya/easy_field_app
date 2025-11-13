package com.easyfield.login.response.loginresponse

data class LoginResponse(
    val `data`: Data,
    val message: String,
    val status: Int,
    val success: Boolean
)