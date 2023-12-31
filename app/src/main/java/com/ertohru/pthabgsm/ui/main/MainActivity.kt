package com.ertohru.pthabgsm.ui.main

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.aurelhubert.ahbottomnavigation.AHBottomNavigation
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem
import com.ertohru.pthabgsm.R
import com.ertohru.pthabgsm.ui.servis.ServisFragment
import com.ertohru.pthabgsm.ui.home.HomeFragment
import com.ertohru.pthabgsm.ui.login.LoginActivity
import com.ertohru.pthabgsm.ui.profile.ProfileFragment
import com.ertohru.pthabgsm.ui.riwayat.RiwayatFragment
import com.ertohru.pthabgsm.utils.sharedpref.SessionUser
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar?.hide()

        setUpBottomNavigation()
        intentNavigation()

    }

    private fun setUpBottomNavigation(){

        val home = AHBottomNavigationItem("Beranda",R.drawable.ic_home_grey_24dp)
        val servis = AHBottomNavigationItem("Servis",R.drawable.ic_library_books_grey_24dp)
        val history = AHBottomNavigationItem("Riwayat",R.drawable.ic_history_grey_24dp)
        val profile = AHBottomNavigationItem("Profil",R.drawable.ic_person_grey_24dp)

        botnavMain.addItem(home)
        botnavMain.addItem(servis)
        botnavMain.addItem(history)
        botnavMain.addItem(profile)

        botnavMain.defaultBackgroundColor = Color.parseColor("#FFFFFF")
        botnavMain.accentColor = resources.getColor(R.color.colorAccent)
        botnavMain.inactiveColor = Color.parseColor("#747474")
        botnavMain.titleState = AHBottomNavigation.TitleState.ALWAYS_SHOW

        val fm = supportFragmentManager
        fm.beginTransaction().replace(R.id.flMain,HomeFragment()).commit()

        botnavMain.setOnTabSelectedListener(object : AHBottomNavigation.OnTabSelectedListener{

            override fun onTabSelected(position: Int, wasSelected: Boolean): Boolean {

                when(position){

                    0 -> fm.beginTransaction().replace(R.id.flMain, HomeFragment()).commit()
                    1 -> fm.beginTransaction().replace(R.id.flMain, ServisFragment()).commit()
                    2 -> fm.beginTransaction().replace(R.id.flMain, RiwayatFragment()).commit()
                    3 -> fm.beginTransaction().replace(R.id.flMain, ProfileFragment()).commit()

                }

                return true
            }

        })

    }

    private fun intentNavigation(){
        val i = intent

        if(i.hasExtra("TARGET")){
            if(i.getStringExtra("TARGET") == "SERVIS"){
                botnavMain.currentItem = 1
            }else if(i.getStringExtra("TARGET") == "HISTORY"){
                botnavMain.currentItem = 2
            }
        }

    }

    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
    }

}
