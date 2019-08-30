package com.ertohru.pthabgsm.ui.teknisilihatsparepart

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.ertohru.pthabgsm.R
import com.ertohru.pthabgsm.api.Client
import com.ertohru.pthabgsm.api.Support
import com.ertohru.pthabgsm.api.model.DalamPengerjaanBookingResponse
import com.ertohru.pthabgsm.api.response.DaftarBookingItemResponse
import com.ertohru.pthabgsm.api.response.SetTeknisiReponse
import com.ertohru.pthabgsm.ui.teknisimain.TeknisiMainActivity
import com.ertohru.pthabgsm.utils.Loading
import com.ertohru.pthabgsm.utils.StringUtils
import com.ertohru.pthabgsm.utils.sharedpref.SessionTeknisi
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.activity_teknisi_lihat_sparepart.*
import retrofit2.Call
import retrofit2.Response

class TeknisiLihatSparepartActivity : AppCompatActivity() {

    private var needConfirmation = false
    private var bookingId = ""
    private var bookingBiaya = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_teknisi_lihat_sparepart)

        supportActionBar?.hide()

        val i = intent
        needConfirmation = i.getStringExtra("need_confirmation").toBoolean()
        bookingId = i.getStringExtra("booking_id")
        bookingBiaya = i.getStringExtra("booking_biaya")

        Log.d("NEED_CONFIRMATION", needConfirmation.toString())

        if (!needConfirmation){
            cardTidakDipilihTLS.visibility = View.GONE
            btnSetujuiTLS.visibility = View.GONE
        }

        btnBackTLS.setOnClickListener { finish() }
        btnSetujuiTLS.setOnClickListener {
            setujui()
        }
    }

    override fun onResume() {
        super.onResume()
        loadItem()
    }

    private fun loadItem(){

        val loading = Loading(this)
        loading.show("Memuat data...")

        Client().prepare(Support.API_PTHABGSM).daftarBookingItem(bookingId.toInt())
            .enqueue(object: retrofit2.Callback<DaftarBookingItemResponse>{

                override fun onFailure(call: Call<DaftarBookingItemResponse>, t: Throwable) {
                    loading.dismiss()
                    Toasty.error(applicationContext, "Koneksi gagal.", Toasty.LENGTH_SHORT).show()
                    finish()
                }

                override fun onResponse(
                    call: Call<DaftarBookingItemResponse>,
                    response: Response<DaftarBookingItemResponse>
                ) {

                    if (response.isSuccessful){

                        loading.dismiss()

                        var dipilih = ""
                        var total = 0

                        for (selected in response?.body()?.booking_item!!){
                            dipilih += "${selected.barang_servis_nama} (Rp. ${StringUtils.rupiah(selected.barang_servis_harga.toString())}) \n"
                            total += selected.barang_servis_harga!!
                        }

                        total += bookingBiaya.toInt()

                        dipilih += "\n\nTotal : Rp."+StringUtils.rupiah(total.toString())+" (Termasuk biaya servis: Rp."+StringUtils.rupiah(bookingBiaya)+")"

                        var tidakDipilih = ""

                        for (selected in response?.body()?.booking_item_ditolak!!){
                            tidakDipilih += "${selected.barang_servis_nama} \nRp. ${StringUtils.rupiah(selected.barang_servis_harga.toString())}"
                        }

                        if (tidakDipilih == ""){
                            tidakDipilih += "-"
                        }

                        tvDipilihTLS.text = dipilih
                        tvTidakDipilih.text = tidakDipilih

                    }

                }

            })

    }

    private fun setujui(){

        val loading = Loading(this)
        loading.show("Memuat data...")

        Client().prepare(Support.API_PTHABGSM).dalamPengerjaanBooking(bookingId)
            .enqueue(object: retrofit2.Callback<DalamPengerjaanBookingResponse>{

                override fun onFailure(call: Call<DalamPengerjaanBookingResponse>, t: Throwable) {
                    loading.dismiss()
                    Toasty.error(applicationContext, "Koneksi gagal.", Toasty.LENGTH_SHORT).show()
                }

                override fun onResponse(
                    call: Call<DalamPengerjaanBookingResponse>,
                    response: Response<DalamPengerjaanBookingResponse>
                ) {

                    if (response.isSuccessful){
                        loading.dismiss()

                        setTeknisi()
                    }

                }

            })

    }

    private fun setTeknisi(){

        val loading = Loading(this)
        loading.show("Mengkonfirmasi teknisi...")

        Client().prepare(Support.API_PTHABGSM).setTeknisi(bookingId, SessionTeknisi(this).teknisiId().toString())
            .enqueue(object: retrofit2.Callback<SetTeknisiReponse>{
                override fun onFailure(call: Call<SetTeknisiReponse>, t: Throwable) {
                    loading.dismiss()
                }

                override fun onResponse(call: Call<SetTeknisiReponse>, response: Response<SetTeknisiReponse>) {

                    if (response.isSuccessful){

                        loading.dismiss()
                        Toasty.success(applicationContext, response?.body()?.message!!, Toasty.LENGTH_SHORT).show()

                        finish()

                        startActivity(Intent(applicationContext, TeknisiMainActivity::class.java))

                    }

                }

            })

    }

}
