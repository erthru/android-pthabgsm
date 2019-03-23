package com.ertohru.pthabgsm.api.response

import com.ertohru.pthabgsm.api.model.UserLoginResponse

data class LoginResponse(

    var error:Boolean?,
    var pesan:String?,
    var user:UserLoginResponse?

)