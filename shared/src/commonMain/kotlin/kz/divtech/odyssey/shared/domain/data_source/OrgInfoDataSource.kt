package kz.divtech.odyssey.shared.domain.data_source

import kotlinx.coroutines.flow.Flow
import kz.divtech.odyssey.shared.domain.model.OrgInfo

interface OrgInfoDataSource {
    suspend fun getOrgInfo(): Flow<OrgInfo?>
    suspend fun deleteOrgInfo()
    suspend fun refreshOrgInfo(orgInfo: OrgInfo)
}