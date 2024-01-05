package kz.divtech.odyssey.shared.domain.data_source

import kz.divtech.odyssey.shared.domain.model.help.faq.Faq

interface FaqDataSource {
    suspend fun getFaq(): List<Faq>
    suspend fun searchFaq(searchQuery: String): List<Faq>
    suspend fun insertFAQ(faqList: List<Faq>)
    suspend fun deleteFaq()
    suspend fun refreshFaq(faqList: List<Faq>)
}