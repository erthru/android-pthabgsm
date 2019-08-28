package com.ertohru.pthabgsm.api.response

data class LoginTeknisiResponse(
    val error: Boolean,
    val message: String,
    val teknisi_id: Int
)