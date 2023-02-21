package kz.divtech.odyssey.rotation.data.remote.retrofit

import kz.divtech.odyssey.rotation.app.App
import kz.divtech.odyssey.rotation.app.Config
import kz.divtech.odyssey.rotation.data.remote.retrofit_result.ResultAdapterFactory
import kz.divtech.odyssey.rotation.utils.SharedPrefs
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
                    .addHeader(Config.DEVICE_ID_KEY, SharedPrefs.fetchDeviceId(App.appContext))
                    .addHeader(Config.AUTHORIZATION_KEY, SharedPrefs.getTokenWithBearer(App.appContext))
                    .build()
                chain.proceed(request)
            })
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .addInterceptor(UnauthorizedInterceptor())
            .build()

        return Retrofit.Builder().baseUrl(SharedPrefs.fetchUrl(App.appContext)+Config.API)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .addCallAdapterFactory(ResultAdapterFactory())
            .build()
    }

    fun getApiService() : ApiService {
        return getClient().create(ApiService::class.java)
    }

}