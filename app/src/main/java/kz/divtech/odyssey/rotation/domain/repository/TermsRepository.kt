package kz.divtech.odyssey.rotation.domain.repository

import kz.divtech.odyssey.rotation.data.local.Dao
import kz.divtech.odyssey.rotation.data.remote.result.Result
import kz.divtech.odyssey.rotation.data.remote.result.isSuccess
import kz.divtech.odyssey.rotation.data.remote.retrofit.RetrofitClient
import okhttp3.ResponseBody

class TermsRepository(val dao: Dao) {
    val uaConfirmed = dao.observeUAConfirmed()

    suspend fun getTermsOfAgreement(): Result<ResponseBody> {
        return RetrofitClient.getApiProxyService().getUserAgreement()
    }

    suspend fun updateUAConfirm(): Result<ResponseBody>{
        val response = RetrofitClient.getApiService().updateUAConfirm()
        if(response.isSuccess()){
            dao.updateUaConfirmed(true)
        }
        return response
    }

}