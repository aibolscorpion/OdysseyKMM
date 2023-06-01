package kz.divtech.odyssey.rotation.domain.repository

import androidx.annotation.WorkerThread
import kotlinx.coroutines.flow.Flow
import kz.divtech.odyssey.rotation.data.local.Dao
import kz.divtech.odyssey.rotation.data.remote.result.*
import kz.divtech.odyssey.rotation.data.remote.retrofit.RetrofitClient
import kz.divtech.odyssey.rotation.domain.model.help.faq.Faq

class FaqRepository(private val dao: Dao) {
    val faqList: Flow<List<Faq>> = dao.observeFaqList()
    private var firstTime = true

    suspend fun searchFaq(searchQuery: String) = dao.searchFAQ(searchQuery)

    @WorkerThread
    suspend fun deleteFaq() {
        dao.deleteFAQ()
    }


    @WorkerThread
    suspend fun refreshFaq(faqList: List<Faq>) {
        dao.refreshFaq(faqList)
    }

    suspend fun getFaqListFromServer(isRefreshing: Boolean): Result<List<Faq>>? {
        var response: Result<List<Faq>>? = null
        if (firstTime || isRefreshing) {
            response = RetrofitClient.getApiService().getFAQs()
            if (response.isSuccess()) {
                val faqList = response.asSuccess().value
                refreshFaq(faqList)
                firstTime = false
            }
        }
        return response
    }
}