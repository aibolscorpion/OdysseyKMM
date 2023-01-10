package kz.divtech.odyssey.rotation.data.remote.retrofit

import kz.divtech.odyssey.rotation.domain.model.login.sendsms.CodeResponse
import kz.divtech.odyssey.rotation.domain.model.login.login.Login
import kz.divtech.odyssey.rotation.domain.model.login.login.LoginResponse
import kz.divtech.odyssey.rotation.domain.model.help.faq.Faq
import kz.divtech.odyssey.rotation.domain.model.help.press_service.full_article.FullArticle
import kz.divtech.odyssey.rotation.domain.model.help.press_service.news.News
import kz.divtech.odyssey.rotation.domain.model.login.login.Employee
import kz.divtech.odyssey.rotation.domain.model.login.search_by_iin.EmployeeData
import kz.divtech.odyssey.rotation.domain.model.login.update_phone.UpdatePhoneRequest
import kz.divtech.odyssey.rotation.domain.model.profile.documents.Document
import kz.divtech.odyssey.rotation.domain.model.profile.documents.Documents
import kz.divtech.odyssey.rotation.domain.model.trips.Data
import okhttp3.ResponseBody
import retrofit2.http.*
import retrofit2.Response

interface ApiService {

    @GET("user-agreement")
    suspend fun getUserAgreement(): Response<ResponseBody>

    @GET("employees/get-employee-by-phone")
    suspend fun getEmployeeByPhone(@Query("phone") phone: String): Response<EmployeeData>

    //login
    @POST("employees/send-code-login")
    suspend fun sendSms(@Body phone: HashMap<String, String>): Response<CodeResponse>

    @POST("employees/login")
    suspend fun login(@Body login : Login): Response<LoginResponse>

    @GET("employees/get-employee-by-iin")
    suspend fun getEmployeeByIIN(@Query("iin") iin: String): Response<EmployeeData>

    @POST("employees/update-phone")
    suspend fun updatePhoneNumber(@Body request: UpdatePhoneRequest) : Response<ResponseBody>


    //Trips
    @GET("employees/get-applications")
    suspend fun getTrips(): Response<Data>

    //FAQ
    @GET("faqs")
    suspend fun getFAQs() : Response<List<Faq>>

    //Articles
    @GET("articles")
    suspend fun getArticles() : Response<News>

    @GET("articles/{id}")
    suspend fun getSpecificArticleById(@Path("id") articleId: Int) : Response<FullArticle>

    @POST("articles/{id}/mark-as-read")
    suspend fun markAsReadArticleById(@Path("id") articleId: Int): Response<ResponseBody>

    //EmployeeInfo
    @GET("employees/info")
    fun getEmployeeInfo() : Response<Employee>

    //Update kz.divtech.odyssey.rotation.domain.model.trips.Data
    @POST("employees/update-data")
    suspend fun updateData(@Body employee: Employee): Response<ResponseBody>

    //Documents
    @GET("employees/get-documents")
    suspend fun getDocuments(): Response<Documents>

    @POST("employees/update-document")
    suspend fun updateDocument(@Body document: Document): Response<ResponseBody>

    //Notifications
    @GET("notifications")
    fun getNotifications()

    @GET("notifications/mark-as-read")
    fun markAsReadNotificationById()

    //Logout
    @POST("logout")
    suspend fun logout(): Response<ResponseBody>

    //DeviceInfo
    @POST("employees/fix")
    fun saveDeviceInfo()

}