package com.easyfield.attendance.response.leavelist

data class LeaveListResponse(
    val `data`: Data,
    val message: String,
    val status: Int,
    val success: Boolean
)