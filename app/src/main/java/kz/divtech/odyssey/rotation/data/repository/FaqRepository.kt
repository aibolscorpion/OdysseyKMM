package kz.divtech.odyssey.rotation.data.repository

import androidx.annotation.WorkerThread
import kotlinx.coroutines.flow.Flow
import kz.divtech.odyssey.rotation.data.local.Dao
import kz.divtech.odyssey.rotation.data.remote.result.*
import kz.divtech.odyssey.rotation.data.remote.retrofit.ApiService
import kz.divtech.odyssey.rotation.domain.model.help.faq.Faq

class FaqRepository(private val dao: Dao, private val apiService: ApiService) {
    val faqList: Flow<List<Faq>> = dao.observeFaqList()

    suspend fun searchFaq(searchQuery: String) = dao.searchFAQ(searchQuery)

    @WorkerThread
    suspend fun deleteFaq() {
        dao.deleteFAQ()
    }


    @WorkerThread
    suspend fun refreshFaq(faqList: List<Faq>) {
        dao.refreshFaq(faqList)
    }

    suspend fun getFaqListFromServer(): Result<List<Faq>> {
        val response: Result<List<Faq>> = apiService.getFAQs()
        if (response.isSuccess()) {
            val faqList = response.asSuccess().value
            refreshFaq(faqList)
        }
        return response
    }
}