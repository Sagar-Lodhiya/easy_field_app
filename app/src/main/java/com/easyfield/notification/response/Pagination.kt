package com.easyfield.notification.response

data class Pagination(
    val currentPage: Int,
    val pageCount: Int,
    val perPage: Int,
    val totalCount: Int
)