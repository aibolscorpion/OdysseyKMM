package kz.divtech.odyssey.rotation.domain.repository

import androidx.annotation.WorkerThread
import kotlinx.coroutines.flow.Flow
import kz.divtech.odyssey.rotation.data.local.Dao
import kz.divtech.odyssey.rotation.data.remote.retrofit.RetrofitClient
import kz.divtech.odyssey.rotation.domain.model.help.faq.Faq
import timber.log.Timber

class FaqRepository(private val dao: Dao) {
    val faqList: Flow<List<Faq>> = dao.getFAQ()
    private var firstTime = true

    fun searchFaq(searchQuery: String): Flow<List<Faq>> {
        return dao.searchFAQ(searchQuery)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun deleteFaq(){
        dao.deleteFAQ()
    }


    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun refreshFaq(faqList: List<Faq>){
        dao.refreshFaq(faqList)
    }

    suspend fun getFaqListFromServer(isRefreshing: Boolean) {
        try{
            if(firstTime || isRefreshing){
                val response = RetrofitClient.getApiService().getFAQs()
                val faqList = response.body()
                if(response.isSuccessful){
                    refreshFaq(faqList!!)
                    firstTime = false
                }
            }
        }catch (e: Exception){
            Timber.e("exception - ${e.message}")
        }
    }
}