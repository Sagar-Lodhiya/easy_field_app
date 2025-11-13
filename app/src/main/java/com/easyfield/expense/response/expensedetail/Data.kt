package com.easyfield.expense.response.expensedetail

data class Data(
    val approved_amount: Int,
    val category_id: Int,
    val category_name: String,
    val city_id: Int,
    val city_name: String,
    val created_at: String,
    val expense_details: String,
    val expense_date:String,
    val expense_photo: String,
    val requested_amount: Int,
    val status_id: Int,
    val status_name: String
)