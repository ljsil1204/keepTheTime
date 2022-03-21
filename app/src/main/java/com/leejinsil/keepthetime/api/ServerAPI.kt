package com.leejinsil.keepthetime.api

import android.content.Context
import com.google.gson.GsonBuilder
import com.leejinsil.keepthetime.utils.ContextUtil
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*

class ServerAPI {

    companion object{

        private val BASE_URL = "https://keepthetime.xyz"
        private var retrofit : Retrofit? = null

        fun getRetrofit (context : Context) : Retrofit {

            if (retrofit == null) {

//                토큰 자동 첨부
                val interceptor = Interceptor{
                    with(it) {

                        val newRequest = request().newBuilder()
                            .addHeader("X-Http-Token", ContextUtil.getUserToken(context))
                            .build()

                        proceed(newRequest)
                    }
                }

                val myClient = OkHttpClient.Builder()
                    .addInterceptor(interceptor)
                    .build()

                val gson = GsonBuilder()
                    .setDateFormat("yyyy-MM-dd HH:mm:ss")
                    .registerTypeAdapter(
                        Date::class.java,
                        DateDeserializer()
                    )
                    .create()

                retrofit = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .client(myClient)
                    .build()

            }

            return retrofit!!

        }

    }

}