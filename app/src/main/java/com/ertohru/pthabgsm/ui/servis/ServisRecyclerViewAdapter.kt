package com.ertohru.pthabgsm.ui.servis

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ertohru.pthabgsm.R
import com.ertohru.pthabgsm.api.model.DataBookingUserDaftarBookingUserResponse
import com.ertohru.pthabgsm.utils.StringUtils
import kotlinx.android.synthetic.main.list_daftar_booking_user_servis.view.*

class ServisRecyclerViewAdapter(val context:Context, val data:ArrayList<DataBookingUserDaftarBookingUserResponse>?) : RecyclerView.Adapter<ServisRecyclerViewAdapter.Holder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(LayoutInflater.from(parent.context).inflate(R.layout.list_daftar_booking_user_servis,parent,false))
    }

    override fun getItemCount(): Int = data?.size ?: 0

    override fun onBindViewHolder(holder: Holder, position: Int) {

        holder.entity = data?.get(position)!!
        holder.setup()

    }


    class Holder(val v: View) : RecyclerView.ViewHolder(v){

        lateinit var entity: DataBookingUserDaftarBookingUserResponse

        fun setup(){
            v.tvDateLDBUS.text = StringUtils.dateMin(entity.booking_created_at!!)
            v.tvDealerLDBUS.text = entity.dealer_nama
            v.tvJenisLDBUS.text = "Jenis Servis: "+entity.booking_jenis_servis
            v.tvBookingIdLDBUS.text = "Pesanan Id: #"+entity.booking_id.toString()
        }

    }
}