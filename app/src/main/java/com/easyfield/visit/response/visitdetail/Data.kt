package com.easyfield.visit.response.visitdetail

data class Data(
    val created_at: String,
    val discussion_point: String,
    val duration: String,
    val id: Int,
    val latitude: Double,
    val longitude: Double,
    val party_id: Int,
    val party_name: String,
    val place: String,
    val remarks: String,
    val time: String
)