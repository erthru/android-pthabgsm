package com.ertohru.pthabgsm.api.model

data class ListBookingTeknisiResponse(
    val error: Boolean,
    val total: Int,
    val bookings: ArrayList<Booking>
)

data class Booking(
    val booking_id: String,
    val booking_jenis_servis: String,
    val booking_model_kendaraan: String,
    val booking_vincode: String,
    val booking_km: String,
    val booking_no_polisi: String,
    val booking_jadwal_servis: String,
    val booking_keterangan: String,
    val booking_biaya: String,
    val booking_created_at: String,
    val user_id: String,
    val dealer_id: String,
    val teknisi_id: String,
    val user_nama_lengkap: String,
    val user_alamat: String,
    val user_no_hp: String,
    val user_created_at: String,
    val user_updated_at: String,
    val last_status: String
)