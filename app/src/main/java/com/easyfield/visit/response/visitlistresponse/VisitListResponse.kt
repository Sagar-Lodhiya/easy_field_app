package com.easyfield.visit.response.visitlistresponse

data class VisitListResponse(
    val `data`: Data,
    val message: String,
    val status: Int,
    val success: Boolean
)