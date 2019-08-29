package com.ertohru.pthabgsm.ui.teknisiservisdetail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.ertohru.pthabgsm.R
import com.ertohru.pthabgsm.utils.StringUtils
import kotlinx.android.synthetic.main.activity_pesanan_detail.*
import kotlinx.android.synthetic.main.activity_teknisi_servis_detail.*

class TeknisiServisDetailActivity : AppCompatActivity() {

    lateinit var bookingId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_teknisi_servis_detail)

        supportActionBar?.hide()

        val i = intent
        bookingId = i.getStringExtra("booking_id")

        tvInvoiceTSD.text = i.getStringExtra("booking_id")
        tvJenisTSD.text = i.getStringExtra("booking_jenis_servis")
        tvModelKendaraanTSD.text = i.getStringExtra("booking_model_kendaraan")
        tvVincodeTSD.text = i.getStringExtra("booking_vincode")
        tvKMTSD.text = i.getStringExtra("booking_km")
        tvNoPolisiTSD.text = i.getStringExtra("booking_no_polisi")
        tvJadwalServisTSD.text = StringUtils.dateMinDetail(i.getStringExtra("booking_jadwal_servis"))
        tvKeteranganTSD.text = i.getStringExtra("booking_keterangan")
        tvTglTSD.text = StringUtils.dateLengkap(i.getStringExtra("booking_created_at"))
        tvNamaTSD.text = i.getStringExtra("user_nama_lengkap")
        tvAlamatTSD.text = i.getStringExtra("user_alamat")

        val lastStatus = i.getStringExtra("last_status")

        when(lastStatus){
            "DITERIMA" -> {
                tvKeteranganDetailTSD.text = "Menunggu pemilihan sparepart"
            }
            "PEMILIHAN PART" -> {
                tvKeteranganDetailTSD.text = "Menunggu user mengkonfirmasi sparepart yang telah ditentukan"
                btnViewSparePartTSD.text = "LIHAT SPAREPART YANG TELAH DITENTUKAN"
            }
            "MENUNGGU PERSETUJUAN" -> {
                tvKeteranganDetailTSD.text = "User telah menentukan sparepart yang ingin digunakan"
                btnViewSparePartTSD.text = "LIHAT SPAREPART YANG TELAH DITENTUKAN"
            }
            "DALAM PENGERJAAN" -> {
                tvKeteranganDetailTSD.text = "Servis ini sedang dalam pengerjaan"
                btnViewSparePartTSD.text = "LIHAT SPAREPART YANG TELAH DITENTUKAN"
                btnTolakTSD.visibility = View.GONE
            }
        }

        btnBackTSD.setOnClickListener {
            finish()
        }

    }
}
