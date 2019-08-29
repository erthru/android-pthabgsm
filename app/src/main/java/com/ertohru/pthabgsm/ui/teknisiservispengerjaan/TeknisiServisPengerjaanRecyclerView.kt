package com.ertohru.pthabgsm.ui.teknisiservispengerjaan

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ertohru.pthabgsm.R
import com.ertohru.pthabgsm.api.model.Booking
import com.ertohru.pthabgsm.ui.teknisiservisdetail.TeknisiServisDetailActivity
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

        holder.v.layoutLPTSP.setOnClickListener {
            context.startActivity(Intent(context, TeknisiServisDetailActivity::class.java)
                .putExtra("booking_id", data?.get(position)?.booking_id)
                .putExtra("booking_jenis_servis",data?.get(position)?.booking_jenis_servis)
                .putExtra("booking_model_kendaraan",data?.get(position)?.booking_model_kendaraan)
                .putExtra("booking_vincode",data?.get(position)?.booking_vincode)
                .putExtra("booking_km",data?.get(position)?.booking_km)
                .putExtra("booking_no_polisi",data?.get(position)?.booking_no_polisi)
                .putExtra("booking_jadwal_servis",data?.get(position)?.booking_jadwal_servis)
                .putExtra("booking_keterangan",data?.get(position)?.booking_keterangan)
                .putExtra("booking_biaya",data?.get(position)?.booking_biaya)
                .putExtra("booking_created_at",data?.get(position)?.booking_created_at)
                .putExtra("user_id",data?.get(position)?.user_id)
                .putExtra("dealer_id",data?.get(position)?.dealer_id)
                .putExtra("user_nama_lengkap",data?.get(position)?.user_nama_lengkap)
                .putExtra("user_alamat",data?.get(position)?.user_alamat)
                .putExtra("user_no_hp",data?.get(position)?.user_no_hp)
                .putExtra("user_created_at",data?.get(position)?.user_created_at)
                .putExtra("user_updated_at",data?.get(position)?.user_updated_at)
                .putExtra("last_status",data?.get(position)?.last_status)
            )
        }

    }

    class ViewHolder(val v: View): RecyclerView.ViewHolder(v)
}