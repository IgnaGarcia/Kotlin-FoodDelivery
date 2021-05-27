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
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

object ApiClient{
    private const val API_KEY = "c34167616f834599a1ccf9a86f2397fd"
    //private const val API_KEY = "fd926ca241f8421da10f7370f3cf73a4"
    //private const val API_KEY = "1e2e85c08c95409fa3c5b99e7c671346"

    private const val API_BASE_URL = "https://api.spoonacular.com/"
    const val URL_COMPLEX = "recipes/complexSearch"
    const val URL_VEGIE = "diet=vegetarian&addRecipeInformation=true"

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
        @GET("$URL_COMPLEX?apiKey=$API_KEY&$URL_VEGIE")
        fun getPlates(@Query("number") number: Int,
                      @Query("offset") offset: Int): Call<PlateListResponse>

        @GET("$URL_COMPLEX?apiKey=$API_KEY&$URL_VEGIE&number=20")
        fun getPlates(@Query("titleMatch") titleMatch: String): Call<PlateListResponse>

        @GET("recipes/{plateId}/information?apiKey=$API_KEY")
        fun getPlateDetails(@Path("plateId") plateId : Int): Call<Plate>

        @GET("$URL_COMPLEX?apiKey=$API_KEY&addRecipeInformation=true&number=10")
        fun getPlatesByDiet(@Query("diet") diet : String) : Call<PlateListResponse>
    }
}