package com.ertohru.pthabgsm.ui.pesanandetail

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.ertohru.pthabgsm.R
import com.ertohru.pthabgsm.api.Client
import com.ertohru.pthabgsm.api.Support
import com.ertohru.pthabgsm.api.response.DaftarBookingStatusResponse
import com.ertohru.pthabgsm.ui.viewservispart.ViewServisPartActivity
import com.ertohru.pthabgsm.utils.StringUtils
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.activity_pesanan_detail.*
import retrofit2.Call
import retrofit2.Response

class PesananDetailActivity : AppCompatActivity() {

    var bookingId:Int? = null
    var onSelected = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pesanan_detail)

        supportActionBar?.hide()

        handleView()

    }

    override fun onResume() {
        super.onResume()
        loadStatus()
    }

    private fun handleView(){

        btnBackPD.setOnClickListener { this.finish() }

        rvStatusPD.setHasFixedSize(true)
        rvStatusPD.layoutManager = LinearLayoutManager(this)

        val i = intent

        bookingId = Integer.parseInt(i.getStringExtra("booking_id"))

        tvInvoicePD.text = i.getStringExtra("booking_id")
        tvJenisPD.text = i.getStringExtra("booking_jenis_servis")
        tvKeteranganPD.text = i.getStringExtra("booking_keterangan")
        tvTglPD.text = StringUtils.dateLengkap(i.getStringExtra("booking_created_at"))
        tvNamaPD.text = i.getStringExtra("user_nama_lengkap")
        tvAlamatPD.text = i.getStringExtra("user_alamat")
        tvDealerPD.text = i.getStringExtra("dealer_nama")

        val lastStatus = i.getStringExtra("last_status")

        if(lastStatus == "BELUM DIPROSES"){
            tvKeteranganDetailPD.text = "* Pesanan anda belum diproses."
        }else if(lastStatus == "DITERIMA"){
            tvKeteranganDetailPD.text = "* Pesanan anda telah diterima silahkan antarkan mobil anda ke "+i.getStringExtra("dealer_alamat")
        }else if(lastStatus == "PEMILIHAN PART"){
            tvKeteranganDetailPD.text = "* Daftar sparepart/servis telah ditentukan oleh dealer. Tap tombol di bawah untuk melihat daftar dan estimasi harga."
            btnViewPartPD.visibility = View.VISIBLE
            onSelected = true
        }else if(lastStatus == "MENUNGGU PERSETUJUAN"){
            tvKeteranganDetailPD.text = "* Pemilihan part servis telah ditentukan. Menunggu konfirmasi dari pihak dealer. Tap tombol dibawah untuk melihat daftar yang telah dipilih"
            btnViewPartPD.visibility = View.VISIBLE
        }else if(lastStatus == "DALAM PENGERJAAN"){
            tvKeteranganDetailPD.text = "* Pesanan servis anda dalam tahap pengerjaan."
            btnViewPartPD.visibility = View.VISIBLE
        }

        btnViewPartPD.setOnClickListener {

            val ss = Intent(applicationContext,ViewServisPartActivity::class.java)
            ss.putExtra("booking_id",bookingId!!.toString())

            if(onSelected)
                ss.putExtra("onSelected","1")

            startActivity(ss)

        }

    }

    private fun loadStatus(){

        rvStatusPD.adapter = null
        pbStatusPD.visibility = View.VISIBLE

        Client().prepare(Support.API_PTHABGSM).daftarBookingStatus(bookingId!!)

            .enqueue(object : retrofit2.Callback<DaftarBookingStatusResponse>{
                override fun onFailure(call: Call<DaftarBookingStatusResponse>, t: Throwable) {
                    Log.d("ONFAILURE", t.toString())
                    pbStatusPD.visibility = View.GONE
                    Toasty.error(applicationContext,"Koneksi internet gagal.",Toasty.LENGTH_SHORT).show()
                    finish()
                }

                override fun onResponse(
                    call: Call<DaftarBookingStatusResponse>,
                    response: Response<DaftarBookingStatusResponse>
                ) {

                    if(response.isSuccessful){

                        pbStatusPD.visibility = View.GONE

                        val adapter = StatusPesananDetailRecyclerView(applicationContext,response.body()?.booking_status)
                        adapter.notifyDataSetChanged()
                        rvStatusPD.adapter = adapter

                    }

                }

            })

    }

}
