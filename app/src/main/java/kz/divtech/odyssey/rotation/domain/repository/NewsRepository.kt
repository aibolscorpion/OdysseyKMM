package kz.divtech.odyssey.rotation.domain.repository

import androidx.annotation.WorkerThread
import kotlinx.coroutines.flow.Flow
import kz.divtech.odyssey.rotation.app.Constants
import kz.divtech.odyssey.rotation.data.local.Dao
import kz.divtech.odyssey.rotation.data.remote.retrofit.RetrofitClient
import kz.divtech.odyssey.rotation.domain.model.help.press_service.news.Article
import timber.log.Timber

class NewsRepository(private val dao: Dao) {

    val news: Flow<List<Article>> = dao.getNews()

    @WorkerThread
    @Suppress("RedundantSuspendModifier")
    suspend fun insertNews(news: List<Article>){
        dao.insertNews(news)
    }

    fun searchArticlesFromDB(searchQuery: String): Flow<List<Article>> = dao.searchArticle(searchQuery)


    @WorkerThread
    @Suppress("RedundantSuspendModifier")
    suspend fun deleteNews(){
        dao.deleteNews()
    }

    suspend fun getNewsFromServer(){
        try{
            val response = RetrofitClient.getApiService().getArticles()
            val news = response.body()?.data
            when(response.code()){
                Constants.SUCCESS_CODE -> {
                    if(response.body()?.data!!.isNotEmpty()){
                        deleteNews()
                        insertNews(news!!)
                    }
                }
            }
        }catch (e: Exception){
            Timber.e("exception - ${e.message}")
        }
    }

}