package com.ertohru.pthabgsm.ui.viewservispart

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.ertohru.pthabgsm.R
import com.ertohru.pthabgsm.api.Client
import com.ertohru.pthabgsm.api.Support
import com.ertohru.pthabgsm.api.response.DaftarBookingItemResponse
import com.ertohru.pthabgsm.api.response.MenungguPersetujuanBookingResponse
import com.ertohru.pthabgsm.ui.main.MainActivity
import com.ertohru.pthabgsm.utils.Loading
import com.ertohru.pthabgsm.utils.StringUtils
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.activity_view_servis_part.*
import retrofit2.Call
import retrofit2.Response

class ViewServisPartActivity : AppCompatActivity() {

    var bookingId:Int? = null
    var biaya = 0

    companion object {
        var ON_SELECTED = false
        var UNSELECTED_ITEM = ArrayList<String>()
        var TOTAL_HARGA = 0
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_servis_part)

        supportActionBar?.hide()

        registerReceiver(bc, IntentFilter("VSP_TOTAL"))

        handleView()

    }

    override fun onDestroy() {
        super.onDestroy()
        ON_SELECTED = false
        UNSELECTED_ITEM.clear()
        TOTAL_HARGA = 0

    }

    override fun onResume(){
        super.onResume()
    }

    fun handleView(){

        btnSetVSP.visibility = View.GONE

        btnBackVSP.setOnClickListener { this.finish() }

        val i = intent

        bookingId = Integer.parseInt(i.getStringExtra("booking_id"))

        if(i.hasExtra("onSelected")){
            btnSetVSP.visibility = View.VISIBLE
            ON_SELECTED = true
        }

        biaya = i.getStringExtra("booking_biaya").toInt()


        rvVSP.setHasFixedSize(true)
        rvVSP.layoutManager = LinearLayoutManager(this)
        rvVSP.addItemDecoration(DividerItemDecoration(applicationContext,DividerItemDecoration.VERTICAL))

        loadItem()

        btnSetVSP.setOnClickListener {

            setItem()

        }

        btnRecreateVSP.setOnClickListener { recreate() }

    }

    private fun loadItem(){

        rvVSP.adapter = null
        pbVSP.visibility = View.VISIBLE

        Client().prepare(Support.API_PTHABGSM).daftarBookingItem(bookingId!!)
            .enqueue(object : retrofit2.Callback<DaftarBookingItemResponse>{

                override fun onFailure(call: Call<DaftarBookingItemResponse>, t: Throwable) {
                    Log.d("ONFAILURE",t.toString())
                    pbVSP.visibility = View.GONE
                    Toasty.error(applicationContext,"Koneksi internet gagal.",Toasty.LENGTH_SHORT).show()
                    finish()
                }

                override fun onResponse(
                    call: Call<DaftarBookingItemResponse>,
                    response: Response<DaftarBookingItemResponse>
                ) {

                    if(response.isSuccessful){

                        pbVSP.visibility = View.GONE

                        val adapter = ViewServisPartRecyclerView(applicationContext,response.body()?.booking_item)
                        adapter.notifyDataSetChanged()
                        rvVSP.adapter = adapter

                    }

                }

            })

    }

    private fun setItem(){

        val loading = Loading(this)
        loading.show("Proses...")

        var unselectedItem = ""

        if(UNSELECTED_ITEM.size > 0){
            for(i in 0 until UNSELECTED_ITEM.size){
                unselectedItem += UNSELECTED_ITEM.get(i)+","
            }
        }

        Client().prepare(Support.API_PTHABGSM).menungguPersetujuanBooking(bookingId!!,unselectedItem)
            .enqueue(object : retrofit2.Callback<MenungguPersetujuanBookingResponse>{
                override fun onFailure(call: Call<MenungguPersetujuanBookingResponse>, t: Throwable) {
                    Log.d("ONFAILURE",t.toString())
                    loading.dismiss()
                    Toasty.error(applicationContext,"Koneksi internet gagal.",Toasty.LENGTH_SHORT).show()
                }

                override fun onResponse(
                    call: Call<MenungguPersetujuanBookingResponse>,
                    response: Response<MenungguPersetujuanBookingResponse>
                ) {

                    if(response.isSuccessful){

                        loading.dismiss()

                        if(!response.body()?.error!!){

                            Toasty.success(applicationContext,response.body()?.pesan!!,Toasty.LENGTH_SHORT).show()
                            finish()
                            startActivity(Intent(applicationContext,MainActivity::class.java))

                        }else{
                            Toasty.error(applicationContext,response.body()?.pesan!!,Toasty.LENGTH_SHORT).show()
                        }

                    }

                }

            })

    }

    val bc = object : BroadcastReceiver(){

        override fun onReceive(p0: Context?, p1: Intent?) {

            val total = TOTAL_HARGA+biaya

            tvTotalVSP.text = "Total: Rp. "+ StringUtils.rupiah(total.toString()) +" (Termasuk biaya servis: Rp. "+StringUtils.rupiah(biaya.toString())+")"
        }

    }

}
