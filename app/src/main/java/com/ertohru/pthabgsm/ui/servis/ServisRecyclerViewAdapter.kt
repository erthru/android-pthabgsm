package com.ertohru.pthabgsm.ui.servis

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ertohru.pthabgsm.R
import com.ertohru.pthabgsm.api.model.DataBookingUserDaftarBookingUserResponse
import com.ertohru.pthabgsm.ui.pesanandetail.PesananDetailActivity
import com.ertohru.pthabgsm.utils.StringUtils
import kotlinx.android.synthetic.main.list_daftar_booking_user_servis.view.*

class ServisRecyclerViewAdapter(val context:Context, val data:ArrayList<DataBookingUserDaftarBookingUserResponse>?) : RecyclerView.Adapter<ServisRecyclerViewAdapter.Holder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(LayoutInflater.from(parent.context).inflate(R.layout.list_daftar_booking_user_servis,parent,false))
    }

    override fun getItemCount(): Int = data?.size ?: 0

    override fun onBindViewHolder(holder: Holder, position: Int) {

        holder.entity = data?.get(position)!!
        holder.setup(context)

    }


    class Holder(val v: View) : RecyclerView.ViewHolder(v){

        lateinit var entity: DataBookingUserDaftarBookingUserResponse

        fun setup(context: Context){
            v.tvDateLDBUS.text = StringUtils.dateMin(entity.booking_created_at!!)
            v.tvDealerLDBUS.text = entity.dealer_nama
            v.tvJenisLDBUS.text = "Jenis Servis: "+entity.booking_jenis_servis
            v.tvBookingIdLDBUS.text = "No. Invoice: #"+entity.booking_id.toString()

            v.layoutLDBUS.setOnClickListener {

                val i = Intent(context, PesananDetailActivity::class.java)
                i.putExtra("booking_id",entity?.booking_id.toString())
                i.putExtra("booking_jenis_servis",entity?.booking_jenis_servis)
                i.putExtra("booking_keterangan",entity?.booking_keterangan)
                i.putExtra("booking_biaya",entity?.booking_biaya)
                i.putExtra("booking_created_at",entity?.booking_created_at)
                i.putExtra("user_id",entity?.user_id)
                i.putExtra("dealer_id",entity?.dealer_id)
                i.putExtra("dealer_nama",entity?.dealer_nama)
                i.putExtra("dealer_alamat",entity?.dealer_alamat)
                i.putExtra("user_nama_lengkap",entity?.user_nama_lengkap)
                i.putExtra("user_alamat",entity?.user_alamat)
                i.putExtra("user_no_hp",entity?.user_no_hp)
                i.putExtra("user_created_at",entity?.user_created_at)
                i.putExtra("user_updated_at",entity?.user_updated_at)
                i.putExtra("last_status",entity?.last_status)

                context.startActivity(i)

            }

        }

    }
}