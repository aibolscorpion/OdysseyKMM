package kz.divtech.odyssey.shared.data.local.data_source.org_info

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToOneOrNull
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kz.divtech.odssey.database.OdysseyDatabase
import kz.divtech.odyssey.shared.domain.data_source.OrgInfoDataSource
import kz.divtech.odyssey.shared.domain.model.OrgInfo

class SqlDelightOrgInfoDataSource(db: OdysseyDatabase): OrgInfoDataSource {
    private val queries = db.orgInfoQueries

    override suspend fun getOrgInfo(): Flow<OrgInfo?> {
        return queries.getOrgInfo(mapper = { supportPhone, telegramId, whatsappPhone ->
                OrgInfo(supportPhone = supportPhone,
                    telegramId = telegramId,
                    whatsappPhone = whatsappPhone)
            }
        ).asFlow().mapToOneOrNull(Dispatchers.IO)
    }

    override suspend fun deleteOrgInfo() {
        queries.deleteOrgInfo()
    }

    override suspend fun refreshOrgInfo(orgInfo: OrgInfo) {
        queries.transaction{
            queries.deleteOrgInfo()
            queries.insertOrgInfo(orgInfo.supportPhone, orgInfo.telegramId, orgInfo.whatsappPhone)
        }
    }
}