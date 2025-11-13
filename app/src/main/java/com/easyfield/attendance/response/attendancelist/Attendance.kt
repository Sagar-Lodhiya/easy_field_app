package com.easyfield.attendance.response.attendancelist

data class Attendance(
    val created_at: String,
    val day: String,
    val day_number: String,
    val name: String,
    val punch_in: String,
    val punch_out: String,
    val type: String,
    val status:String,
    val user_id: Int,
    val id:Int
)