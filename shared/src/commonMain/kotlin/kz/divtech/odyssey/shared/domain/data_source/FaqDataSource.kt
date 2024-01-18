package kz.divtech.odyssey.shared.domain.data_source

import kotlinx.coroutines.flow.Flow
import kz.divtech.odyssey.shared.domain.model.help.faq.Faq

interface FaqDataSource {
    fun getFaq(): Flow<List<Faq>>
    fun searchFaq(searchQuery: String): List<Faq>
    fun insertFAQ(faqList: List<Faq>)
    fun deleteFaq()
    fun refreshFaq(faqList: List<Faq>)
}