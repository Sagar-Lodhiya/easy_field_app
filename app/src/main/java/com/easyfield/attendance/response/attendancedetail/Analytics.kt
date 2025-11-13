package com.easyfield.attendance.response.attendancedetail

data class Analytics(
    val created_at: String,
    val da_amount: String,
    val punch_in_date: String,
    val punch_in_image: String,
    val punch_in_place: String,
    val punch_in_time: String,
    val punch_out_date: String,
    val punch_out_image: String,
    val punch_out_place: String,
    val punch_out_time: String,
    val ta_amount: String,
    val total_hours: String,
    val tour_details: String,
    val traveled_km: String,
    val vehicle_type: String
)