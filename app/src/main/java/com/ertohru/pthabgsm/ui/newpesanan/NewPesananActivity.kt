package com.ertohru.pthabgsm.ui.newpesanan

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.ertohru.pthabgsm.R
import com.ertohru.pthabgsm.api.Client
import com.ertohru.pthabgsm.api.Support
import com.ertohru.pthabgsm.api.response.StartBookingResponse
import com.ertohru.pthabgsm.ui.main.MainActivity
import com.ertohru.pthabgsm.utils.Loading
import com.ertohru.pthabgsm.utils.sharedpref.SessionUser
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.activity_new_pesanan.*
import retrofit2.Call
import retrofit2.Response

class NewPesananActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_pesanan)

        supportActionBar?.hide()

        handleView()

    }

    private fun handleView(){

        btnBackNP.setOnClickListener { this.finish() }

        var jenis:String? = null

        rgJenisNP.setOnCheckedChangeListener { _, i ->
            when(i){
                R.id.radioBodyNP -> jenis = "BODY"
                R.id.radioGRNP -> jenis = "GR"
                R.id.radioSBINP -> jenis = "SBI"
                R.id.radioSBENP -> jenis = "SBE"
            }
        }

        btnBuatNP.setOnClickListener {

            if(jenis == null || edKeteranganNP.text.toString().isNullOrEmpty() || edModelKendaraanNP.text.toString().isNullOrEmpty() || edVincodePN.text.toString().isNullOrEmpty() || edKMPN.text.toString().isNullOrEmpty() || edNoPolisiPN.text.toString().isNullOrEmpty()){
                Toasty.error(applicationContext,"Lengkapi data",Toasty.LENGTH_SHORT).show()
            }else{
                createPesanan(jenis!!)
            }

        }

    }

    private fun createPesanan(jenis:String){

        val loading = Loading(this)
        loading.show("Proses ...")

        Client().prepare(Support.API_PTHABGSM).startBooking(

            SessionUser(this).userId(),
            1,
            jenis,
            edModelKendaraanNP.text.toString(),
            edVincodePN.text.toString(),
            edKMPN.text.toString(),
            edNoPolisiPN.text.toString(),
            edKeteranganNP.text.toString())

            .enqueue(object : retrofit2.Callback<StartBookingResponse>{

                override fun onFailure(call: Call<StartBookingResponse>, t: Throwable) {
                    loading.dismiss()
                    Toasty.error(applicationContext,"Koneksi internet gagal.",Toasty.LENGTH_SHORT).show()
                    Log.d("ONFAILURE",t.toString())
                }

                override fun onResponse(call: Call<StartBookingResponse>, response: Response<StartBookingResponse>) {

                    if(response.isSuccessful){

                        loading.dismiss()
                        finish()
                        startActivity(Intent(applicationContext,MainActivity::class.java).putExtra("TARGET","SERVIS"))
                        Toasty.success(applicationContext,response.body()?.pesan!!,Toasty.LENGTH_SHORT).show()

                    }

                }

            })

    }

}
