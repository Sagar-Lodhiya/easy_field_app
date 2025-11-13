package com.easyfield.expense.response.expensedetail

data class ExpenseDetailResponse(
    val `data`: Data,
    val message: String,
    val status: Int,
    val success: Boolean
)