package com.easyfield.login.response.loginresponse

data class User(
    val access_token: String,
    val app_version: String,
    val created_at: String,
    val device_id: String,
    val device_model: String,
    val device_type: String,
    val eligible_km: String,
    val employee_id: String,
    val id: Int,
    val last_name: String,
    val name: String,
    val os_version: String,
    val updated_at: String,
    val user_type :Int,
)