package com.ertohru.pthabgsm.utils.sharedpref

import android.content.Context
import android.content.SharedPreferences

class SessionTeknisi(val context: Context) {

    val sp:SharedPreferences = context.getSharedPreferences("session_teknisi",0)
    val editor:SharedPreferences.Editor = sp.edit()

    fun createSession(teknisiId:Int){
        editor.putInt("teknisiId",teknisiId)
        editor.putBoolean("isLogin",true)
        editor.commit()
    }

    fun teknisiId() : Int{

        return sp.getInt("teknisiId",0)

    }

    fun isLogin() : Boolean{

        return sp.getBoolean("isLogin",false)

    }

    fun clearSession(){
        editor.clear()
        editor.commit()
    }
}