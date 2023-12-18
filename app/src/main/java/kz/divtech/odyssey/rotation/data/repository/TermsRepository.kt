package kz.divtech.odyssey.rotation.data.repository

import kz.divtech.odyssey.rotation.common.App
import kz.divtech.odyssey.rotation.common.Constants
import kz.divtech.odyssey.rotation.data.local.Dao
import kz.divtech.odyssey.rotation.data.remote.result.Result
import kz.divtech.odyssey.rotation.data.remote.result.isSuccess
import kz.divtech.odyssey.rotation.data.remote.retrofit.ApiService
import kz.divtech.odyssey.rotation.data.remote.retrofit.ProxyApiService
import okhttp3.ResponseBody
import java.io.File

class TermsRepository(private val dao: Dao, private val apiService: ApiService, private val proxyService: ProxyApiService) {
    val uaConfirmed = dao.observeUAConfirmed()

    suspend fun getTermsOfAgreement(): Result<ResponseBody> {
        return proxyService.getUserAgreement()
    }

    suspend fun updateUAConfirm(): Result<ResponseBody>{
        val response = apiService.updateUAConfirm()
        if(response.isSuccess()){
            dao.updateUaConfirmed(true)
        }
        return response
    }

    fun getFile() = File(App.appContext.cacheDir, Constants.TERMS_FILE_NAME)

    fun deleteTermsFile(){
        val fileToDelete = getFile()
        if(fileToDelete.exists()){
            fileToDelete.delete()
        }
    }


}