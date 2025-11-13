package com.easyfield.party.response.partydetailresponse

data class PartyDetailResponse(
    val `data`: Data,
    val message: String,
    val status: Int,
    val success: Boolean
)