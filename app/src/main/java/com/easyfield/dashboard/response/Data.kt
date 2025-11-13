package com.easyfield.dashboard.response

data class Data(
    val analytics: List<Analytic>,
    val attendance: Attendance,
    val user: User
)