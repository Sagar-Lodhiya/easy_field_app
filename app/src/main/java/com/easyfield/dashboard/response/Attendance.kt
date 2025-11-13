package com.easyfield.dashboard.response

data class Attendance(
    val can_punch_in: Boolean,
    val can_punch_out: Boolean,
    val is_on_leave: Boolean,
    val punch_in_time: String,
    val punch_out_time: String,
    val punch_in_id:Int,
    val can_start_tracking :Boolean,
    val can_stop_tracking : Boolean,
    val punch_type:String?
)