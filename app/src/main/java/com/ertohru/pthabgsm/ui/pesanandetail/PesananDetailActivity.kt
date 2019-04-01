package com.ertohru.pthabgsm.ui.pesanandetail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.ertohru.pthabgsm.R
import com.ertohru.pthabgsm.utils.StringUtils
import kotlinx.android.synthetic.main.activity_pesanan_detail.*

class PesananDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pesanan_detail)

        supportActionBar?.hide()

        handleView()

    }

    private fun handleView(){

        btnBackPD.setOnClickListener { this.finish() }

        val i = intent

        tvInvoicePD.text = "Invoice Id: #"+i.getStringExtra("booking_id")
        tvJenisPD.text = "Jenis Servis: "+i.getStringExtra("booking_jenis_servis")
        tvKeteranganPD.text = "Keterangan: "+i.getStringExtra("booking_keterangan")
        tvTglPD.text = "Tgl: "+StringUtils.dateLengkap(i.getStringExtra("booking_created_at"))
        tvNamaPD.text = "Nama: "+i.getStringExtra("user_nama_lengkap")
        tvAlamatPD.text = "Alamat: "+i.getStringExtra("user_alamat")
        tvDealerPD.text = "Dealer: "+i.getStringExtra("dealer_nama")

        val lastStatus = i.getStringExtra("last_status")

        if(lastStatus == "BELUM DIPROSES"){
            tvKeteranganDetailPD.text = "* Pesanan anda belum diproses."
        }else if(lastStatus == "DITERIMA"){
            tvKeteranganDetailPD.text = "* Pesanan anda telah diterima silahkan antarkan mobil anda ke "+i.getStringExtra("dealer_alamat")
        }else if(lastStatus == "PEMILIHAN PART"){
            tvKeteranganDetailPD.text = "* Daftar sparepart/servis telah ditentukan oleh dealer. Tap tombol di bawah untuk melihat daftar."
            btnViewPartPD.visibility = View.VISIBLE
        }else if(lastStatus == "MENUNGGU PERSETUJUAN"){
            tvKeteranganDetailPD.text = "* Pemilihan part servis telah ditentukan. Menunggu konfirmasi dari pihak dealer. Tap tombol dibawah untuk melihat daftar yang telah dipilih"
            btnViewPartPD.visibility = View.VISIBLE
        }else if(lastStatus == "DALAM PENGERJAAN"){
            tvKeteranganDetailPD.text = "* Pesanan servis anda dalam tahap pengerjaan."
            btnViewPartPD.visibility = View.VISIBLE
        }

    }

}
