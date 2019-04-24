package com.ertohru.pthabgsm.ui.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import com.ertohru.pthabgsm.R
import com.ertohru.pthabgsm.ui.login.LoginActivity
import com.ertohru.pthabgsm.ui.main.MainActivity
import com.ertohru.pthabgsm.utils.sharedpref.SessionUser

class SplashActivity : AppCompatActivity() {

    var delay:Long = 2000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)

        supportActionBar?.hide()

        handlerHandle()

    }

    private fun handlerHandle(){

        val i = intent

        if(i.hasExtra("A"))
            delay = 0

        Handler().postDelayed({

            finish()

            if(i.hasExtra("BODY")){
                if(i.getStringExtra("BODY").contains("diterima") || i.getStringExtra("BODY").contains("pengerjaan") || i.getStringExtra("BODY").contains("ditentukan")) {

                    val intent = Intent(this,MainActivity::class.java)
                    intent.putExtra("TARGET","SERVIS")
                    startActivity(intent)

                }else if(i.getStringExtra("BODY").contains("selesai")) {

                    val intent = Intent(this,MainActivity::class.java)
                    intent.putExtra("TARGET","HISTORY")
                    startActivity(intent)

                }
            }else{
                if(SessionUser(this).isLogin()){
                    startActivity(Intent(this,MainActivity::class.java))
                }else{
                    startActivity(Intent(this,LoginActivity::class.java))
                }
            }


        },delay)

    }
}
