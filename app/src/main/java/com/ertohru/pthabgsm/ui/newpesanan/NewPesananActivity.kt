package com.ertohru.pthabgsm.ui.newpesanan

import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
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
import java.util.*

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
               R.id.radioPDSINP -> jenis = "PDS/I"
               R.id.radioSBINP -> jenis = "SBI"
               R.id.radioSBENP -> jenis = "SBE"
               R.id.radioTWCNP -> jenis = "TWC"
               R.id.radioRTJNP -> jenis = "RTJ"
               R.id.radioGPNP -> jenis = "GR"
            }
        }

        btnBuatNP.setOnClickListener {

            if(jenis == null || edKeteranganNP.text.toString().isNullOrEmpty() || edModelKendaraanNP.text.toString().isNullOrEmpty() || edVincodePN.text.toString().isNullOrEmpty() || edKMPN.text.toString().isNullOrEmpty() || edNoPolisiPN.text.toString().isNullOrEmpty()){
                Toasty.error(applicationContext,"Lengkapi data",Toasty.LENGTH_SHORT).show()
            }else{
                createPesanan(jenis!!)
            }

        }

        tvInformasiJenisServisNP.setOnClickListener {

            val dialogView = layoutInflater.inflate(R.layout.dialog_lihat_informasi_new_pesanan,null,false)
            val dialog = MaterialDialog(this)
            dialog.setTitle("Jenis Servis")
            dialog.customView(null, dialogView, false, true)
            dialog.cancelable(true)
            dialog.positiveButton(text = "TUTUP")
            dialog.show()

        }

        edJadwalServisNP.setOnClickListener {

            val calendar = Calendar.getInstance()
            DatePickerDialog(
                this,
                DatePickerDialog.OnDateSetListener { datePicker, y, m, d ->
                    val monthFix = (m+1).toString()

                    val year = y.toString()
                    val month = if (monthFix.length == 1) "0$monthFix" else "$monthFix"
                    val day = if (d.toString().length == 1) "0$d" else "$d"

                    val date = "$year-$month-$day"

                    Log.d("SELECTED_DATE",date)
                    edJadwalServisNP.setText(date)
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()

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
            edJadwalServisNP.text.toString(),
            edKeteranganNP.text.toString())

            .enqueue(object : retrofit2.Callback<StartBookingResponse>{

                override fun onFailure(call: Call<StartBookingResponse>, t: Throwable) {
                    loading.dismiss()
                    Toasty.error(applicationContext,"Koneksi internet gagal.",Toasty.LENGTH_SHORT).show()
                    Log.d("ONFAILURE",t.toString())
                }

                override fun onResponse(call: Call<StartBookingResponse>, response: Response<StartBookingResponse>) {

                    if(response.isSuccessful){

                        if(response.body()?.error!!){
                            loading.dismiss()
                            Toasty.error(applicationContext,response.body()?.pesan!!, Toast.LENGTH_SHORT).show()
                        }else{
                            loading.dismiss()
                            finish()
                            startActivity(Intent(applicationContext,MainActivity::class.java).putExtra("TARGET","SERVIS"))
                            Toasty.success(applicationContext,response.body()?.pesan!!,Toasty.LENGTH_SHORT).show()
                        }

                    }

                }

            })

    }

}
