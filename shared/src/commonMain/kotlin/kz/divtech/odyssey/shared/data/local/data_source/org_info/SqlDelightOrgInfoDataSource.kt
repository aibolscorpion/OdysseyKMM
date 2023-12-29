package kz.divtech.odyssey.shared.data.local.data_source.org_info

import kz.divtech.odssey.database.OdysseyDatabase
import kz.divtech.odyssey.shared.domain.data_source.OrgInfoDataSource
import kz.divtech.odyssey.shared.domain.model.OrgInfo

class SqlDelightOrgInfoDataSource(db: OdysseyDatabase): OrgInfoDataSource {
    private val queries = db.orgInfoQueries
    override suspend fun insertOrgInfo(orgInfo: OrgInfo) {
        queries.insertOrgInfo(orgInfo.supportPhone, orgInfo.telegramId, orgInfo.whatsappPhone)
    }

    override suspend fun getOrgInfo(): OrgInfo? {
        return queries.getOrgInfo().executeAsOneOrNull()?.toOrgInfo()
    }

    override suspend fun deleteOrgInfo() {
        queries.deleteOrgInfo()
    }
}