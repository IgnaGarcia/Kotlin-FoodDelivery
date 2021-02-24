package com.example.ejercicio3.network

import com.example.ejercicio3.entities.Plate
import com.example.ejercicio3.network.responses.PlateListResponse
import com.example.ejercicio3.network.responses.PlateResponse
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import java.util.concurrent.TimeUnit

object ApiClient{
    private const val API_KEY = "fd926ca241f8421da10f7370f3cf73a4"
    private const val API_BASE_URL = "https://api.spoonacular.com/"

    private var mInterface : AppService
    private var mRetrofitAdapter : Retrofit

    init{
        mRetrofitAdapter =
                Retrofit
                        .Builder()
                        .baseUrl(API_BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .client(
                                OkHttpClient.Builder()
                                        .connectTimeout(50, TimeUnit.SECONDS)
                                        .readTimeout(50, TimeUnit.SECONDS)
                                        .addInterceptor(
                                                HttpLoggingInterceptor()
                                                        .setLevel(HttpLoggingInterceptor.Level.BODY)
                                        )
                                        .build()
                        )
                        .build()

        mInterface = mRetrofitAdapter.create(AppService::class.java)
    }

    fun getServiceClient() = mInterface

    interface AppService{
        @GET("recipes/complexSearch?apiKey=$API_KEY&diet=vegetarian&addRecipeInformation=true&number=5")
        fun getPlates(): Call<PlateListResponse>

        @GET("recipes/complexSearch?apiKey=$API_KEY&diet=vegetarian&addRecipeInformation=true&number=20")
        fun getMorePlates(): Call<PlateListResponse>

        @GET("recipes/{plateId}/information?apiKey=$API_KEY")
        fun getPlateDetails(@Path("plateId") plateId : Int): Call<PlateResponse>
    }
}