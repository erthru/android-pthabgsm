package com.ertohru.pthabgsm.api.model

data class UserLoginResponse(
    var user_id:Int?,
    var user_email:String?,
    var user_nama_lengkap:String?,
    var user_alamat:String?,
    var user_no_hp:String?
)