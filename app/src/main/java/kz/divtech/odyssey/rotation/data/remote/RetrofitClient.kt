package kz.divtech.odyssey.rotation.data.remote

import kz.divtech.odyssey.rotation.app.Config
import kz.divtech.odyssey.rotation.utils.SharedPrefs
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
                        .addHeader(Config.DEVICE_ID_KEY, SharedPrefs().fetchDeviceId())
                        .addHeader(Config.AUTHORIZATION_KEY, SharedPrefs().getTokenWithBearer())
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

    fun getApiService() : ApiService {
        return getClient().create(ApiService::class.java)
    }

}