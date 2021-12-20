package com.example.pomeloassignment.apis

import com.example.pomeloassignment.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class POMService {
    private val BASE_URL = "https://45434c1b-1e22-4af2-8c9f-c2d99ffa4896.mock.pstmn.io/";
    fun getPOMService(): POMApi {


        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BASIC
        logging.level = HttpLoggingInterceptor.Level.BODY
        logging.level = HttpLoggingInterceptor.Level.HEADERS
        val httpClient = OkHttpClient.Builder()

        //Add key in gradle.properties file to use this API
        httpClient.addInterceptor(logging).addInterceptor { chain ->
            val request: Request =
                chain.request().newBuilder().addHeader("content-type", "application/json").
                addHeader("x-api-key",BuildConfig.API_KEY).build()
            chain.proceed(request)
        };

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create()).client(httpClient.build())
            .build()
            .create(POMApi::class.java)
    }


}