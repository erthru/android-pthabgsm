package com.ertohru.pthabgsm.ui.teknisisetsparepart

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.ertohru.pthabgsm.R
import com.ertohru.pthabgsm.api.Client
import com.ertohru.pthabgsm.api.Support
import com.ertohru.pthabgsm.api.model.DaftarBarangServis
import com.ertohru.pthabgsm.api.model.DaftarBarangServisByJenisNonPagingResponse
import com.ertohru.pthabgsm.api.model.PemilihanPartBookingReponse
import com.ertohru.pthabgsm.ui.teknisimain.TeknisiMainActivity
import com.ertohru.pthabgsm.utils.Loading
import com.ertohru.pthabgsm.utils.StringUtils
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.activity_teknisi_set_sparepart.*
import kotlinx.android.synthetic.main.dialog_select_sparepart.view.*
import retrofit2.Call
import retrofit2.Response

class TeknisiSetSparepartActivity : AppCompatActivity() {

    companion object{
        var SELECTED_SPAREPART = ArrayList<SelectedSparepart>()
    }

    private var jenis = ""
    private var bookingId = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_teknisi_set_sparepart)

        supportActionBar?.hide()

        val i = intent
        jenis = i.getStringExtra("jenis")
        bookingId = i.getStringExtra("booking_id")

        btnBackTSS.setOnClickListener { finish() }
        btnPilihTSS.setOnClickListener { loadSparepart() }
        btnResetTSS.setOnClickListener {
            tvSparepartTSS.text = "Belum ada sparepart yang dipilih"
            SELECTED_SPAREPART.clear()
            btnPilihTSS.visibility = View.VISIBLE
            btnResetTSS.visibility = View.GONE
            cardProsesTSS.visibility = View.GONE
        }

        btnJumlah.setOnClickListener {
            if (edHargaServisTSS.text.isNullOrEmpty()){
                Toasty.error(applicationContext,"Input harga servis", Toasty.LENGTH_SHORT).show()
            }else{

                var selectedTotal = 0

                for (selected in SELECTED_SPAREPART){
                    selectedTotal += selected.harga.toInt()
                }

                selectedTotal += edHargaServisTSS.text.toString().toInt()

                tvTotalTSS.text = "Total : Rp."+StringUtils.rupiah(selectedTotal.toString())

            }
        }

        btnProsesTSS.setOnClickListener {
            if (tvTotalTSS.text.toString() == "Total : Rp.0"){
                Toasty.error(applicationContext,"Silahkan jumlah terlebih dahulu", Toasty.LENGTH_SHORT).show()
            }else{
                proses()
            }
        }
    }

    private fun loadSparepart(){

        val loading = Loading(this)
        loading.show("Memuat sparepart ...")

        Client().prepare(Support.API_PTHABGSM).daftarBarangServisByJenisNonPaging(jenis)
            .enqueue(object: retrofit2.Callback<DaftarBarangServisByJenisNonPagingResponse>{

                override fun onFailure(call: Call<DaftarBarangServisByJenisNonPagingResponse>, t: Throwable) {
                    loading.dismiss()
                    Toasty.error(applicationContext, "Koneksi gagal.", Toasty.LENGTH_SHORT).show()
                }

                override fun onResponse(
                    call: Call<DaftarBarangServisByJenisNonPagingResponse>,
                    response: Response<DaftarBarangServisByJenisNonPagingResponse>
                ) {

                    if (response.isSuccessful){
                        loading.dismiss()

                        Log.d("RESPONSE", response?.body().toString())

                        val dView = layoutInflater.inflate(R.layout.dialog_select_sparepart, null, false)
                        dView.rvDSS.setHasFixedSize(true)
                        dView.rvDSS.layoutManager = LinearLayoutManager(applicationContext)

                        dView.rvDSS.adapter = TeknisiSetSparepartRecyclerView(response?.body()?.daftar_barang_servis)
                        dView.rvDSS.addItemDecoration(DividerItemDecoration(applicationContext, DividerItemDecoration.VERTICAL))

                        val dialog = MaterialDialog(this@TeknisiSetSparepartActivity)
                        dialog.customView(null, dView, true, false)

                        dialog.positiveButton(null, "SELESAI"){
                            if (SELECTED_SPAREPART.size == 0){
                                Toasty.error(applicationContext, "Sparepart tidak ada yang dipilih", Toasty.LENGTH_SHORT).show()
                            }else{
                                btnPilihTSS.visibility = View.GONE
                                btnResetTSS.visibility = View.VISIBLE
                                cardProsesTSS.visibility = View.VISIBLE

                                var sparepart = ""

                                for(selected in SELECTED_SPAREPART){
                                    sparepart += selected.nama+" (Rp. ${StringUtils.rupiah(selected.harga)})\n"
                                }

                                tvSparepartTSS.text = sparepart
                            }
                        }
                        dialog.negativeButton(null, "BATAL"){
                            dialog.dismiss()
                        }
                        dialog.show()

                    }

                }

            })
    }

    private fun proses(){

        val loading = Loading(this)
        loading.show("Memproses ...")

        var selectedSparepartToProcess = ""

        for (selected in SELECTED_SPAREPART){
            selectedSparepartToProcess += selected.id+","
        }

        Client().prepare(Support.API_PTHABGSM).pemilihanPartBooking(bookingId, selectedSparepartToProcess, edHargaServisTSS.text.toString())
            .enqueue(object: retrofit2.Callback<PemilihanPartBookingReponse>{

                override fun onFailure(call: Call<PemilihanPartBookingReponse>, t: Throwable) {
                    loading.dismiss()
                    Toasty.error(applicationContext, "Koneksi gagal.", Toasty.LENGTH_SHORT).show()
                }

                override fun onResponse(
                    call: Call<PemilihanPartBookingReponse>,
                    response: Response<PemilihanPartBookingReponse>
                ) {

                    if (response.isSuccessful){

                        loading.dismiss()

                        Toasty.success(applicationContext, response?.body()?.pesan!!, Toasty.LENGTH_SHORT).show()

                        finish()

                        startActivity(Intent(applicationContext, TeknisiMainActivity::class.java))

                    }

                }

            })

    }

    override fun onDestroy() {
        super.onDestroy()
        SELECTED_SPAREPART.clear()
    }

    data class SelectedSparepart (
        val id: String,
        val nama: String,
        val harga: String
    )
}
