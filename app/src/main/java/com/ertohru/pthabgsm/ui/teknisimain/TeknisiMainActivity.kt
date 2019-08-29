package com.ertohru.pthabgsm.ui.teknisimain

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.aurelhubert.ahbottomnavigation.AHBottomNavigation
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem
import com.ertohru.pthabgsm.R
import com.ertohru.pthabgsm.ui.teknisiprofil.TeknisiProfilFragment
import com.ertohru.pthabgsm.ui.teknisiservispengerjaan.TeknisiServisPengerjaanFragment
import kotlinx.android.synthetic.main.activity_teknisi_main.*

class TeknisiMainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_teknisi_main)

        supportActionBar?.hide()

        val pengerjaan = AHBottomNavigationItem("Pengerjaan",R.drawable.ic_library_books_grey_24dp)
        val profil = AHBottomNavigationItem("Profil",R.drawable.ic_person_grey_24dp)

        botnavTM.addItem(pengerjaan)
        botnavTM.addItem(profil)

        botnavTM.defaultBackgroundColor = Color.parseColor("#FFFFFF")
        botnavTM.accentColor = resources.getColor(R.color.colorAccent)
        botnavTM.inactiveColor = Color.parseColor("#747474")
        botnavTM.titleState = AHBottomNavigation.TitleState.ALWAYS_SHOW

        val fm = supportFragmentManager
        fm.beginTransaction().replace(R.id.flTM, TeknisiServisPengerjaanFragment()).commit()

        botnavTM.setOnTabSelectedListener(object : AHBottomNavigation.OnTabSelectedListener{

            override fun onTabSelected(position: Int, wasSelected: Boolean): Boolean {

                when(position){

                    0 -> fm.beginTransaction().replace(R.id.flTM, TeknisiServisPengerjaanFragment()).commit()
                    1 -> fm.beginTransaction().replace(R.id.flTM, TeknisiProfilFragment()).commit()

                }

                return true
            }

        })
    }
}
