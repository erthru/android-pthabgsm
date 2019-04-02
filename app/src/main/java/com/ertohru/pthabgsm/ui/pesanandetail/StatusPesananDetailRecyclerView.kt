package com.ertohru.pthabgsm.ui.pesanandetail

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.ertohru.pthabgsm.R
import com.ertohru.pthabgsm.api.model.BookingStatusDaftarBookingStatusResponse
import com.ertohru.pthabgsm.utils.StringUtils
import kotlinx.android.synthetic.main.list_status_pesanan_detail.view.*

class StatusPesananDetailRecyclerView(val context: Context, val data:ArrayList<BookingStatusDaftarBookingStatusResponse>?) : RecyclerView.Adapter<StatusPesananDetailRecyclerView.Holder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(LayoutInflater.from(parent.context).inflate(R.layout.list_status_pesanan_detail,parent,false))
    }

    override fun getItemCount(): Int = data?.size ?: 0

    override fun onBindViewHolder(holder: Holder, position: Int) {

        val entity = data?.get(position)
        holder.init(entity,position,context)


    }


    class Holder(val v:View) : RecyclerView.ViewHolder(v){

        fun init(entity:BookingStatusDaftarBookingStatusResponse?,position: Int,context: Context){

            v.tvTglLSPD.text = StringUtils.dateLengkap(entity?.booking_status_created_at!!)
            v.tvStatusLSPD.text = entity.booking_status_stat!!

            if(position > 0){

                v.imgBulletLSPD.background = ContextCompat.getDrawable(context,R.drawable.view_circle_grey)

            }

        }

    }
}