package kz.divtech.odyssey.shared.data.repository

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.url
import io.ktor.utils.io.errors.IOException
import kz.divtech.odyssey.shared.common.Resource
import kz.divtech.odyssey.shared.data.remote.HttpRoutes
import kz.divtech.odyssey.shared.domain.data_source.OrgInfoDataSource
import kz.divtech.odyssey.shared.domain.model.OrgInfo
import kz.divtech.odyssey.shared.domain.repository.OrgInfoRepository

class OrgInfoRepositoryImpl(private val httpClient: HttpClient,
                            private val dataSource: OrgInfoDataSource): OrgInfoRepository {
    override suspend fun getOrgInfo(): Resource<OrgInfo> {
        return try {
            val result: OrgInfo = httpClient.get {
                url(HttpRoutes.GET_ORG_INFO)
            }.body()
            dataSource.refreshOrgInfo(orgInfo = result)
            Resource.Success(data = result)
        }catch (e: IOException){
            Resource.Error.IOException(e.message.toString())
        }catch (e: Exception){
            Resource.Error.Exception(e.message.toString())
        }
    }

    override suspend fun getOrgInfoFromDB(): OrgInfo?{
        return dataSource.getOrgInfo()
    }

    override suspend fun deleteOrgInfo(){
        dataSource.deleteOrgInfo()
    }




}