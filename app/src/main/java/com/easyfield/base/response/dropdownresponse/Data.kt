package com.easyfield.base.response.dropdownresponse

data class Data(

    val expense_categories:List<Category>,
    val categories: List<Category>,
    val cities: List<Category>,
    val leave_types: List<Category>,

    val payment_details:List<Category>,
    val collection_of:List<Category>,
    val amount_type:List<Category>,
    val parties: List<Category>,
    val states: List<Category>


)