package com.ertohru.pthabgsm.api.response

import com.ertohru.pthabgsm.api.model.UserUserDetailResponse

data class UserDetailResponse(

    var error:Boolean?,
    var pesan:String?,
    var user:UserUserDetailResponse

)