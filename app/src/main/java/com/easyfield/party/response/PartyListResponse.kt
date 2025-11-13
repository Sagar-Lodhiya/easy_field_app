package com.easyfield.party.response

data class PartyListResponse(
    val `data`: Data,
    val message: String,
    val status: Int,
    val success: Boolean
)