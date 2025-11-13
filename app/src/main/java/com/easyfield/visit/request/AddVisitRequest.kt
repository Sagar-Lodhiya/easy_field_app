package com.easyfield.visit.request

import com.google.gson.annotations.SerializedName

data class AddVisitRequest(

    @SerializedName("created_at") var created_at: String? = null,
    @SerializedName("time") var time: String? = null,
    @SerializedName("place") var place: String? = null,
    @SerializedName("party_id") var partyId: String? = null,
    @SerializedName("duration") var duration: String? = null,
    @SerializedName("discussion_point") var discussionPoint: String? = null,
    @SerializedName("remark") var remark: String? = null,
    @SerializedName("latitude") var lat: Double? = null,
    @SerializedName("longitude") var long: Double? = null,
)
