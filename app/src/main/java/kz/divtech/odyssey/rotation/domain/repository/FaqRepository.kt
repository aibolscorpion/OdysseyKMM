package kz.divtech.odyssey.rotation.domain.repository

import androidx.annotation.WorkerThread
import kotlinx.coroutines.flow.Flow
import kz.divtech.odyssey.rotation.data.local.Dao
import kz.divtech.odyssey.rotation.data.remote.retrofit.RetrofitClient
import kz.divtech.odyssey.rotation.domain.model.help.faq.Faq
import timber.log.Timber

class FaqRepository(private val dao: Dao) {
    val faqList: Flow<List<Faq>> = dao.getFAQ()

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insertFaq(faq: List<Faq>){
        dao.insertFAQ(faq)
    }

    fun searchFaq(searchQuery: String): Flow<List<Faq>> {
        return dao.searchFAQ(searchQuery)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun deleteFaq(){
        dao.deleteFAQ()
    }

    suspend fun getFaqListFromServer() {
        try{
            val response = RetrofitClient.getApiService().getFAQs()
            if(response.isSuccessful){
                insertFaq(response.body()!!)
            }
        }catch (e: Exception){
            Timber.e("exception - ${e.message}")
        }
    }
}