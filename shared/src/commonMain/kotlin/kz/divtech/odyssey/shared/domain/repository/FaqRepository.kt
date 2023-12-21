package kz.divtech.odyssey.shared.domain.repository

import kz.divtech.odyssey.shared.common.Resource
import kz.divtech.odyssey.shared.domain.model.help.faq.Faq

interface FaqRepository {
    suspend fun getFaqList(): Resource<List<Faq>>
}