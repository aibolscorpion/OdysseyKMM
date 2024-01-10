package kz.divtech.odyssey.shared.domain.repository

import kotlinx.coroutines.flow.Flow
import kz.divtech.odyssey.shared.common.Resource
import kz.divtech.odyssey.shared.domain.model.OrgInfo

interface OrgInfoRepository {
    suspend fun getOrgInfo(): Resource<OrgInfo>
    suspend fun getOrgInfoFromDB(): Flow<OrgInfo?>
    suspend fun deleteOrgInfo()
}