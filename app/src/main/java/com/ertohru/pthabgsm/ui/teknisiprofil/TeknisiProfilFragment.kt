package com.ertohru.pthabgsm.ui.teknisiprofil


import android.os.Bundle
import android.se.omapi.Session
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast

import com.ertohru.pthabgsm.R
import com.ertohru.pthabgsm.api.Client
import com.ertohru.pthabgsm.api.Support
import com.ertohru.pthabgsm.api.response.UpdatePasswordTeknisiResponse
import com.ertohru.pthabgsm.utils.Loading
import com.ertohru.pthabgsm.utils.sharedpref.SessionTeknisi
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.fragment_teknisi_profil.*
import kotlinx.android.synthetic.main.fragment_teknisi_profil.view.*
import retrofit2.Call
import retrofit2.Response

class TeknisiProfilFragment : Fragment() {

    lateinit var v: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        v = inflater.inflate(R.layout.fragment_teknisi_profil, container, false)

        v.btnLogoutTPF.setOnClickListener {
            SessionTeknisi(context!!).clearSession()
            activity?.finish()
        }

        v.btnGantiPasswordFTP.setOnClickListener {
            if(v.edPasswordFTP.text.toString() != v.edPasswordReFTP.text.toString()){
                Toasty.error(context!!,"Password tidak sama",Toasty.LENGTH_SHORT).show()
            }else if(v.edPasswordFTP.text.isNullOrEmpty() || v.edPasswordReFTP.text.isNullOrEmpty()){
                Toasty.error(context!!,"Password tidak boleh kosong",Toasty.LENGTH_SHORT).show()
            }else{
                gantiPassword()
            }
        }

        return v
    }

    private fun gantiPassword(){

        val loading = Loading(context!!)
        loading.show("Memproses...")

        Client().prepare(Support.API_PTHABGSM).updatePasswordTeknisi(SessionTeknisi(context!!).teknisiId().toString(), v.edPasswordFTP.text.toString())
            .enqueue(object: retrofit2.Callback<UpdatePasswordTeknisiResponse>{
                override fun onFailure(call: Call<UpdatePasswordTeknisiResponse>, t: Throwable) {
                    Log.d("ONFAILURE",t.toString())
                    Toasty.error(context!!,"Koneksi bermasalah.", Toasty.LENGTH_SHORT).show()
                    loading.dismiss()
                }

                override fun onResponse(
                    call: Call<UpdatePasswordTeknisiResponse>,
                    response: Response<UpdatePasswordTeknisiResponse>
                ) {

                    loading.dismiss()

                    if(response.isSuccessful){

                        Toasty.success(context!!,response.body()?.message!!, Toasty.LENGTH_SHORT).show()
                        edPasswordFTP.setText("")
                        edPasswordReFTP.setText("")
                        rootFTP.isFocusableInTouchMode = true

                    }

                }
            })

    }

}
