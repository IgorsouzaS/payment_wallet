package com.picpay.service

import com.picpay.model.response.BankResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface BankService {
    @GET("{code}")
    fun getBankData(@Path("code") lon: String): Call<BankResponse>
}
