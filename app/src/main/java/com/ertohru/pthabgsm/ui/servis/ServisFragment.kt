package com.ertohru.pthabgsm.ui.servis


import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.ertohru.pthabgsm.R
import com.ertohru.pthabgsm.api.Client
import com.ertohru.pthabgsm.api.Support
import com.ertohru.pthabgsm.api.model.DataBookingUserDaftarBookingUserResponse
import com.ertohru.pthabgsm.api.response.DaftarBookingUserResponse
import com.ertohru.pthabgsm.utils.sharedpref.SessionUser
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.fragment_servis.*
import kotlinx.android.synthetic.main.fragment_servis.view.*
import retrofit2.Call
import retrofit2.Response


class ServisFragment : Fragment() {

    lateinit var v:View
    var page = 1
    lateinit var parcelable:Parcelable
    var dataListServis = ArrayList<DataBookingUserDaftarBookingUserResponse>()
    var allData = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        v = inflater.inflate(R.layout.fragment_servis, container, false)

        v.rvServis.layoutManager = LinearLayoutManager(context)
        v.rvServis.setHasFixedSize(true)

        handleViewAction()

        return v
    }

    private fun handleViewAction(){

        v.rvServis.addOnScrollListener(object : RecyclerView.OnScrollListener(){

            var directiorDown:Boolean = false

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                directiorDown = dy > 0
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {

                if (recyclerView.canScrollVertically(1).not()
                    && newState == RecyclerView.SCROLL_STATE_IDLE
                    && directiorDown) {

                    if(!allData && dataListServis.size >= 10)
                        loadDataNext()

                }
            }

        })

    }

    override fun onResume() {
        super.onResume()

        page = 1
        v.rvServis.adapter = null
        loadData()

    }

    private fun loadData(){

        v.pbServis.visibility = View.VISIBLE

        Client().prepare(Support.API_PTHABGSM).daftarBookingUser(SessionUser(context!!).userId(),page)
            .enqueue(object : retrofit2.Callback<DaftarBookingUserResponse>{
                override fun onFailure(call: Call<DaftarBookingUserResponse>, t: Throwable) {

                    v.pbServis.visibility = View.GONE
                    Log.d("ONFAILURE",t.toString())
                    Toasty.error(context!!,"Koneksi gagal.", Toasty.LENGTH_SHORT).show()

                }

                override fun onResponse(
                    call: Call<DaftarBookingUserResponse>,
                    response: Response<DaftarBookingUserResponse>
                ) {

                    if(response.isSuccessful){

                        if(response.body()?.data_booking_user?.size == 0)
                            tvKosongServis.visibility = View.VISIBLE

                        v.pbServis.visibility = View.GONE

                        dataListServis = response.body()?.data_booking_user!!
                        val adapter = ServisRecyclerViewAdapter(context!!,dataListServis)
                        adapter.notifyDataSetChanged()
                        v.rvServis.adapter = adapter

                    }

                }

            })

    }

    private fun loadDataNext(){

        page+=1
        v.pbLoadingNext.visibility = View.VISIBLE
        parcelable = v.rvServis.layoutManager?.onSaveInstanceState()!!

        Client().prepare(Support.API_PTHABGSM).daftarBookingUser(SessionUser(context!!).userId(),page)
            .enqueue(object : retrofit2.Callback<DaftarBookingUserResponse>{
                override fun onFailure(call: Call<DaftarBookingUserResponse>, t: Throwable) {

                    v.pbLoadingNext.visibility = View.GONE
                    Log.d("ONFAILURE",t.toString())
                    Toasty.error(context!!,"Koneksi gagal.", Toasty.LENGTH_SHORT).show()
                    page -= 1

                }

                override fun onResponse(
                    call: Call<DaftarBookingUserResponse>,
                    response: Response<DaftarBookingUserResponse>
                ) {

                    if(response.isSuccessful){

                        v.pbLoadingNext.visibility = View.GONE



                        if(response.body()?.data_booking_user!!.size == 0){
                            allData = true
                        }else{
                            dataListServis.addAll(response.body()?.data_booking_user!!)

                            val adapter = ServisRecyclerViewAdapter(context!!,dataListServis)
                            adapter.notifyDataSetChanged()
                            v.rvServis.adapter = adapter
                            v.rvServis.layoutManager?.onRestoreInstanceState(parcelable)
                        }



                    }

                }

            })

    }


}
