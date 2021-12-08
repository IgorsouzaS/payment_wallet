package com.picpay.model.response

import com.google.gson.annotations.SerializedName

class BankResponse {
    @SerializedName("ispb")
    var ispb: String? = null
    @SerializedName("name")
    var name: String? = null
    @SerializedName("fullName")
    var fullName: String? = null
    @SerializedName("code")
    var code: Int = 0
}
