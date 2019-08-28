package com.ertohru.pthabgsm.ui.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.ertohru.pthabgsm.R
import com.ertohru.pthabgsm.api.Client
import com.ertohru.pthabgsm.api.Support
import com.ertohru.pthabgsm.api.response.LoginResponse
import com.ertohru.pthabgsm.ui.loginteknisi.LoginTeknisiActivity
import com.ertohru.pthabgsm.ui.main.MainActivity
import com.ertohru.pthabgsm.ui.signup.SignupActivity
import com.ertohru.pthabgsm.utils.Loading
import com.ertohru.pthabgsm.utils.fcm.FCMMessagingService
import com.ertohru.pthabgsm.utils.sharedpref.SessionUser
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.iid.InstanceIdResult
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_signup.*
import retrofit2.Call
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        supportActionBar?.hide()

        btnDaftarLogin.setOnClickListener { startActivity(Intent(this,SignupActivity::class.java)) }
        btnLogin.setOnClickListener { auth() }

        btnLoginTeknisiLogin.setOnClickListener { startActivity(Intent(this,LoginTeknisiActivity::class.java)) }

    }

    private fun auth(){

        val loading = Loading(this)
        loading.show("Autentikasi pengguna ....")

        Client().prepare(Support.API_PTHABGSM).login(edEmailLogin.text.toString(),edPasswordLogin.text.toString())
            .enqueue(object : retrofit2.Callback<LoginResponse>{

                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {

                    Log.d("ONFAILURE",t.toString())
                    Toasty.error(this@LoginActivity,"Koneksi bermasalah.", Toasty.LENGTH_SHORT).show()
                    loading.dismiss()

                }

                override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {

                    if(response.isSuccessful){

                        loading.dismiss()

                        if(!response.body()?.error!!){

                            SessionUser(this@LoginActivity).createSession(response.body()?.user?.user_id!!)

                            FirebaseInstanceId.getInstance().instanceId.addOnSuccessListener(this@LoginActivity, object : OnSuccessListener<InstanceIdResult>{

                                override fun onSuccess(p0: InstanceIdResult?) {

                                    Log.d("TOKEN"," "+p0?.token)

                                    FCMMessagingService().saveUserToken(SessionUser(this@LoginActivity).userId().toString(),p0?.token!!)
                                }

                            })

                            finish()
                            startActivity(Intent(applicationContext,MainActivity::class.java))

                        }else{

                            Toasty.error(this@LoginActivity,response.body()?.pesan!!,Toasty.LENGTH_SHORT).show()

                        }

                    }

                }

            })

    }

    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
    }

}
