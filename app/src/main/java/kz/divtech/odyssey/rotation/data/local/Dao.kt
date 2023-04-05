package kz.divtech.odyssey.rotation.data.local

import androidx.paging.PagingSource
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow
import kz.divtech.odyssey.rotation.domain.model.OrgInfo
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
    @Query("SELECT * FROM trip WHERE date > date('now') ORDER BY date ASC")
    fun getActiveTripsSortedByDate() :  PagingSource<Int, Trip>

    @Query("SELECT * FROM trip WHERE date < date('now') ORDER BY date DESC")
    fun getArchiveTripsSortedByDate() : PagingSource<Int, Trip>

    @Query("SELECT * FROM trip WHERE date > date('now') " +
        "ORDER BY case when status = 'issued' then 0 else 1 end, status")
    fun getActiveTripsSortedByStatus() :  PagingSource<Int, Trip>

    @Query("SELECT * FROM trip WHERE date < date('now') " +
            "ORDER BY case when status = 'issued' then 0 else 1 end, status")
    fun getArchiveTripsSortedByStatus() : PagingSource<Int, Trip>

    @Query("SELECT * FROM trip WHERE date > date('now') AND (status IN (:statusType))" +
            " AND (direction IN (:direction))")
    fun getFilteredActiveTrips(statusType: Array<String>, direction: Array<String>) : PagingSource<Int, Trip>

    @Query("SELECT * FROM trip WHERE date < date('now') AND (status IN (:statusType))" +
            " AND (direction IN (:direction))")
    fun getFilteredArchiveTrips(statusType: Array<String>, direction: Array<String>) : PagingSource<Int, Trip>

    @Query("SELECT * FROM trip WHERE date > date('now') ORDER BY date ASC LIMIT 1")
    fun observeNearestActiveTrip() :  Flow<Trip>

    @Query("SELECT * FROM trip WHERE id=:id")
    fun observeTripById(id: Int): Flow<Trip>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrips(data: List<Trip>)

    @Query("DELETE FROM trip")
    suspend fun deleteAllTrips()

    @Query("DELETE FROM trip WHERE date > date('now')")
    suspend fun deleteActiveTrips()

    @Query("DELETE FROM trip WHERE date < date('now')")
    suspend fun deleteArchiveTrips()


    @Transaction
    suspend fun refreshAllTrips(data: List<Trip>){
        deleteAllTrips()
        insertTrips(data)
    }

    @Transaction
    suspend fun refreshTrips(isActive: Boolean, data: List<Trip>){
        if(isActive)  deleteActiveTrips()
        else  deleteArchiveTrips()
        insertTrips(data)
    }

    //Employee
    @Query("SELECT * FROM employee")
    fun observeEmployee(): Flow<Employee>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEmployee(employee: Employee)

    @Query("DELETE FROM employee")
    suspend fun deleteEmployee()

    //FAQ
    @Query("SELECT * FROM faq")
    fun observeFaqList(): Flow<List<Faq>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFAQ(faqList: List<Faq>)

    @Query("DELETE FROM faq")
    suspend fun deleteFAQ()

    @Query("SELECT * FROM faq WHERE question LIKE '%'||:searchQuery||'%' OR answer LIKE '%'||:searchQuery||'%'")
    suspend fun searchFAQ(searchQuery: String) : List<Faq>

    @Transaction
    suspend fun refreshFaq(faqList: List<Faq>){
        deleteFAQ()
        insertFAQ(faqList)
    }

    //Documents
    @Query("SELECT * FROM document")
    fun observeDocuments() : Flow<List<Document>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDocuments(documents: List<Document>)

    @Query("DELETE FROM document")
    suspend fun deleteDocuments()

    //News
    @Query("SELECT * FROM article ORDER BY published_on DESC")
    fun getNewsPagingSource(): PagingSource<Int, Article>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNews(articleList: List<Article>)

    @Query("DELETE FROM article")
    suspend fun deleteNews()

    @Query("SELECT * FROM article WHERE title LIKE '%'||:searchQuery||'%' OR" +
            " short_content LIKE '%'||:searchQuery||'%'")
    suspend fun searchArticle(searchQuery: String): List<Article>

    @Transaction
    suspend fun refreshNews(articleList: List<Article>){
        deleteNews()
        insertNews(articleList)
    }

    //FullArticle
    @Query("SELECT * FROM full_article WHERE id=:id")
    fun observeArticleById(id: Int) : Flow<FullArticle>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFullArticle(fullArticle: FullArticle)

    @Query("DELETE FROM full_article")
    suspend fun deleteFullArticles()

    //Notifications
    @Query("SELECT * FROM notification ORDER BY created_at DESC")
    fun getNotificationPagingSource() : PagingSource<Int, Notification>

    @Query("SELECT * FROM notification ORDER BY created_at DESC LIMIT 3")
    fun observeThreeNotifications(): Flow<List<Notification>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNotifications(notificationList: List<Notification>)

    @Query("DELETE FROM notification")
    suspend fun deleteNotifications()

    @Transaction
    suspend fun refreshNotifications(notificationList: List<Notification>){
        deleteNotifications()
        insertNotifications(notificationList)
    }

    //Organization Info
    @Query("SELECT * FROM orgInfo")
    fun observeOrgInfo(): Flow<OrgInfo>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrgInfo(orgInfo: OrgInfo)

    @Transaction
    suspend fun refreshOrgInfo(orgInfo: OrgInfo){
        deleteOrgInfo()
        insertOrgInfo(orgInfo)
    }

    @Query("DELETE FROM orgInfo")
    suspend fun deleteOrgInfo()

}