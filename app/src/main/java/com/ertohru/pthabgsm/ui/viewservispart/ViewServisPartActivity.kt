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
import com.ertohru.pthabgsm.utils.StringUtils
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.activity_view_servis_part.*
import retrofit2.Call
import retrofit2.Response

class ViewServisPartActivity : AppCompatActivity() {

    var bookingId:Int? = null

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


        rvVSP.setHasFixedSize(true)
        rvVSP.layoutManager = LinearLayoutManager(this)
        rvVSP.addItemDecoration(DividerItemDecoration(applicationContext,DividerItemDecoration.VERTICAL))

        loadItem()

        btnSetVSP.setOnClickListener {



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

    val bc = object : BroadcastReceiver(){

        override fun onReceive(p0: Context?, p1: Intent?) {
            tvTotalVSP.text = "Total: Rp. "+ StringUtils.rupiah(TOTAL_HARGA.toString())
        }

    }

}
