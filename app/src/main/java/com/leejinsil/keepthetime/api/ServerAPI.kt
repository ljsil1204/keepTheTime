package com.leejinsil.keepthetime.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ServerAPI {

    companion object{

        private val BASE_URL = "https://keepthetime.xyz"
        private var retrofit : Retrofit? = null

        fun getRetrofit () : Retrofit {

            if (retrofit == null) {

                retrofit = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()

            }

            return retrofit!!

        }

    }

}