package com.easyfield.visit.response.visitlistresponse

data class Content(
    val created_at: String,
    val discussion_point: String,
    val duration: String,
    val latitude: Double,
    val longitude: Double,
    val party_id: Int,
    val party_name: String,
    val place: String,
    val remarks: String,
    val time: String,
    val id:Int
)