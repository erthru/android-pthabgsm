package com.ertohru.pthabgsm.ui.riwayat

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.ertohru.pthabgsm.R
import com.ertohru.pthabgsm.api.model.DataBookingUserDaftarBookingUserResponse
import com.ertohru.pthabgsm.api.model.RiwayatBookingUseRiwayatBookingUserResponse
import com.ertohru.pthabgsm.ui.pesanandetail.PesananDetailActivity
import com.ertohru.pthabgsm.utils.StringUtils
import kotlinx.android.synthetic.main.list_daftar_booking_user_servis.view.*
import kotlinx.android.synthetic.main.list_riwayat_booking_user_riwayat.view.*

class RiwayatRecyclerViewAdapter(val context:Context, val data:ArrayList<RiwayatBookingUseRiwayatBookingUserResponse>?) : RecyclerView.Adapter<RiwayatRecyclerViewAdapter.Holder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(LayoutInflater.from(parent.context).inflate(R.layout.list_riwayat_booking_user_riwayat,parent,false))
    }

    override fun getItemCount(): Int = data?.size ?: 0

    override fun onBindViewHolder(holder: Holder, position: Int) {

        holder.entity = data?.get(position)!!
        holder.setup(context)

    }


    class Holder(val v: View) : RecyclerView.ViewHolder(v){

        lateinit var entity: RiwayatBookingUseRiwayatBookingUserResponse

        fun setup(context: Context){
            v.tvDateLRBUR.text = StringUtils.dateMin(entity.booking_created_at!!)
            v.tvDealerLRBUR.text = entity.dealer_nama
            v.tvJenisLRBUR.text = "Jenis Servis: "+entity.booking_jenis_servis
            v.tvStatusLRBUR.text = entity.last_status?.toLowerCase()
            v.tvBookingIdLRBUR.text = "No. Invoice: #"+entity.booking_id.toString()

            if(entity.last_status == "SELESAI"){
                v.layoutStatusLRBUR.setBackgroundColor(ContextCompat.getColor(context,R.color.colorSuccess))
            }else{
                v.layoutStatusLRBUR.setBackgroundColor(ContextCompat.getColor(context,R.color.colorError))
            }

            v.layoutLRBUR.setOnClickListener {

                val i = Intent(context, PesananDetailActivity::class.java)
                i.putExtra("booking_id",entity?.booking_id.toString())
                i.putExtra("booking_jenis_servis",entity?.booking_jenis_servis)
                i.putExtra("booking_model_kendaraan",entity?.booking_model_kendaraan)
                i.putExtra("booking_vincode",entity?.booking_vincode)
                i.putExtra("booking_km",entity?.booking_km)
                i.putExtra("booking_no_polisi",entity?.booking_no_polisi)
                i.putExtra("booking_jadwal_servis",entity?.booking_jadwal_servis)
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