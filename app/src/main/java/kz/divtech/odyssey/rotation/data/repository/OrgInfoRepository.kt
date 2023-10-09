package kz.divtech.odyssey.rotation.data.repository

import androidx.annotation.WorkerThread
import kz.divtech.odyssey.rotation.data.local.Dao
import kz.divtech.odyssey.rotation.data.remote.result.asSuccess
import kz.divtech.odyssey.rotation.data.remote.result.isSuccess
import kz.divtech.odyssey.rotation.data.remote.retrofit.RetrofitClient
import kz.divtech.odyssey.rotation.domain.model.OrgInfo

class OrgInfoRepository(val dao: Dao) {
    val orgInfo = dao.observeOrgInfo()

    suspend fun deleteOrgInfo(){
        dao.deleteOrgInfo()
    }

    @WorkerThread
    suspend fun refreshOrgInfo(orgInfo: OrgInfo) {
        dao.refreshOrgInfo(orgInfo)
    }

    suspend fun getOrgInfoFromServer(){
        val response = RetrofitClient.getApiProxyService().getOrgInfo()
        if(response.isSuccess()){
            val orgInfo = response.asSuccess().value
            refreshOrgInfo(orgInfo)
        }
    }

}