package com.easyfield.party.request

import com.google.gson.annotations.SerializedName

data class AddPartyRequest(




    @SerializedName("firm_name") var firm_name: String? = null,
    @SerializedName("dealer_name") var dealer_name: String? = null,


    @SerializedName("party_category_id") var party_category_id: Int? = null,
    @SerializedName("dealer_phone") var dealer_phone: String? = null,
    @SerializedName("city_or_town") var dealer_city: String? = null,
    @SerializedName("address") var dealer_address: String? = null,


    @SerializedName("gst_number") var gst_no: String? = null,
    @SerializedName("dealer_aadhar") var aadhar_no: String? = null,
)
