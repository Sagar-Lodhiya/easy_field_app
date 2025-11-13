package com.easyfield.network

import android.util.Log
import com.easyfield.utils.AppConstants
import com.easyfield.utils.PreferenceHelper

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {

    val retrofitClientURL="https://easy-field.codeflix.fr"

    val instance: API by lazy {

        val client = OkHttpClient.Builder().apply {
            addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }).addInterceptor(AuthInterceptor())
            connectTimeout(160, TimeUnit.SECONDS)
            readTimeout(160, TimeUnit.SECONDS)
            writeTimeout(160, TimeUnit.SECONDS)
        }.build()


        val retrofit = Retrofit.Builder()
            .baseUrl("https://easy-field.codeflix.fr/web/api/v1/ ")
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()

        retrofit.create(API::class.java)
    }

    class AuthInterceptor : Interceptor {

        override fun intercept(chain: Interceptor.Chain): Response {
            val requestBuilder = chain.request().newBuilder()

            requestBuilder.header("Content-Type", "application/json");
            requestBuilder.header("Accept", "application/json");

            val token = PreferenceHelper[AppConstants.PREF_KEY_AUTH_TOKEN, "test"]
            Log.w("vishaltoken",""+token.toString())
            if (!token.isNullOrEmpty()) {
                requestBuilder.addHeader("Authorization", "Bearer "+token)
            }
            return chain.proceed(requestBuilder.build())
        }
    }
}