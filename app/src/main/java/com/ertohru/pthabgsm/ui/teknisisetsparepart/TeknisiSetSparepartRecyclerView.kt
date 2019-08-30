package com.ertohru.pthabgsm.ui.teknisisetsparepart

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ertohru.pthabgsm.R
import com.ertohru.pthabgsm.api.model.DaftarBarangServis
import kotlinx.android.synthetic.main.list_select_sparepart.view.*

class TeknisiSetSparepartRecyclerView(val data: ArrayList<DaftarBarangServis>?) : RecyclerView.Adapter<TeknisiSetSparepartRecyclerView.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.list_select_sparepart,parent,false))
    }

    override fun getItemCount(): Int = data?.size ?: 0

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.v.tvNamaLSS.text = data?.get(position)?.barang_servis_nama
        holder.v.tvHargaLSS.text = "Rp. "+data?.get(position)?.barang_servis_harga

        var jumlah = 0

        holder.v.btnTambahLSS.setOnClickListener {
            jumlah += 1
            holder.v.tvJumlahLSS.text = jumlah.toString()
            TeknisiSetSparepartActivity.SELECTED_SPAREPART.add(TeknisiSetSparepartActivity.SelectedSparepart(
                data?.get(position)?.barang_servis_id!!,
                data?.get(position)?.barang_servis_nama,
                data?.get(position)?.barang_servis_harga
            ))

            Log.d("SELECTED_SPAREPART", "SELECTED: "+TeknisiSetSparepartActivity.SELECTED_SPAREPART.toString())
        }

        holder.v.btnKurangLSS.setOnClickListener {
            if (jumlah > 0){
                jumlah -= 1
                holder.v.tvJumlahLSS.text = jumlah.toString()

                val selectedSparepart = TeknisiSetSparepartActivity.SelectedSparepart(
                    data?.get(position)?.barang_servis_id!!,
                    data?.get(position)?.barang_servis_nama,
                    data?.get(position)?.barang_servis_harga
                )

                TeknisiSetSparepartActivity.SELECTED_SPAREPART.remove(selectedSparepart)
                Log.d("SELECTED_SPAREPART", "SELECTED: "+TeknisiSetSparepartActivity.SELECTED_SPAREPART.toString())
            }
        }

    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    class ViewHolder(val v:View) : RecyclerView.ViewHolder(v)
}