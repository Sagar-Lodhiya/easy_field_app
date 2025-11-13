package com.easyfield.base.response.dropdownresponse

data class City(
    val id: Int,
    val name: String
){
    override fun toString(): String {
        return name
    }
}