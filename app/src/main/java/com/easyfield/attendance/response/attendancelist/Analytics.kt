package com.easyfield.attendance.response.attendancelist

data class Analytics(
    val absent: String,
    val leaves: String,
    val public_holiday: String,
    val working_days: String
)