package com.easyfield.visit.response.visitdetail

data class VisitDetailResponse(
    val `data`: Data,
    val message: String,
    val status: Int,
    val success: Boolean
)