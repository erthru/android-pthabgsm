package com.ertohru.pthabgsm.ui.teknisisetsparepart

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
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
import com.ertohru.pthabgsm.utils.Loading
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_teknisi_set_sparepart)

        supportActionBar?.hide()

        val i = intent
        jenis = i.getStringExtra("jenis")

        btnBackTSS.setOnClickListener { finish() }
        btnPilihTSS.setOnClickListener { loadSparepart() }
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

    data class SelectedSparepart (
        val id: String,
        val nama: String,
        val harga: String
    )
}
