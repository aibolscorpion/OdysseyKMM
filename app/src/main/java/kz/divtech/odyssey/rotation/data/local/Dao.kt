package kz.divtech.odyssey.rotation.data.local

import androidx.paging.PagingSource
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow
import kz.divtech.odyssey.rotation.domain.model.help.faq.Faq
import kz.divtech.odyssey.rotation.domain.model.help.press_service.full_article.FullArticle
import kz.divtech.odyssey.rotation.domain.model.help.press_service.news.Article
import kz.divtech.odyssey.rotation.domain.model.login.login.Employee
import kz.divtech.odyssey.rotation.domain.model.profile.documents.Document
import kz.divtech.odyssey.rotation.domain.model.profile.notifications.Notification
import kz.divtech.odyssey.rotation.domain.model.trips.Trip

@androidx.room.Dao
interface Dao {
    //Trips
    @Query("SELECT * FROM trip")
    fun getTrips(): Flow<List<Trip>>

    @Query("SELECT * FROM trip WHERE id=:id")
    fun getTripById(id: Int): Flow<Trip>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrips(data: List<Trip>)

    @Query("DELETE FROM trip")
    suspend fun deleteTrips()

    @Transaction
    suspend fun refreshTrips(data: List<Trip>){
        deleteTrips()
        insertTrips(data)
    }

    //Employee
    @Query("SELECT * FROM employee")
    fun getEmployee(): Flow<Employee>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEmployee(employee: Employee)

    @Query("DELETE FROM employee")
    suspend fun deleteEmployee()

    //FAQ
    @Query("SELECT * FROM faq")
    fun getFAQ(): Flow<List<Faq>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFAQ(faqList: List<Faq>)

    @Query("DELETE FROM faq")
    suspend fun deleteFAQ()

    @Query("SELECT * FROM faq WHERE question LIKE '%'||:searchQuery||'%' OR answer LIKE '%'||:searchQuery||'%'")
    fun searchFAQ(searchQuery: String) : Flow<List<Faq>>

    @Transaction
    suspend fun refreshFaq(faqList: List<Faq>){
        deleteFAQ()
        insertFAQ(faqList)
    }

    //Documents
    @Query("SELECT * FROM document")
    fun getDocuments() : Flow<List<Document>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDocuments(documents: List<Document>)

    @Query("DELETE FROM document")
    suspend fun deleteDocuments()

    //News
    @Query("SELECT * FROM article")
    fun getNews(): Flow<List<Article>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNews(articleList: List<Article>)

    @Query("DELETE FROM article")
    suspend fun deleteNews()

    @Query("SELECT * FROM article WHERE title LIKE '%'||:searchQuery||'%' OR" +
            " short_content LIKE '%'||:searchQuery||'%'")
    fun searchArticle(searchQuery: String): Flow<List<Article>>

    @Transaction
    suspend fun refreshNews(articleList: List<Article>){
        deleteNews()
        insertNews(articleList)
    }

    //FullArticle
    @Query("SELECT * FROM full_article WHERE id=:id")
    fun getArticleById(id: Int) : Flow<FullArticle>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFullArticle(fullArticle: FullArticle)

    @Query("DELETE FROM full_article")
    suspend fun deleteFullArticles()

    //Notifications
    @Query("SELECT * FROM notification")
    fun getNotificationPagingSource() : PagingSource<Int, Notification>

    @Query("SELECT * FROM notification LIMIT 3")
    fun getThreeNotifications(): Flow<List<Notification>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNotifications(notificationList: List<Notification>)

    @Query("DELETE FROM notification")
    suspend fun deleteNotifications()

    @Transaction
    suspend fun refreshNotifications(notificationList: List<Notification>){
        deleteNotifications()
        insertNotifications(notificationList)
    }

}