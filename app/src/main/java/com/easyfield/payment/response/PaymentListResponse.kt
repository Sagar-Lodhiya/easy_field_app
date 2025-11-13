package com.easyfield.payment.response

data class PaymentListResponse(
    val `data`: Data,
    val message: String,
    val status: Int,
    val success: Boolean
)