package kz.divtech.odyssey.rotation.domain.repository

import kz.divtech.odyssey.rotation.data.local.Dao
import kz.divtech.odyssey.rotation.data.remote.result.asSuccess
import kz.divtech.odyssey.rotation.data.remote.result.isSuccess
import kz.divtech.odyssey.rotation.data.remote.retrofit.RetrofitClient
import kz.divtech.odyssey.rotation.domain.model.OrgInfo

class OrgInfoRepository(val dao: Dao) {
    private var firstTime = true
    val orgInfo = dao.observeOrgInfo()

    private suspend fun insertOrgInfo(orgInfo: OrgInfo){
        dao.insertOrgInfo(orgInfo)
    }

    suspend fun deleteOrgInfo(){
        dao.deleteOrgInfo()
    }

    suspend fun getOrgInfoFromServer(){
        if(firstTime){
        val response = RetrofitClient.getApiService().getOrgInfo()
        if(response.isSuccess()){
                val orgInfo = response.asSuccess().value
                insertOrgInfo(orgInfo)
                firstTime = false
            }
        }
    }

}