package com.easyfield.payment.response

data class Content(
    val amount: String,
    val amount_details: String,
    val amount_type: String,
    val collection_of: String,
    val created_at: String,
    val extra: String,
    val party_id: Int,
    val party_name: String,
    val payments_details: String,
    val payments_photo: String,
    val status_id: Int,
    val status_name: String,
    val id:Int
)