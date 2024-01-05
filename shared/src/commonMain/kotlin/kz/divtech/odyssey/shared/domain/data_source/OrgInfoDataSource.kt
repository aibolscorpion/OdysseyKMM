package kz.divtech.odyssey.shared.domain.data_source

import kz.divtech.odyssey.shared.domain.model.OrgInfo

interface OrgInfoDataSource {
    suspend fun getOrgInfo(): OrgInfo?
    suspend fun deleteOrgInfo()
    suspend fun refreshOrgInfo(orgInfo: OrgInfo)
}