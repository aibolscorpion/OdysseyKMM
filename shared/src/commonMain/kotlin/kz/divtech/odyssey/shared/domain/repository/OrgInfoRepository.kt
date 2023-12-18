package kz.divtech.odyssey.shared.domain.repository

import kz.divtech.odyssey.shared.common.Resource
import kz.divtech.odyssey.shared.domain.model.OrgInfo

interface OrgInfoRepository {
    suspend fun getOrgInfo(): Resource<OrgInfo>
}