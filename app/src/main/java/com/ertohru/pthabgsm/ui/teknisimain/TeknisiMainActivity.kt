package com.ertohru.pthabgsm.ui.teknisimain

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.aurelhubert.ahbottomnavigation.AHBottomNavigation
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem
import com.ertohru.pthabgsm.R
import com.ertohru.pthabgsm.ui.home.HomeFragment
import com.ertohru.pthabgsm.ui.profile.ProfileFragment
import com.ertohru.pthabgsm.ui.riwayat.RiwayatFragment
import com.ertohru.pthabgsm.ui.servis.ServisFragment
import com.ertohru.pthabgsm.ui.teknisiprofil.TeknisiProfilFragment
import com.ertohru.pthabgsm.ui.teknisiservismenunggu.TeknisiServisMenungguFragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_teknisi_main.*

class TeknisiMainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_teknisi_main)

        supportActionBar?.hide()

        val menunggu = AHBottomNavigationItem("Menunggu",R.drawable.ic_access_time_gray_24dp)
        val pengerjaan = AHBottomNavigationItem("Pengerjaan",R.drawable.ic_library_books_grey_24dp)
        val profil = AHBottomNavigationItem("Profil",R.drawable.ic_person_grey_24dp)

        botnavTM.addItem(menunggu)
        botnavTM.addItem(pengerjaan)
        botnavTM.addItem(profil)

        botnavTM.defaultBackgroundColor = Color.parseColor("#FFFFFF")
        botnavTM.accentColor = resources.getColor(R.color.colorAccent)
        botnavTM.inactiveColor = Color.parseColor("#747474")
        botnavTM.titleState = AHBottomNavigation.TitleState.ALWAYS_SHOW

        val fm = supportFragmentManager
        fm.beginTransaction().replace(R.id.flTM, TeknisiServisMenungguFragment()).commit()

        botnavTM.setOnTabSelectedListener(object : AHBottomNavigation.OnTabSelectedListener{

            override fun onTabSelected(position: Int, wasSelected: Boolean): Boolean {

                when(position){

                    0 -> fm.beginTransaction().replace(R.id.flTM, TeknisiServisMenungguFragment()).commit()
                    1 -> fm.beginTransaction().replace(R.id.flTM, TeknisiServisMenungguFragment()).commit()
                    2 -> fm.beginTransaction().replace(R.id.flTM, TeknisiProfilFragment()).commit()

                }

                return true
            }

        })
    }
}
