package com.easyfield.payment.response.paymentDetailresponse

data class Data(
    val amount: String,
    val amount_details: String,
    val amount_type: String,
    val collection_of: String,
    val created_at: String,
    val extra: String,
    val id: Int,
    val party_id: Int,
    val party_name: String,
    val payments_details: Any,
    val payments_photo: String,
    val status_id: Int,
    val status_name: String
)