package com.easyfield.base.response.dropdownresponse

data class DropDownResponse(
    val `data`: Data,
    val message: String,
    val status: Int,
    val success: Boolean
)