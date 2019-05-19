package com.ertohru.pthabgsm.ui.riwayatdatefilter

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ertohru.pthabgsm.R
import com.ertohru.pthabgsm.api.Client
import com.ertohru.pthabgsm.api.Support
import com.ertohru.pthabgsm.api.model.RiwayatBookingUseRiwayatBookingUserDateFilterResponse
import com.ertohru.pthabgsm.api.model.RiwayatBookingUseRiwayatBookingUserResponse
import com.ertohru.pthabgsm.api.response.RiwayatBookingUserResponse
import com.ertohru.pthabgsm.api.response.RiwayatDateFilterBookingUserResponse
import com.ertohru.pthabgsm.ui.riwayat.RiwayatRecyclerViewAdapter
import com.ertohru.pthabgsm.utils.sharedpref.SessionUser
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.activity_riwayat_date_filter.*
import kotlinx.android.synthetic.main.fragment_riwayat.view.*
import retrofit2.Call
import retrofit2.Response
import java.util.ArrayList

class RiwayatDateFilterActivity : AppCompatActivity() {

    lateinit var v: View
    var page = 1
    lateinit var parcelable: Parcelable
    var riwayatDateFilterListServis = ArrayList<RiwayatBookingUseRiwayatBookingUserDateFilterResponse>()
    var allData = false

    var dateB = ""
    var dateA = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_riwayat_date_filter)

        supportActionBar?.hide()

        handleView()

    }

    private fun handleView(){

        rvRDF.setHasFixedSize(true)
        rvRDF.layoutManager = LinearLayoutManager(this)

        btnBackRDF.setOnClickListener { this.finish() }

        rvRDF.addOnScrollListener(object : RecyclerView.OnScrollListener(){

            var directiorDown:Boolean = false

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                directiorDown = dy > 0
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {

                if (recyclerView.canScrollVertically(1).not()
                    && newState == RecyclerView.SCROLL_STATE_IDLE
                    && directiorDown) {

                    if(!allData && riwayatDateFilterListServis.size >= 10*page)
                        loadDataNext()

                }
            }

        })

    }

    override fun onResume() {
        super.onResume()

        page = 1

        val i = intent
        dateB = i.getStringExtra("dateB")
        dateA = i.getStringExtra("dateA")

        Log.d("DATE_FROM_INTENT",dateB+" "+dateA)

        pbLoadingNextRDF.visibility = View.GONE
        rvRDF.adapter = null
        loadData()

    }

    private fun loadData(){

        pbRDF.visibility = View.VISIBLE

        Client().prepare(Support.API_PTHABGSM).riwayatBookingUserDateFilter(SessionUser(applicationContext).userId(),page,dateB,dateA)
            .enqueue(object : retrofit2.Callback<RiwayatDateFilterBookingUserResponse>{
                override fun onFailure(call: Call<RiwayatDateFilterBookingUserResponse>, t: Throwable) {

                    pbRDF.visibility = View.GONE
                    Log.d("ONFAILURE",t.toString())
                    Toasty.error(applicationContext,"Koneksi gagal.", Toasty.LENGTH_SHORT).show()

                }

                override fun onResponse(
                    call: Call<RiwayatDateFilterBookingUserResponse>,
                    response: Response<RiwayatDateFilterBookingUserResponse>
                ) {

                    if(response.isSuccessful){

                        if(response.body()?.riwayat_booking_user?.size == 0)
                            tvKosongRDF.visibility = View.VISIBLE

                        pbRDF.visibility = View.GONE

                        riwayatDateFilterListServis = response.body()?.riwayat_booking_user!!
                        val adapter = RiwayatDateFilterRecyclerViewAdapter(applicationContext,riwayatDateFilterListServis)
                        adapter.notifyDataSetChanged()
                        rvRDF.adapter = adapter

                    }

                }

            })

    }

    private fun loadDataNext(){

        page+=1
        pbLoadingNextRDF.visibility = View.VISIBLE
        parcelable = rvRDF.layoutManager?.onSaveInstanceState()!!

        Client().prepare(Support.API_PTHABGSM).riwayatBookingUserDateFilter(SessionUser(applicationContext).userId(),page,dateB,dateA)
            .enqueue(object : retrofit2.Callback<RiwayatDateFilterBookingUserResponse>{
                override fun onFailure(call: Call<RiwayatDateFilterBookingUserResponse>, t: Throwable) {

                    pbLoadingNextRDF.visibility = View.GONE
                    Log.d("ONFAILURE",t.toString())
                    Toasty.error(applicationContext,"Koneksi gagal.", Toasty.LENGTH_SHORT).show()
                    page -= 1

                }

                override fun onResponse(
                    call: Call<RiwayatDateFilterBookingUserResponse>,
                    response: Response<RiwayatDateFilterBookingUserResponse>
                ) {

                    if(response.isSuccessful){

                        pbLoadingNextRDF.visibility = View.GONE

                        if(response.body()?.riwayat_booking_user!!.size == 0){
                            allData = true
                        }else{
                            riwayatDateFilterListServis.addAll(response.body()?.riwayat_booking_user!!)

                            val adapter = RiwayatDateFilterRecyclerViewAdapter(applicationContext,riwayatDateFilterListServis)
                            adapter.notifyDataSetChanged()
                            rvRDF.adapter = adapter
                            rvRDF.layoutManager?.onRestoreInstanceState(parcelable)
                        }



                    }

                }

            })

    }

}
