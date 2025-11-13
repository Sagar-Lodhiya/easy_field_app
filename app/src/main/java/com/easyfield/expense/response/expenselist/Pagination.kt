package com.easyfield.expense.response.expenselist

data class Pagination(
    val currentPage: Int,
    val pageCount: Int,
    val perPage: Int,
    val totalCount: Int
)