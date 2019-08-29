package com.ertohru.pthabgsm.ui.teknisiservispengerjaan

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ertohru.pthabgsm.R
import com.ertohru.pthabgsm.api.model.Booking
import com.ertohru.pthabgsm.utils.StringUtils
import kotlinx.android.synthetic.main.list_pengerjaan_teknisi_servis_pengerjaan.view.*

class TeknisiServisPengerjaanRecyclerView(val context: Context, val data: ArrayList<Booking>?) : RecyclerView.Adapter<TeknisiServisPengerjaanRecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.list_pengerjaan_teknisi_servis_pengerjaan,parent,false))
    }

    override fun getItemCount(): Int = data?.size ?: 0

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.v.tvBookingIdLPTSP.text = "No Invoice: #"+data?.get(position)?.booking_id
        holder.v.tvJenisLPTSP.text = "Jenis Servis: "+data?.get(position)?.booking_jenis_servis
        holder.v.tvDateLPTSP.text = StringUtils.dateMin(data?.get(position)?.booking_created_at!!)

        var status = ""
        var rawStatus = data?.get(position)?.last_status

        when(rawStatus){
            "DITERIMA" -> status = "Menunggu Pemilihan Sparepart"
            "PEMILIHAN PART" -> status = "Menunggu Konfirmasi User"
            "MENUNGGU PERSETUJUAN" -> status = "User Telah Menentukan Sparepart"
            "DALAM PENGERJAAN" -> status = "Dalam Pengerjaan"
        }

        holder.v.tvStatusLPTSP.text = status

    }

    class ViewHolder(val v: View): RecyclerView.ViewHolder(v)
}