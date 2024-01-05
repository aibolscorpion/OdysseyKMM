package kz.divtech.odyssey.shared.domain.repository

import kz.divtech.odyssey.shared.common.Resource
import kz.divtech.odyssey.shared.domain.model.help.faq.Faq

interface FaqRepository {
    suspend fun getFaqList(): Resource<List<Faq>>
    suspend fun getFaqListFromDb(): List<Faq>
    suspend fun searchFaqFromDb(searchQuery: String): List<Faq>
    suspend fun deleteFaq()
}