package com.ertohru.pthabgsm.ui.riwayat


import android.app.DatePickerDialog
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView

import com.ertohru.pthabgsm.R
import com.ertohru.pthabgsm.api.Client
import com.ertohru.pthabgsm.api.Support
import com.ertohru.pthabgsm.api.model.DataBookingUserDaftarBookingUserResponse
import com.ertohru.pthabgsm.api.model.RiwayatBookingUseRiwayatBookingUserResponse
import com.ertohru.pthabgsm.api.response.DaftarBookingUserResponse
import com.ertohru.pthabgsm.api.response.RiwayatBookingUserResponse
import com.ertohru.pthabgsm.ui.servis.ServisRecyclerViewAdapter
import com.ertohru.pthabgsm.utils.sharedpref.SessionUser
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.dialog_date_filter_riwayat.*
import kotlinx.android.synthetic.main.dialog_date_filter_riwayat.view.*
import kotlinx.android.synthetic.main.fragment_riwayat.*
import kotlinx.android.synthetic.main.fragment_riwayat.view.*
import kotlinx.android.synthetic.main.fragment_servis.*
import kotlinx.android.synthetic.main.fragment_servis.view.*
import retrofit2.Call
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import java.util.zip.Inflater

class RiwayatFragment : Fragment() {

    lateinit var v:View
    var page = 1
    lateinit var parcelable: Parcelable
    var riwayatListServis = ArrayList<RiwayatBookingUseRiwayatBookingUserResponse>()
    var allData = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        v = inflater.inflate(R.layout.fragment_riwayat, container, false)

        v.rvRiwayat.layoutManager = LinearLayoutManager(context)
        v.rvRiwayat.setHasFixedSize(true)

        handleViewAction()

        return v
    }

    private fun handleViewAction(){

        v.rvRiwayat.addOnScrollListener(object : RecyclerView.OnScrollListener(){

            var directiorDown:Boolean = false

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                directiorDown = dy > 0
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {

                if (recyclerView.canScrollVertically(1).not()
                    && newState == RecyclerView.SCROLL_STATE_IDLE
                    && directiorDown) {

                    if(!allData && riwayatListServis.size >= 10*page)
                        loadDataNext()

                }
            }

        })

        v.btnFilterRiwayat.setOnClickListener { dialogFilter() }

    }

    override fun onResume() {
        super.onResume()

        page = 1
        v.rvRiwayat.adapter = null
        loadData()

    }

    private fun loadData(){

        v.pbRIwayat.visibility = View.VISIBLE

        Client().prepare(Support.API_PTHABGSM).riwayatBookingUser(SessionUser(context!!).userId(),page)
            .enqueue(object : retrofit2.Callback<RiwayatBookingUserResponse>{
                override fun onFailure(call: Call<RiwayatBookingUserResponse>, t: Throwable) {

                    v.pbRIwayat.visibility = View.GONE
                    Log.d("ONFAILURE",t.toString())
                    Toasty.error(context!!,"Koneksi gagal.", Toasty.LENGTH_SHORT).show()

                }

                override fun onResponse(
                    call: Call<RiwayatBookingUserResponse>,
                    response: Response<RiwayatBookingUserResponse>
                ) {

                    if(response.isSuccessful){

                        if(response.body()?.riwayat_booking_user?.size == 0)
                            v.tvKosongRiwayat.visibility = View.VISIBLE

                        v.pbRIwayat.visibility = View.GONE

                        riwayatListServis = response.body()?.riwayat_booking_user!!
                        val adapter = RiwayatRecyclerViewAdapter(context!!,riwayatListServis)
                        adapter.notifyDataSetChanged()
                        v.rvRiwayat.adapter = adapter

                    }

                }

            })

    }

    private fun loadDataNext(){

        page+=1
        v.pbLoadingNextRiwayat.visibility = View.VISIBLE
        parcelable = v.rvRiwayat.layoutManager?.onSaveInstanceState()!!

        Client().prepare(Support.API_PTHABGSM).riwayatBookingUser(SessionUser(context!!).userId(),page)
            .enqueue(object : retrofit2.Callback<RiwayatBookingUserResponse>{
                override fun onFailure(call: Call<RiwayatBookingUserResponse>, t: Throwable) {

                    v.pbLoadingNextRiwayat.visibility = View.GONE
                    Log.d("ONFAILURE",t.toString())
                    Toasty.error(context!!,"Koneksi gagal.", Toasty.LENGTH_SHORT).show()
                    page -= 1

                }

                override fun onResponse(
                    call: Call<RiwayatBookingUserResponse>,
                    response: Response<RiwayatBookingUserResponse>
                ) {

                    if(response.isSuccessful){

                        v.pbLoadingNextRiwayat.visibility = View.GONE



                        if(response.body()?.riwayat_booking_user!!.size == 0){
                            allData = true
                        }else{
                            riwayatListServis.addAll(response.body()?.riwayat_booking_user!!)

                            val adapter = RiwayatRecyclerViewAdapter(context!!,riwayatListServis)
                            adapter.notifyDataSetChanged()
                            v.rvRiwayat.adapter = adapter
                            v.rvRiwayat.layoutManager?.onRestoreInstanceState(parcelable)
                        }



                    }

                }

            })

    }

    private fun dialogFilter(){

        val dview = LayoutInflater.from(context).inflate(R.layout.dialog_date_filter_riwayat,null,false)

        dview.btnSet1DDFR.setOnClickListener {

            val calendar = Calendar.getInstance()
            val date = DatePickerDialog.OnDateSetListener { datePicker, year, month, day ->

                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONDAY, month)
                calendar.set(Calendar.DAY_OF_MONTH, day)

                val format = "dd-MM-yyyy"

                dview.btnSet1DDFR.text = SimpleDateFormat(format, Locale.getDefault()).format(calendar.time)

            }

            DatePickerDialog(context,date,calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH)).show()

        }

        dview.btnSet2DDFR.setOnClickListener {

            val calendar = Calendar.getInstance()
            val date = DatePickerDialog.OnDateSetListener { datePicker, year, month, day ->

                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONDAY, month)
                calendar.set(Calendar.DAY_OF_MONTH, day)

                val format = "dd-MM-yyyy"

                dview.btnSet2DDFR.text = SimpleDateFormat(format, Locale.getDefault()).format(calendar.time)

            }

            DatePickerDialog(context,date,calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH)).show()

        }

        MaterialDialog(context!!).show {
            customView(null,dview,false,false)
        }

    }


}
