package com.ertohru.pthabgsm.ui.home


import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.ertohru.pthabgsm.R
import com.ertohru.pthabgsm.api.Client
import com.ertohru.pthabgsm.api.Support
import com.ertohru.pthabgsm.api.response.ReportUserResponse
import com.ertohru.pthabgsm.ui.newpesanan.NewPesananActivity
import com.ertohru.pthabgsm.utils.sharedpref.SessionUser
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.view.*
import retrofit2.Call
import retrofit2.Response


class HomeFragment : Fragment() {

    lateinit var v:View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        v = inflater.inflate(R.layout.fragment_home, container, false)

        handleView()

        return v
    }

    override fun onResume() {
        super.onResume()

        v.mainContentHome.visibility = View.GONE
        loadReport()

    }

    private fun handleView(){

        v.btnBuatPesananHome.setOnClickListener { startActivity(Intent(context,
            NewPesananActivity::class.java)) }

    }


    private fun loadReport(){

        v.pbHome.visibility = View.VISIBLE

        Client().prepare(Support.API_PTHABGSM).reportUser(SessionUser(context!!).userId())
            .enqueue(object : retrofit2.Callback<ReportUserResponse>{

                override fun onFailure(call: Call<ReportUserResponse>, t: Throwable) {
                    v.pbHome.visibility = View.GONE
                    Log.d("ONFAILURE",t.toString())
                    Toasty.error(context!!,"Koneksi gagal.",Toasty.LENGTH_SHORT).show()
                }

                override fun onResponse(call: Call<ReportUserResponse>, response: Response<ReportUserResponse>) {

                    v.pbHome.visibility = View.GONE

                    if(response.isSuccessful){

                        Log.d("JSON",response?.body().toString())

                        v.mainContentHome.visibility = View.VISIBLE
                        v.tvDoneHome.text = "Servis Selesai: "+response.body()?.report_user?.servis_selesai?.toString()
                        v.tvOngoingHome.text = "Pesanan Berjalan: "+response.body()?.report_user?.servis_berjalan?.toString()
                        v.tvPesananTodayHome.text = "Pesanan Hari Ini: "+response.body()?.report_user?.today_pesanan?.toString()
                        v.tvTotalPesananHome.text = "Total Servis: "+response.body()?.report_user?.total_pesanan?.toString()

                    }

                }

            })

    }


}
