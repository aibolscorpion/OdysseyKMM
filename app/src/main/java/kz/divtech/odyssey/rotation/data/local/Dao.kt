package kz.divtech.odyssey.rotation.data.local

import androidx.lifecycle.LiveData
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
import kz.divtech.odyssey.rotation.domain.model.login.login.employee_response.Employee
import kz.divtech.odyssey.rotation.domain.model.profile.notifications.Notification
import kz.divtech.odyssey.rotation.domain.model.trips.ActiveTrip
import kz.divtech.odyssey.rotation.domain.model.trips.ArchiveTrip
import kz.divtech.odyssey.rotation.domain.model.trips.response.trip.SingleTrip
import kz.divtech.odyssey.rotation.domain.model.trips.response.trip.Trip

@androidx.room.Dao
interface Dao {
    //Trips
    @Query("SELECT * FROM active_trip " +
            "WHERE (status IN (:statusType)) AND ((direction IN (:direction) AND direction IS NOT NULL) OR (direction IS NULL AND 'to-work' IN (:direction) AND 'to-home' IN (:direction))) " +
            "ORDER BY date ASC")
    fun getActiveTripsSortedByDate(statusType: Array<String>, direction: Array<String>) :  PagingSource<Int, Trip>

    @Query("SELECT * FROM archive_trip " +
            "WHERE (status IN (:statusType)) AND ((direction IN (:direction) AND direction IS NOT NULL) OR (direction IS NULL AND 'to-work' IN (:direction) AND 'to-home' IN (:direction))) " +
            "ORDER BY date DESC")
    fun getArchiveTripsSortedByDate(statusType: Array<String>, direction: Array<String>) : PagingSource<Int, Trip>
    @Query("SELECT * FROM active_trip " +
            "WHERE (status IN (:statusType)) AND ((direction IN (:direction) AND direction IS NOT NULL) OR (direction IS NULL AND 'to-work' IN (:direction) AND 'to-home' IN (:direction))) " +
            "ORDER BY CASE WHEN status = 'issued' THEN 0 " +
            "WHEN status = 'partly' THEN 1 " +
            "WHEN status = 'opened' THEN 2 " +
            "WHEN status = 'returned' THEN 3 " +
            "ELSE 4 end, date ASC")
    fun getActiveTripsSortedByStatus(statusType: Array<String>, direction: Array<String>) :  PagingSource<Int, Trip>

    @Query("SELECT * FROM archive_trip " +
            "WHERE (status IN (:statusType)) AND ((direction IN (:direction) AND direction IS NOT NULL) OR (direction IS NULL AND 'to-work' IN (:direction) AND 'to-home' IN (:direction))) " +
            "ORDER BY CASE WHEN status = 'issued' THEN 0 " +
            "WHEN status = 'partly' THEN 1 " +
            "WHEN status = 'opened' THEN 2 " +
            "WHEN status = 'returned' THEN 3 " +
            "ELSE 4 end, date DESC")
    fun getArchiveTripsSortedByStatus(statusType: Array<String>, direction: Array<String>) : PagingSource<Int, Trip>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertActiveTrips(data: List<ActiveTrip>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertArchiveTrips(data: List<ArchiveTrip>)

    @Query("DELETE FROM active_trip")
    suspend fun deleteActiveTrips()

    @Query("DELETE FROM archive_trip")
    suspend fun deleteArchiveTrips()

    @Transaction
    suspend fun refreshActiveTrips(data: List<ActiveTrip>){
        deleteActiveTrips()
        insertActiveTrips(data)
    }

    @Transaction
    suspend fun refreshArchiveTrips(data: List<ArchiveTrip>){
        deleteArchiveTrips()
        insertArchiveTrips(data)
    }

    @Query("SELECT * FROM nearest_active_trip")
    fun getNearestActiveTrip(): Flow<SingleTrip>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNearestActiveTrip(data: SingleTrip?)

    @Query("DELETE FROM nearest_active_trip")
    suspend fun deleteNearestActiveTrip()

    @Transaction
    suspend fun refreshNearestActiveTrip(data: SingleTrip){
        deleteNearestActiveTrip()
        insertNearestActiveTrip(data)
    }

    //Employee
    @Query("SELECT * FROM employee")
    fun observeEmployee(): LiveData<Employee>

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

    @Query("SELECT ua_confirmed FROM employee")
    fun observeUAConfirmed() : LiveData<Boolean>

    @Query("UPDATE employee SET ua_confirmed = :uaConfirmed")
    suspend fun updateUaConfirmed(uaConfirmed: Boolean)

}