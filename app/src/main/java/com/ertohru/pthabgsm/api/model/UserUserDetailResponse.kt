package com.ertohru.pthabgsm.api.model

data class UserUserDetailResponse(
    var user_id:Int?,
    var user_nama_lengkap:String?,
    var user_alamat:String?,
    var user_no_hp:String?,
    var user_created_at:String?,
    var user_updated_at:String?,
    var login_email:String?
)