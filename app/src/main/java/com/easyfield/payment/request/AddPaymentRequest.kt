package com.easyfield.payment.request

import com.google.gson.annotations.SerializedName

data class AddPaymentRequest(
    @SerializedName("created_at") var created_at: String? = null,
    @SerializedName("party_id") var partyId: Int? = null,
    @SerializedName("amount") var amount: Double? = null,
    @SerializedName("amount_type") var amountType: String? = null,
    @SerializedName("amount_details") var amount_details: String? = null,
    @SerializedName("collection_of") var collectionOf: String? = null,
    @SerializedName("payment_details") var paymentDetails: String? = null,
    @SerializedName("extra") var extra: String? = null,
    @SerializedName("meter_reading_photo") var file: String? = null
)
