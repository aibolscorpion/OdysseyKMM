package kz.divtech.odyssey.shared.domain.repository

import kotlinx.coroutines.flow.Flow
import kz.divtech.odyssey.shared.common.Resource
import kz.divtech.odyssey.shared.domain.model.help.faq.Faq

interface FaqRepository {
    suspend fun getFaqList(): Resource<List<Faq>>
    suspend fun getFaqListFromDb(): Flow<List<Faq>>
    suspend fun searchFaqFromDb(searchQuery: String): List<Faq>
    suspend fun deleteFaq()
}