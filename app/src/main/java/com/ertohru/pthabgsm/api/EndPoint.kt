package com.ertohru.pthabgsm.api

import com.ertohru.pthabgsm.api.response.*
import retrofit2.Call
import retrofit2.http.*

interface EndPoint{

    @FormUrlEncoded
    @POST("App.php?mod=registrasi")
    fun registrasi(
        @Field("login_email") loginEmail:String,
        @Field("user_nama_lengkap") userNamaLengkap:String,
        @Field("user_no_hp") userNoHp:String,
        @Field("login_pass") loginPass:String,
        @Field("user_alamat") userAlamat:String
    ) : Call<RegistrasiResponse>

    @FormUrlEncoded
    @POST("App.php?mod=login_user")
    fun login(
        @Field("login_email") loginEmail:String,
        @Field("login_pass") loginPass:String
    ) : Call<LoginResponse>

    @GET("App.php?mod=report_user")
    fun reportUser(
        @Query("user_id") userId:Int
    ) : Call<ReportUserResponse>

    @GET("App.php?mod=daftar_booking_user")
    fun daftarBookingUser(
        @Query("user_id") userId:Int,
        @Query("page") page:Int
    ) : Call<DaftarBookingUserResponse>

    @GET("App.php?mod=riwayat_booking_user")
    fun riwayatBookingUser(
        @Query("user_id") userId:Int,
        @Query("page") page:Int
    ) : Call<RiwayatBookingUserResponse>

    @GET("App.php?mod=riwayat_booking_user_date_filter")
    fun riwayatBookingUserDateFilter(
        @Query("user_id") userId:Int,
        @Query("page") page:Int,
        @Query("date_b") dateB:String,
        @Query("date_a") dateA:String
    ) : Call<RiwayatDateFilterBookingUserResponse>

    @FormUrlEncoded
    @POST("App.php?mod=start_booking")
    fun startBooking(
        @Field("user_id")userId:Int,
        @Field("dealer_id")dealerId:Int,
        @Field("booking_jenis_servis")jenisServis:String,
        @Field("booking_model_kendaraan")modelKendaraan:String,
        @Field("booking_vincode")vincode:String,
        @Field("booking_km")km:String,
        @Field("booking_no_polisi")noPolisi:String,
        @Field("booking_keterangan")keterangan:String
    ) : Call<StartBookingResponse>

    @GET("App.php?mod=daftar_booking_status")
    fun daftarBookingStatus(
        @Query("booking_id") bookingId:Int
    ) : Call<DaftarBookingStatusResponse>

    @GET("App.php?mod=daftar_booking_item")
    fun daftarBookingItem(
        @Query("booking_id") bookingId:Int
    ) : Call<DaftarBookingItemResponse>

    @FormUrlEncoded
    @POST("App.php?mod=menunggu_persetujuan_booking")
    fun menungguPersetujuanBooking(
        @Field("booking_id")bookingId:Int,
        @Field("unselected_booking_item_id")unselectedBookingItemId:String?
    ) : Call<MenungguPersetujuanBookingResponse>

    @GET("App.php?mod=user_detail")
    fun userDetail(
        @Query("user_id")userId: Int
    ) : Call<UserDetailResponse>

    @FormUrlEncoded
    @POST("App.php?mod=update_profile")
    fun updateProfile(
        @Field("user_id")userId: Int,
        @Field("user_nama_lengkap")userNamaLengkap: String,
        @Field("user_alamat")userAlamat: String,
        @Field("user_no_hp")userNoHp:String
    ) : Call<UpdateProfileResponse>

    @FormUrlEncoded
    @POST("App.php?mod=save_user_token")
    fun saveUserToken(
        @Field("user_id")userId:Int,
        @Field("token")token:String
    ) : Call<SaveUserTokenResponse>

    @FormUrlEncoded
    @POST("App.php?mod=remove_user_token")
    fun removeUserToken(
        @Field("user_id")userId:Int
    ) : Call<RemoveUserTokenResponse>

}