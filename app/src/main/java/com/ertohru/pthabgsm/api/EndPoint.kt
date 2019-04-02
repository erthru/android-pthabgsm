package com.ertohru.pthabgsm.api

import com.ertohru.pthabgsm.api.response.*
import retrofit2.Call
import retrofit2.http.*

interface EndPoint{

    @FormUrlEncoded
    @POST("app.php?mod=registrasi")
    fun registrasi(
        @Field("login_email") loginEmail:String,
        @Field("user_nama_lengkap") userNamaLengkap:String,
        @Field("user_no_hp") userNoHp:String,
        @Field("login_pass") loginPass:String,
        @Field("user_alamat") userAlamat:String
    ) : Call<RegistrasiResponse>

    @FormUrlEncoded
    @POST("app.php?mod=login")
    fun login(
        @Field("login_email") loginEmail:String,
        @Field("login_pass") loginPass:String
    ) : Call<LoginResponse>

    @GET("app.php?mod=report_user")
    fun reportUser(
        @Query("user_id") userId:Int
    ) : Call<ReportUserResponse>

    @GET("app.php?mod=daftar_booking_user")
    fun daftarBookingUser(
        @Query("user_id") userId:Int,
        @Query("page") page:Int
    ) : Call<DaftarBookingUserResponse>

    @FormUrlEncoded
    @POST("app.php?mod=start_booking")
    fun startBooking(
        @Field("user_id")userId:Int,
        @Field("dealer_id")dealerId:Int,
        @Field("booking_jenis_servis")jenisServis:String,
        @Field("booking_keterangan")keterangan:String
    ) : Call<StartBookingResponse>

    @GET("app.php?mod=daftar_booking_status")
    fun daftarBookingStatus(
        @Query("booking_id") bookingId:Int
    ) : Call<DaftarBookingStatusResponse>

    @GET("app.php?mod=daftar_booking_item")
    fun daftarBookingItem(
        @Query("booking_id") bookingId:Int
    ) : Call<DaftarBookingItemResponse>

}