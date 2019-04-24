package com.ertohru.pthabgsm.utils.fcm

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.ertohru.pthabgsm.R
import com.ertohru.pthabgsm.api.Client
import com.ertohru.pthabgsm.api.Support
import com.ertohru.pthabgsm.api.response.RemoveUserTokenResponse
import com.ertohru.pthabgsm.api.response.SaveUserTokenResponse
import com.ertohru.pthabgsm.ui.splash.SplashActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import retrofit2.Call
import retrofit2.Response

class FCMMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(p0: RemoteMessage?) {
        super.onMessageReceived(p0)

        val data = HashMap<String, String>()
        data.put("A","a")
        data.put("BODY",p0?.data?.get("body")!!)

        Log.d("BODY_NOTIFICATION_DATA"," "+data.get("BODY"))

        showNotification(p0?.notification?.body,data)

    }

    override fun onNewToken(p0: String?) {
        super.onNewToken(p0)
    }

    private fun showNotification(message:String?, data:HashMap<String, String>){

        val i = Intent(applicationContext,SplashActivity::class.java)
        i.putExtra("A",data.get("A"))
        i.putExtra("BODY",data.get("BODY"))
        val pi = PendingIntent.getActivity(applicationContext, 0, i, PendingIntent.FLAG_UPDATE_CURRENT)

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){

            val channelId = "fcm_pthabgsm"
            val notifyId = 1
            val importance = NotificationManager.IMPORTANCE_HIGH

            val mChannel = NotificationChannel(channelId,"fcm_pthabgsm",importance)
            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(mChannel)

            val notification = Notification.Builder(this,channelId)
                .setContentTitle("PT Hajrat (Servis Mobil)")
                .setContentText(message)
                .setAutoCancel(true)
                .setSmallIcon(R.drawable.ic_settings_red_24dp)
                .setColor(resources.getColor(R.color.colorAccent))
                .setContentIntent(pi)
                .build()


            manager.notify(notifyId,notification)

        }else{
            val builder = NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_settings_red_24dp)
                .setColor(resources.getColor(R.color.colorAccent))
                .setContentTitle("PT Hajrat (Servis Mobil)")
                .setContentText(message)
                .setAutoCancel(true)
                .setPriority(Notification.PRIORITY_HIGH)
                .setDefaults(Notification.DEFAULT_SOUND)
                .setContentIntent(pi)

            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.notify(0,builder.build())
        }

    }

    fun saveUserToken(userId:String,token:String){

        Client().prepare(Support.API_PTHABGSM).saveUserToken(userId.toInt(),token)
            .enqueue(object : retrofit2.Callback<SaveUserTokenResponse>{
                override fun onFailure(call: Call<SaveUserTokenResponse>, t: Throwable) {
                    Log.d("ONFAULURE",t.toString())
                }

                override fun onResponse(call: Call<SaveUserTokenResponse>, response: Response<SaveUserTokenResponse>) {
                    if(response.isSuccessful){
                        Log.d("RESPONSE",response.body()?.pesan)
                    }
                }

            })


    }

    fun removeUserToken(userId:String){

        Client().prepare(Support.API_PTHABGSM).removeUserToken(userId.toInt())
            .enqueue(object : retrofit2.Callback<RemoveUserTokenResponse>{
                override fun onFailure(call: Call<RemoveUserTokenResponse>, t: Throwable) {
                    Log.d("ONFAULURE",t.toString())
                }

                override fun onResponse(
                    call: Call<RemoveUserTokenResponse>,
                    response: Response<RemoveUserTokenResponse>
                ) {

                    if(response.isSuccessful){
                        Log.d("RESPONSE",response.body()?.pesan)
                    }

                }

            })

    }

}