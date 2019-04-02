package com.ertohru.pthabgsm.api.response

import com.ertohru.pthabgsm.api.model.BookingItemDaftarBookingItemResponse

data class DaftarBookingItemResponse(

    var error:Boolean?,
    var pesan:String?,
    var booking_item:ArrayList<BookingItemDaftarBookingItemResponse>

)