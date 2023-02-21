package kz.divtech.odyssey.rotation.data.remote.retrofit

import kz.divtech.odyssey.rotation.app.Constants
import okhttp3.Interceptor
import okhttp3.Response
import org.greenrobot.eventbus.EventBus

class UnauthorizedInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val response = chain.proceed(chain.request())
        if(response.code == Constants.UNAUTHORIZED_CODE){
            EventBus.getDefault().post(UnauthorizedEvent)
        }
        return response
    }
}
