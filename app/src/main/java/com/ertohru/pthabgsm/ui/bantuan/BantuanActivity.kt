package com.ertohru.pthabgsm.ui.bantuan

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.ertohru.pthabgsm.R
import kotlinx.android.synthetic.main.activity_bantuan.*

class BantuanActivity : AppCompatActivity() {

    private var data = ArrayList<HashMap<String, String>>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bantuan)

        supportActionBar?.hide()

        populateItem()
        handleView()

    }

    private fun populateItem(){

        var item0 = HashMap<String,String>()
        item0.put("title","Pesanan ditolak")
        item0.put("content","Alasan pesanan ditolak: \n\n" +
                "1. Stok sparepart yang dibutuhkan lagi out of stock (Habis).\n" +
                "2. Pesanan anda melebihi batas pada hari itu.\n" +
                "3. Antrian pesanan terlalu banyak pada hari itu")
        data.add(item0)
    }

    private fun handleView(){

        btnBackBantuan.setOnClickListener { this.finish() }

        rvBantuan.setHasFixedSize(true)
        rvBantuan.layoutManager = LinearLayoutManager(this)

        val adapter = BantuanRecyclerView(this,data)
        adapter.notifyDataSetChanged()
        rvBantuan.adapter = adapter

    }

}
