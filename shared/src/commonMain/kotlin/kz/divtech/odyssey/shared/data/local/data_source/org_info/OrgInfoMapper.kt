package kz.divtech.odyssey.shared.data.local.data_source.org_info

import database.OrgInfo


fun OrgInfo.toOrgInfo(): kz.divtech.odyssey.shared.domain.model.OrgInfo{
    return kz.divtech.odyssey.shared.domain.model.OrgInfo(
        supportPhone = supportPhone,
        whatsappPhone = whatsappPhone,
        telegramId = telegramId
    )
}