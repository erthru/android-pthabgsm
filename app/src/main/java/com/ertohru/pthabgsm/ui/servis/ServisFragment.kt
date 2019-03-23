package com.ertohru.pthabgsm.ui.servis


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager

import com.ertohru.pthabgsm.R
import com.ertohru.pthabgsm.api.Client
import com.ertohru.pthabgsm.api.Support
import com.ertohru.pthabgsm.api.response.DaftarBookingUserResponse
import com.ertohru.pthabgsm.utils.sharedpref.SessionUser
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.fragment_servis.view.*
import retrofit2.Call
import retrofit2.Response


class ServisFragment : Fragment() {

    lateinit var v:View
    var page = 1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        v = inflater.inflate(R.layout.fragment_servis, container, false)

        v.rvServis.layoutManager = LinearLayoutManager(context)
        v.rvServis.setHasFixedSize(true)

        return v
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

                        v.pbServis.visibility = View.GONE

                        val adapter = ServisRecyclerViewAdapter(context!!,response.body()?.data_booking_user)
                        adapter.notifyDataSetChanged()
                        v.rvServis.adapter = adapter

                    }

                }

            })

    }


}
