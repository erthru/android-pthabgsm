package com.ertohru.pthabgsm.utils

import android.content.Context
import android.view.LayoutInflater
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.ertohru.pthabgsm.R
import kotlinx.android.synthetic.main.dialog_loding.view.*

class Loading(val context: Context){

    private val materialDialog = MaterialDialog(context)

    fun show(message:String){

        val inf = LayoutInflater.from(context).inflate(R.layout.dialog_loding,null,false)
        inf.tvMessage.text = message

        materialDialog.customView(null,inf,false,true)
        materialDialog.cancelable(false)
        materialDialog.show()

    }

    fun dismiss(){
        materialDialog.dismiss()
    }

}