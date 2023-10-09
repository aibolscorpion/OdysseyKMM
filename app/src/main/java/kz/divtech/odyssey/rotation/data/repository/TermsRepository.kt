package kz.divtech.odyssey.rotation.data.repository

import kz.divtech.odyssey.rotation.app.App
import kz.divtech.odyssey.rotation.app.Constants
import kz.divtech.odyssey.rotation.data.local.Dao
import kz.divtech.odyssey.rotation.data.remote.result.Result
import kz.divtech.odyssey.rotation.data.remote.result.isSuccess
import kz.divtech.odyssey.rotation.data.remote.retrofit.RetrofitClient
import okhttp3.ResponseBody
import java.io.File

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

    fun getFile() = File(App.appContext.cacheDir, Constants.TERMS_FILE_NAME)

    fun deleteTermsFile(){
        val fileToDelete = getFile()
        if(fileToDelete.exists()){
            fileToDelete.delete()
        }
    }


}