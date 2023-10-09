package kz.divtech.odyssey.rotation.data.remote.retrofit

import kz.divtech.odyssey.rotation.common.App
import kz.divtech.odyssey.rotation.common.Config
import kz.divtech.odyssey.rotation.data.remote.retrofit_result.ResultAdapterFactory
import kz.divtech.odyssey.rotation.common.utils.SharedPrefs.fetchDeviceId
import kz.divtech.odyssey.rotation.common.utils.SharedPrefs.fetchUrl
import kz.divtech.odyssey.rotation.common.utils.SharedPrefs.getTokenWithBearer
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient{

    private fun getClient(): Retrofit {
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(Interceptor { chain ->
                val request: Request = chain.request().newBuilder()
                    .addHeader(Config.DEVICE_ID_KEY, App.appContext.fetchDeviceId())
                    .addHeader(Config.AUTHORIZATION_KEY, App.appContext.getTokenWithBearer())
                    .build()
                chain.proceed(request)
            })
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .addInterceptor(UnauthorizedInterceptor())
            .build()

        return Retrofit.Builder().baseUrl(App.appContext.fetchUrl()+ Config.API)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .addCallAdapterFactory(ResultAdapterFactory())
            .build()
    }

    fun getApiService() : ApiService {
        return getClient().create(ApiService::class.java)
    }

    private fun getProxyClient(): Retrofit {
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .build()

        return Retrofit.Builder().baseUrl(Config.PROXY_HOST+ Config.API)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .addCallAdapterFactory(ResultAdapterFactory())
            .build()
    }

    fun getApiProxyService() : ApiService {
        return getProxyClient().create(ApiService::class.java)
    }

}