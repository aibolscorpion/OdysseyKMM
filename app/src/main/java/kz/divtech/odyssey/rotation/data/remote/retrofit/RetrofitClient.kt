package kz.divtech.odyssey.rotation.data.remote.retrofit

import kz.divtech.odyssey.rotation.common.Config
import kz.divtech.odyssey.rotation.common.utils.SharedPrefs.fetchDeviceId
import kz.divtech.odyssey.rotation.common.utils.SharedPrefs.fetchUrl
import kz.divtech.odyssey.rotation.common.utils.SharedPrefs.getTokenWithBearer
import kz.divtech.odyssey.rotation.data.remote.retrofit_result.ResultAdapterFactory
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    fun getApiService(): ApiService{
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(Interceptor { chain ->
                val request: Request = chain.request().newBuilder()
                    .addHeader(Config.DEVICE_ID_KEY, fetchDeviceId())
                    .addHeader(Config.AUTHORIZATION_KEY, getTokenWithBearer())
                    .build()
                chain.proceed(request)
            })
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .addInterceptor(UnauthorizedInterceptor())
            .build()

        return Retrofit.Builder().baseUrl(fetchUrl()+ Config.API)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .addCallAdapterFactory(ResultAdapterFactory())
            .build()
            .create(ApiService::class.java)
    }

    fun getProxyService(): ApiService{
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .build()

        return Retrofit.Builder().baseUrl(Config.PROXY_HOST+ Config.API)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .addCallAdapterFactory(ResultAdapterFactory())
            .build()
            .create(ApiService::class.java)
    }
}