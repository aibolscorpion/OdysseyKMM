package kz.divtech.odyssey.shared.domain.data_source

import kotlinx.coroutines.flow.Flow
import kz.divtech.odyssey.shared.domain.model.OrgInfo

interface OrgInfoDataSource {
    fun getOrgInfo(): Flow<OrgInfo?>
    fun deleteOrgInfo()
    fun refreshOrgInfo(orgInfo: OrgInfo)
}