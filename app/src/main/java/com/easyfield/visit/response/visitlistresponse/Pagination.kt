package com.easyfield.visit.response.visitlistresponse

data class Pagination(
    val currentPage: Int,
    val pageCount: Int,
    val perPage: Int,
    val totalCount: Int
)