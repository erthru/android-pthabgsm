package com.ertohru.pthabgsm.api.response

import com.ertohru.pthabgsm.api.model.DataBookingUserDaftarBookingUserResponse
import com.ertohru.pthabgsm.api.model.RiwayatBookingUseRiwayatBookingUserResponse

data class RiwayatBookingUserResponse(

    var error:Boolean?,
    var pesan:String?,
    var riwayat_booking_user:ArrayList<RiwayatBookingUseRiwayatBookingUserResponse>

)