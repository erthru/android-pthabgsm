package com.ertohru.pthabgsm.ui.profile


import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.afollestad.materialdialogs.DialogCallback
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView

import com.ertohru.pthabgsm.R
import com.ertohru.pthabgsm.api.Client
import com.ertohru.pthabgsm.api.Support
import com.ertohru.pthabgsm.api.response.UpdateProfileResponse
import com.ertohru.pthabgsm.api.response.UserDetailResponse
import com.ertohru.pthabgsm.ui.bantuan.BantuanActivity
import com.ertohru.pthabgsm.utils.Loading
import com.ertohru.pthabgsm.utils.sharedpref.SessionUser
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.dialog_update_profile.view.*
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.fragment_profile.view.*
import retrofit2.Call
import retrofit2.Response


class ProfileFragment : Fragment() {

    lateinit var v:View
    lateinit var userId:String
    lateinit var nama:String
    lateinit var alamat:String
    lateinit var nohp:String
    lateinit var email:String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        v = inflater.inflate(R.layout.fragment_profile, container, false)

        handleView()

        return v
    }

    override fun onResume(){
        super.onResume()
        loadDetail()
    }

    private fun handleView(){

        v.btnEditProfile.setOnClickListener { showDialogUpdate() }

        v.btnLogoutProfile.setOnClickListener {
            logout()
        }

        v.btnBantuanProfile.setOnClickListener { startActivity(Intent(context,BantuanActivity::class.java)) }

    }

    private fun loadDetail(){

        v.layoutProfile.visibility = View.GONE
        v.pbProfile.visibility = View.VISIBLE

        Client().prepare(Support.API_PTHABGSM).userDetail(SessionUser(context!!).userId())
            .enqueue(object : retrofit2.Callback<UserDetailResponse>{

                override fun onFailure(call: Call<UserDetailResponse>, t: Throwable) {

                    Log.d("ONFAILURE",t.toString())
                    Toasty.error(context!!,"Koneksi internet gagal.",Toasty.LENGTH_SHORT)
                    v.pbProfile.visibility = View.GONE

                }

                override fun onResponse(call: Call<UserDetailResponse>, response: Response<UserDetailResponse>) {

                    if(response.isSuccessful){

                        v.pbProfile.visibility = View.GONE
                        v.layoutProfile.visibility = View.VISIBLE

                        v.tvInitialProfile.text = response.body()?.user?.user_nama_lengkap?.substring(0,1)
                        v.tvNamaProfile.text = response.body()?.user?.user_nama_lengkap
                        v.tvEmailProfile.text = response.body()?.user?.login_email
                        v.tvAlamatProfile.text = response.body()?.user?.user_alamat

                        userId = response.body()?.user?.user_id.toString()
                        nama = response.body()?.user?.user_nama_lengkap!!
                        alamat = response.body()?.user?.user_alamat!!
                        nohp = response.body()?.user?.user_no_hp!!
                        email = response.body()?.user?.login_email!!


                    }

                }

            })

    }

    private fun showDialogUpdate(){

        val dialog = MaterialDialog(context!!)

        val vv = LayoutInflater.from(context).inflate(R.layout.dialog_update_profile,null,false)

        vv.edNamaDUP.setText(nama)
        vv.edAlamatDUP.setText(alamat)
        vv.edNohpDUP.setText(nohp)

        vv.btnUpdateDUP.setOnClickListener {

            if(vv.edNamaDUP.text.toString().isNullOrEmpty() || vv.edAlamatDUP.text.toString().isNullOrEmpty() || vv.edNohpDUP.text.toString().isNullOrEmpty()){
                Toasty.error(context!!,"Field tidak boleh ada yang kosong",Toasty.LENGTH_SHORT).show()
            }else{
                dialog.dismiss()
                updateProfile(vv.edNamaDUP.text.toString(), vv.edAlamatDUP.text.toString(), vv.edNohpDUP.text.toString())
            }

        }

        dialog.show {

            customView(null,vv,false,false)

        }
    }

    private fun updateProfile(nama:String,alamat:String,nohp:String){

        val loading = Loading(context!!)
        loading.show("Memperbarui profil...")

        Client().prepare(Support.API_PTHABGSM).updateProfile(userId.toInt(),nama,alamat,nohp)
            .enqueue(object : retrofit2.Callback<UpdateProfileResponse>{

                override fun onFailure(call: Call<UpdateProfileResponse>, t: Throwable) {
                    Log.d("ONFAILURE",t.toString())
                    Toasty.error(context!!,"Koneksi internet gagal.",Toasty.LENGTH_SHORT).show()
                    loading.dismiss()
                }

                override fun onResponse(call: Call<UpdateProfileResponse>, response: Response<UpdateProfileResponse>) {

                    if(response.isSuccessful){

                        loading.dismiss()
                        Toasty.success(context!!,response.body()?.pesan!!,Toasty.LENGTH_SHORT).show()
                        onResume()

                    }

                }

            })

    }

    private fun logout(){

    }

}
