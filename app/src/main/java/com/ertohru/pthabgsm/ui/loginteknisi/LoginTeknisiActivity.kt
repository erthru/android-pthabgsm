package com.ertohru.pthabgsm.ui.loginteknisi

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.ertohru.pthabgsm.R
import com.ertohru.pthabgsm.api.Client
import com.ertohru.pthabgsm.api.Support
import com.ertohru.pthabgsm.api.response.LoginTeknisiResponse
import com.ertohru.pthabgsm.ui.teknisimain.TeknisiMainActivity
import com.ertohru.pthabgsm.utils.Loading
import com.ertohru.pthabgsm.utils.sharedpref.SessionTeknisi
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_login_teknisi.*
import retrofit2.Call
import retrofit2.Response

class LoginTeknisiActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_teknisi)

        supportActionBar?.hide()

        if(SessionTeknisi(this).isLogin()){
            finish()
            startActivity(Intent(applicationContext, TeknisiMainActivity::class.java))
        }

        btnBackLT.setOnClickListener { this.finish() }
        btnLoginLT.setOnClickListener { login() }

    }

    private fun login(){

        val loading = Loading(this)
        loading.show("Autentikasi ....")

        Client().prepare(Support.API_PTHABGSM).loginTeknisi(edEmailLoginLT.text.toString(), edPasswordLoginLT.text.toString())
            .enqueue(object: retrofit2.Callback<LoginTeknisiResponse>{
                override fun onFailure(call: Call<LoginTeknisiResponse>, t: Throwable) {
                    Log.d("ONFAILURE",t.toString())
                    Toasty.error(this@LoginTeknisiActivity,"Koneksi bermasalah.", Toasty.LENGTH_SHORT).show()
                    loading.dismiss()
                }

                override fun onResponse(call: Call<LoginTeknisiResponse>, response: Response<LoginTeknisiResponse>) {

                    loading.dismiss()

                    if(response.isSuccessful){

                        if(response.body()?.error!!){
                            Toasty.error(applicationContext, response.body()?.message!!, Toasty.LENGTH_LONG).show()
                        }else{
                            SessionTeknisi(this@LoginTeknisiActivity).createSession(response.body()?.teknisi_id!!)
                            finish()
                            startActivity(Intent(applicationContext, TeknisiMainActivity::class.java))
                        }

                    }

                }
            })

    }

}
