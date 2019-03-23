package com.ertohru.pthabgsm.ui.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import com.ertohru.pthabgsm.R
import com.ertohru.pthabgsm.base.BaseActivity
import com.ertohru.pthabgsm.ui.login.LoginActivity
import com.ertohru.pthabgsm.ui.main.MainActivity
import com.ertohru.pthabgsm.utils.sharedpref.SessionUser

class SplashActivity : BaseActivity() {

    var delay:Long = 2000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)

        Handler().postDelayed({

            finish()
            if(SessionUser(this).isLogin()){
                startActivity(Intent(this,MainActivity::class.java))
            }else{
                startActivity(Intent(this,LoginActivity::class.java))
            }

        },delay)

    }
}
