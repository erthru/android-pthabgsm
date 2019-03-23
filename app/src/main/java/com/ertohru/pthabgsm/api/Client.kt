package com.ertohru.pthabgsm.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Client {

    private fun retrofit(baseUrl:String) : Retrofit{

        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return retrofit

    }

    fun prepare(baseUrl:String) : EndPoint{

        return retrofit(baseUrl).create(EndPoint::class.java)

    }

}