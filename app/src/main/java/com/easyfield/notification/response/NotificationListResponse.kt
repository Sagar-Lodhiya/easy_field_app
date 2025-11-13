package com.easyfield.notification.response

data class NotificationListResponse(
    val `data`: Data,
    val message: String,
    val status: Int,
    val success: Boolean
)