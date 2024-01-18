package kz.divtech.odyssey.shared.domain.repository

import kotlinx.coroutines.flow.Flow
import kz.divtech.odyssey.shared.common.Resource
import kz.divtech.odyssey.shared.domain.model.help.faq.Faq

interface FaqRepository {
    suspend fun getFaqList(): Resource<List<Faq>>
    fun getFaqListFromDb(): Flow<List<Faq>>
    fun searchFaqFromDb(searchQuery: String): List<Faq>
    fun deleteFaq()
}