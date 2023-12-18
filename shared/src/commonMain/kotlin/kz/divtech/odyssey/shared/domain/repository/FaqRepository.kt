package kz.divtech.odyssey.shared.domain.repository

import kz.divtech.odyssey.shared.common.Resource
import kz.divtech.odyssey.shared.domain.model.Faq

interface FaqRepository {
    suspend fun getFaqList(): Resource<List<Faq>>
}