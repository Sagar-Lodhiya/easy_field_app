package com.easyfield.base.response.dropdownresponse

data class Category(
    val id: Int,
    val name: String
){
    override fun toString(): String {
        return name
    }
}