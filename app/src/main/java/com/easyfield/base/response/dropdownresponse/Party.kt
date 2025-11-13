package com.easyfield.base.response.dropdownresponse

data class Party(
    val id: Int,
    val name: String
){
    override fun toString(): String {
        return name
    }
}