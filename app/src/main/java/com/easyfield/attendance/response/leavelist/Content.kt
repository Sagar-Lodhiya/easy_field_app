package com.easyfield.attendance.response.leavelist

data class Content(
    val end_date: String,
    val leave_type: String,
    val leave_type_id: Int,
    val name: String,
    val reason: String,
    val start_date: String,
    val status: Int,
    val user_id: Int
)