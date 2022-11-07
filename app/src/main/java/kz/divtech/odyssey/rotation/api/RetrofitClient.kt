package kz.divtech.odyssey.rotation.api

import kz.divtech.odyssey.rotation.Config
import kz.divtech.odyssey.rotation.utils.SessionManager
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    private fun getClient(): Retrofit {

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(Interceptor {
                val request: Request = it.request().newBuilder()
                        .addHeader(Config.DEVICE_ID_KEY, Config.DEVICE_ID_VALUE)
                        .addHeader(Config.AUTHORIZATION_KEY, SessionManager().getTokenWithBearer())
                        .build()
                it.proceed(request)
            })
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .build()

        return Retrofit.Builder().baseUrl(Config.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
    }

    fun getApiService() : ApiService{
        return getClient().create(ApiService::class.java)
    }

}