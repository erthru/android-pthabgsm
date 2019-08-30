package com.ertohru.pthabgsm.ui.teknisiservisdetail

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.afollestad.materialdialogs.DialogCallback
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.ertohru.pthabgsm.R
import com.ertohru.pthabgsm.api.Client
import com.ertohru.pthabgsm.api.Support
import com.ertohru.pthabgsm.api.model.AlasanDitolakResponse
import com.ertohru.pthabgsm.api.model.DitolakBookingResponse
import com.ertohru.pthabgsm.ui.teknisisetsparepart.TeknisiSetSparepartActivity
import com.ertohru.pthabgsm.utils.Loading
import com.ertohru.pthabgsm.utils.StringUtils
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.activity_pesanan_detail.*
import kotlinx.android.synthetic.main.activity_teknisi_servis_detail.*
import kotlinx.android.synthetic.main.dialog_tolak_teknisi.view.*
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit

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

        btnTolakTSD.setOnClickListener {
            val dView = layoutInflater.inflate(R.layout.dialog_tolak_teknisi,null,false)

            val dialog = MaterialDialog(this)
            dialog.customView(null,dView,false,false)
            dialog.positiveButton(null,"TOLAK") {
                if(dView.edAlasanDTT.text.isNullOrEmpty()){
                    Toasty.error(applicationContext, "Alasan harus diisi", Toasty.LENGTH_SHORT).show()
                }else{
                    tolakPesanan(dView.edAlasanDTT.text.toString())
                }
            }
            dialog.negativeButton (null,"BATAL") {
                dialog.dismiss()
            }
            dialog.show()
        }

        btnViewSparePartTSD.setOnClickListener {
            if (btnViewSparePartTSD.text == "SET SPAREPART"){
                startActivity(Intent(applicationContext, TeknisiSetSparepartActivity::class.java)
                    .putExtra("jenis", i.getStringExtra("booking_jenis_servis"))
                    .putExtra("booking_id",bookingId)
                )
            }else{

            }
        }

    }

    private fun tolakPesanan(alasan: String){

        val loading = Loading(this)
        loading.show("Memproses...")

        Client().prepare(Support.API_PTHABGSM).ditolakBooking(bookingId)
            .enqueue(object: retrofit2.Callback<DitolakBookingResponse>{

                override fun onFailure(call: Call<DitolakBookingResponse>, t: Throwable) {
                    loading.dismiss()
                    Toasty.error(applicationContext, "Koneksi gagal.", Toasty.LENGTH_SHORT).show()
                }

                override fun onResponse(
                    call: Call<DitolakBookingResponse>,
                    response: Response<DitolakBookingResponse>
                ) {
                    if (response.isSuccessful){
                        loading.dismiss()
                        setAlasanDitolak(alasan)
                    }
                }

            })

    }

    private fun setAlasanDitolak(alasan: String){
        val loading = Loading(this)
        loading.show("Menolak Pesanan...")

        Client().prepare(Support.API_PTHABGSM).setAlasanBookingDitolak(bookingId, alasan)
            .enqueue(object: retrofit2.Callback<AlasanDitolakResponse>{

                override fun onFailure(call: Call<AlasanDitolakResponse>, t: Throwable) {
                    loading.dismiss()
                    Toasty.error(applicationContext, "Koneksi gagal.", Toasty.LENGTH_SHORT).show()
                }

                override fun onResponse(
                    call: Call<AlasanDitolakResponse>,
                    response: Response<AlasanDitolakResponse>
                ) {
                    if (response.isSuccessful){
                        loading.dismiss()

                        Toasty.success(applicationContext, response.body()?.pesan!!, Toasty.LENGTH_SHORT).show()
                        finish()
                    }
                }

            })
    }
}
