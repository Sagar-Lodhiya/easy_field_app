package com.easyfield.expense.response.expenselist

data class ExpenseListResponse(
    val `data`: Data,
    val message: String,
    val status: Int,
    val success: Boolean
)