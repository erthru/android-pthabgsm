package com.ertohru.pthabgsm.api.response

import com.ertohru.pthabgsm.api.model.DataBookingUserDaftarBookingUserResponse

data class DaftarBookingUserResponse(

    var error:Boolean?,
    var pesan:String?,
    var data_booking_user:ArrayList<DataBookingUserDaftarBookingUserResponse>

)