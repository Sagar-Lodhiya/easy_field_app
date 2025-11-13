package com.easyfield.payment.response.paymentDetailresponse

data class PaymentDetailResponse(
    val `data`: Data,
    val message: String,
    val status: Int,
    val success: Boolean
)