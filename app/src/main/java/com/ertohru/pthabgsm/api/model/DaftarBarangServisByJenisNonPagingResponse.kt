package com.ertohru.pthabgsm.api.model

data class DaftarBarangServisByJenisNonPagingResponse (
    val error: Boolean,
    val pesan: String,
    val total: String,
    val daftar_barang_servis: ArrayList<DaftarBarangServis>
)

data class DaftarBarangServis(
    val barang_servis_id: String,
    val barang_servis_nama: String,
    val barang_servis_harga: String,
    val barang_servis_kategori: String,
    val barang_servis_created_at: String,
    val barang_servis_updated_at: String
)