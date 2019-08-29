package com.ertohru.pthabgsm.ui.teknisiservispengerjaan


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.ertohru.pthabgsm.R
import com.ertohru.pthabgsm.api.Client
import com.ertohru.pthabgsm.api.Support
import com.ertohru.pthabgsm.api.model.Booking
import com.ertohru.pthabgsm.api.model.ListBookingTeknisiResponse
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.fragment_teknisi_servis_pengerjaan.*
import kotlinx.android.synthetic.main.fragment_teknisi_servis_pengerjaan.view.*
import retrofit2.Call
import retrofit2.Response

class TeknisiServisPengerjaanFragment : Fragment() {

    private lateinit var v:View
    private val bookings = ArrayList<Booking>()
    lateinit var rvAdapter: TeknisiServisPengerjaanRecyclerView
    private var page = 0
    private var allDataLoaded = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_teknisi_servis_pengerjaan, container, false)

        v.rvFTSP.setHasFixedSize(true)
        v.rvFTSP.layoutManager = LinearLayoutManager(context)

        rvAdapter = TeknisiServisPengerjaanRecyclerView(context!!,bookings)
        v.rvFTSP.adapter = rvAdapter

        v.rvFTSP.addOnScrollListener(object: RecyclerView.OnScrollListener(){
            var directionDown = false

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                directionDown = dy > 0
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {

                if (recyclerView.canScrollVertically(1).not() && newState == RecyclerView.SCROLL_STATE_IDLE && directionDown){

                    if(!allDataLoaded){
                        loadBooking()
                    }

                }

            }
        })

        return v
    }

    override fun onResume() {
        super.onResume()
        page = 0
        allDataLoaded = false
        bookings.clear()

        v.rvFTSP.adapter = rvAdapter
        loadBooking()
    }

    private fun loadBooking(){

        page += 1
        v.tvKosongFTSP.visibility = View.GONE

        if (page > 1) {
            v.pbLoadingNextFTSP.visibility = View.VISIBLE
            v.pbFTSP.visibility = View.GONE
        }else {
            v.pbLoadingNextFTSP.visibility = View.GONE
            v.pbFTSP.visibility = View.VISIBLE
        }

        Client().prepare(Support.API_PTHABGSM).listBookingForteknisi(page)
            .enqueue(object: retrofit2.Callback<ListBookingTeknisiResponse>{

                override fun onFailure(call: Call<ListBookingTeknisiResponse>, t: Throwable) {
                    v.pbFTSP.visibility = View.GONE
                    v.pbLoadingNextFTSP.visibility = View.GONE

                    Toasty.error(context!!,"Koneksi Internet Gagal.",Toasty.LENGTH_SHORT).show()
                }

                override fun onResponse(
                    call: Call<ListBookingTeknisiResponse>,
                    response: Response<ListBookingTeknisiResponse>
                ) {

                    if(response.isSuccessful){

                        v.pbFTSP.visibility = View.GONE
                        v.pbLoadingNextFTSP.visibility = View.GONE

                        bookings.addAll(response?.body()?.bookings!!)
                        rvAdapter.notifyDataSetChanged()

                        if (bookings.size == 0){
                            v.tvKosongFTSP.visibility = View.VISIBLE
                        }

                        if (response?.body()?.bookings?.size == 0){
                            allDataLoaded = true
                        }

                    }

                }

            })

    }

}
