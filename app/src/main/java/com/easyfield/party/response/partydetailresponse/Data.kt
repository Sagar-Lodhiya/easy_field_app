package com.easyfield.party.response.partydetailresponse

data class Data(
    val address: String,
    val city_or_town: String,
    val created_at: String,
    val dealer_aadhar: String,
    val dealer_name: String,
    val dealer_phone: String,
    val employee_id: Int,
    val firm_name: String,
    val gst_number: String,
    val id: Int,
    val party_category_id: Int,
    val party_category_name: String
)