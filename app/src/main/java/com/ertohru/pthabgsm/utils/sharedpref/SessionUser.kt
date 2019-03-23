package com.ertohru.pthabgsm.utils.sharedpref

import android.content.Context
import android.content.SharedPreferences

class SessionUser(val context: Context) {

    val sp:SharedPreferences = context.getSharedPreferences("session_user",0)
    val editor:SharedPreferences.Editor = sp.edit()

    fun createSession(userId:Int){
        editor.putInt("userId",userId)
        editor.putBoolean("isLogin",true)
        editor.commit()
    }

    fun userId() : Int{

        return sp.getInt("userId",0)

    }

    fun isLogin() : Boolean{

        return sp.getBoolean("isLogin",false)

    }

    fun clearSession(){
        editor.clear()
        editor.commit()
    }
}