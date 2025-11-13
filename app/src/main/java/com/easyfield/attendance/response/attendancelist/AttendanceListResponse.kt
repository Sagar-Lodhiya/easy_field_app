package com.easyfield.attendance.response.attendancelist

data class AttendanceListResponse(
    val `data`: Data,
    val message: String,
    val status: Int,
    val success: Boolean
)