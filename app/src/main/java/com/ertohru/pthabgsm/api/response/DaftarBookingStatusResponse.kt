package com.ertohru.pthabgsm.api.response

import com.ertohru.pthabgsm.api.model.BookingStatusDaftarBookingStatusResponse

data class DaftarBookingStatusResponse(

    var error:Boolean?,
    var pesan:String?,
    var booking_status:ArrayList<BookingStatusDaftarBookingStatusResponse>

)