package com.easyfield.attendance.response.leavelist

data class Pagination(
    val currentPage: Int,
    val pageCount: Int,
    val perPage: Int,
    val totalCount: Int
)