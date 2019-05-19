package com.ertohru.pthabgsm.ui.signup

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.ertohru.pthabgsm.R
import com.ertohru.pthabgsm.api.Client
import com.ertohru.pthabgsm.api.Support
import com.ertohru.pthabgsm.api.response.RegistrasiResponse
import com.ertohru.pthabgsm.ui.login.LoginActivity
import com.ertohru.pthabgsm.utils.Loading
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.activity_signup.*
import retrofit2.Call
import retrofit2.Response

class SignupActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        supportActionBar?.hide()

        btnMasukSignup.setOnClickListener { startActivity(Intent(this,LoginActivity::class.java)) }
        btnSignup.setOnClickListener {
            if(edPasswordSignup.text.toString() != edPasswordReSignup.text.toString()){
                Toasty.error(this,"Password tidak sama",Toasty.LENGTH_SHORT).show()
            }else{
                signUp()
            }
        }

    }

    private fun signUp(){

        val loading = Loading(this)
        loading.show("Proses data...")

        Client().prepare(Support.API_PTHABGSM).registrasi(
            edEmailSignup.text.toString(),
            edFullNameSignup.text.toString(),
            edTelpSignup.text.toString(),
            edPasswordSignup.text.toString(),
            edAddressSignup.text.toString()
        )
            .enqueue(object : retrofit2.Callback<RegistrasiResponse>{

                override fun onFailure(call: Call<RegistrasiResponse>, t: Throwable) {

                    Log.d("ONFAILURE",t.toString())
                    Toasty.error(this@SignupActivity,"Koneksi bermasalah.",Toasty.LENGTH_SHORT).show()
                    loading.dismiss()

                }

                override fun onResponse(call: Call<RegistrasiResponse>, response: Response<RegistrasiResponse>) {

                    if(response.isSuccessful){

                        loading.dismiss()

                        if(!response.body()?.error!!){

                            Toasty.success(this@SignupActivity,"Registrasi berhasil. Silahkan login",Toasty.LENGTH_LONG).show()
                            finish()
                            startActivity(Intent(applicationContext,LoginActivity::class.java))

                        }else{

                            Toasty.error(this@SignupActivity,response.body()?.pesan!!,Toasty.LENGTH_LONG).show()

                        }

                    }

                }

            })

    }


}
