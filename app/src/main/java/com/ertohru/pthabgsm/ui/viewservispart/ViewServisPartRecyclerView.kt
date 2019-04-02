package com.ertohru.pthabgsm.ui.viewservispart

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ertohru.pthabgsm.api.model.BookingItemDaftarBookingItemResponse
import kotlinx.android.synthetic.main.list_view_servis_part.view.*
import android.graphics.Paint.ANTI_ALIAS_FLAG
import android.graphics.Paint.STRIKE_THRU_TEXT_FLAG
import android.graphics.Paint
import android.util.Log
import com.ertohru.pthabgsm.R
import com.ertohru.pthabgsm.utils.StringUtils




class ViewServisPartRecyclerView(val context:Context,val data:ArrayList<BookingItemDaftarBookingItemResponse>?) : RecyclerView.Adapter<ViewServisPartRecyclerView.Holder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(LayoutInflater.from(parent.context).inflate(R.layout.list_view_servis_part,parent,false))
    }

    override fun getItemCount(): Int = data?.size ?: 0

    override fun onBindViewHolder(holder: Holder, position: Int) {

        val entity = data?.get(position)
        holder.init(entity!!,position,context,data!!)

    }


    class Holder(val v: View) : RecyclerView.ViewHolder(v){

        fun init(entity:BookingItemDaftarBookingItemResponse, position: Int, context: Context, data:ArrayList<BookingItemDaftarBookingItemResponse>){

            if(!ViewServisPartActivity.ON_SELECTED)
                v.btnUnselectedLVSP.visibility = View.GONE

            v.tvNameLVSP.text = entity.barang_servis_nama
            v.tvHargaLVSP.text = "Rp. "+StringUtils.rupiah(entity.barang_servis_harga.toString())

            v.layoutLVSP.visibility = View.GONE

            if(entity.booking_item_status?.equals("DIPILIH")!!){
                v.layoutLVSP.visibility = View.VISIBLE

                ViewServisPartActivity.TOTAL_HARGA += entity.barang_servis_harga!!
                val bc = Intent()
                bc.action = "VSP_TOTAL"
                context.sendBroadcast(bc)

            }

            v.btnUnselectedLVSP.setOnClickListener {

                if(!ViewServisPartActivity.UNSELECTED_ITEM.contains(entity.booking_item_id.toString())){
                    ViewServisPartActivity.TOTAL_HARGA -= entity.barang_servis_harga!!
                    ViewServisPartActivity.UNSELECTED_ITEM.add(entity.booking_item_id.toString())

                    Log.d("UNSELECTED_ITEM",ViewServisPartActivity.UNSELECTED_ITEM.toString())

                    v.tvNameLVSP.paintFlags = v.tvNameLVSP.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                    v.tvHargaLVSP.paintFlags = v.tvHargaLVSP.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG

                    val bc = Intent()
                    bc.action = "VSP_TOTAL"
                    context.sendBroadcast(bc)
                }

            }

        }


    }
}