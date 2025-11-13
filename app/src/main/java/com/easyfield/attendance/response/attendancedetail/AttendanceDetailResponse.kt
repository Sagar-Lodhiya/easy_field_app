package com.easyfield.attendance.response.attendancedetail

data class AttendanceDetailResponse(
    val `data`: Data,
    val message: String,
    val status: Int,
    val success: Boolean
)