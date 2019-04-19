package com.ertohru.pthabgsm.api.model

data class BookingItemDaftarBookingItemResponse(

    var booking_item_id:Int?,
    var booking_id:Int?,
    var barang_servis_id:Int?,
    var booking_item_status:String?,
    var booking_item_cur_harga:Int?,
    var barang_servis_nama:String?,
    var barang_servis_harga:Int?,
    var barang_servis_kategori:String?,
    var barang_servis_created_at:String?,
    var barang_servis_updated_at:String?

)