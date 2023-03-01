package com.fictivestudios.docsvisor.apiManager.client

import android.content.Context
import com.fictivestudios.docsvisor.helper.*
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiClient {

    class RetrofitInstance {
        companion object {
            //val BASE_URL: String = BASE_URL

            val interceptor: HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
                this.level = HttpLoggingInterceptor.Level.BODY
            }





            private lateinit var apiService: ApiInterface


            fun getDoctorThirdApi(context: Context):ApiInterface{

                val retrofit = Retrofit.Builder()
                    .baseUrl(DOCTOR_THRID_PARTY_BASE)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(okhttpClient(context))
                    .build()
                apiService = retrofit.create(ApiInterface::class.java)
                return apiService
            }


            fun getApiServiceForWatchData(context: Context): ApiInterface {

                val retrofit = Retrofit.Builder()
                    .baseUrl(WATCH_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(okhttpClientWatchData(context))
                    .build()

                apiService = retrofit.create(ApiInterface::class.java)

                return apiService
            }
            private fun okhttpClientWatchData(context: Context): OkHttpClient {
                return OkHttpClient.Builder()
                    .addInterceptor(interceptor)
                    .build()
            }



            fun getApiServiceForSymptonChecker(context: Context): ApiInterface {

                val retrofit = Retrofit.Builder()
                    .baseUrl(SYMPTON_CHECKER_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(okhttpClientForSymptomChecker(context))
                    .build()

                apiService = retrofit.create(ApiInterface::class.java)

                return apiService
            }
            private fun okhttpClientForSymptomChecker(context: Context): OkHttpClient {
                return OkHttpClient.Builder()
                    .addInterceptor(interceptor)
                    .build()
            }


            fun getApiService(context: Context): ApiInterface {




                    val retrofit = Retrofit.Builder()
                        .baseUrl(BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .client(okhttpClient(context))
                        .build()

                    apiService = retrofit.create(ApiInterface::class.java)




                return apiService
            }
            fun getAgoraApiService(context: Context): ApiInterface {




                    val retrofit = Retrofit.Builder()
                        .baseUrl(AGORA_BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .client(okhttpClient(context))
                        .build()

                    apiService = retrofit.create(ApiInterface::class.java)




                return apiService
            }


            private fun okhttpClient(context: Context): OkHttpClient {
                return OkHttpClient.Builder()
                    .addInterceptor(AuthInterceptor(context)/*interceptor*/)

                    .build()
            }


            private fun okhttpClient2(context: Context): OkHttpClient {
                return OkHttpClient.Builder()
                    .addInterceptor(interceptor)
                    .build()
            }
        }
    }

}