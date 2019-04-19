package com.ertohru.pthabgsm.api.model

data class  RiwayatBookingUseRiwayatBookingUserDateFilterResponse(

    var booking_id:Int?,
    var booking_jenis_servis:String?,
    var booking_keterangan:String?,
    var booking_biaya:String?,
    var booking_created_at:String?,
    var user_id:Int?,
    var dealer_id:Int?,
    var dealer_nama:String?,
    var dealer_alamat:String?,
    var user_nama_lengkap:String?,
    var user_alamat:String?,
    var user_no_hp:String?,
    var user_created_at:String?,
    var user_updated_at:String?,
    var last_status:String?

)