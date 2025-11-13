package com.easyfield.base.response.commonresponse

data class CommonResponse(
    val message: String,
    val status: Int,
    val success: Boolean
)