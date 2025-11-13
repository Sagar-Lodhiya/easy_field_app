package com.easyfield.expense.response.expenselist

data class Content(
    val category_id: Int,
    val category_name: String,
    val city_id: Int,
    val city_name: String,
    val expense_date: String,
    val expense_details: String,
    val expense_photo: String,
    val id: Int,
    val requested_amount: Int,
    val approved_amount:Int,
    val status_name:String,
    val status_id:Int,   val type: String
) {

}