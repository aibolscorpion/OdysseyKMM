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
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    @GET("user-agreement")
    fun getUserAgreement(): Call<ResponseBody>

    @GET("employees/get-employee-by-phone")
    fun getEmployeeByPhone(@Query("phone") phone: String): Call<EmployeeData>

    //login
    @POST("employees/send-code-login")
    fun sendSms(@Body phone: HashMap<String, String>): Call<CodeResponse>

    @POST("employees/login")
    fun login(@Body login : Login): Call<LoginResponse>

    @GET("employees/get-employee-by-iin")
    fun getEmployeeByIIN(@Query("iin") iin: String): Call<EmployeeData>

    @POST("employees/update-phone")
    fun updatePhoneNumber(@Body request: UpdatePhoneRequest) : Call<ResponseBody>


    //Trips
    @GET("employees/get-applications")
    fun getTrips(): Call<Data>

    //FAQ
    @GET("faqs")
    fun getFAQs() : Call<List<Faq>>

    //Articles
    @GET("articles")
    fun getArticles() : Call<News>

    @GET("articles/{id}")
    fun getSpecificArticleById(@Path("id") articleId: Int) : Call<FullArticle>

    @POST("articles/{id}/mark-as-read")
    fun markAsReadArticleById(@Path("id") articleId: Int): Call<ResponseBody>

    //EmployeeInfo
    @GET("employees/info")
    fun getEmployeeInfo()

    //Update kz.divtech.odyssey.rotation.domain.model.trips.Data
    @POST("employees/update-data")
    fun updateData(@Body employee: Employee): Call<ResponseBody>

    //Documents
    @GET("employees/get-documents")
    fun getDocuments(): Call<Documents>

    @POST("employees/update-document")
    fun updateDocument(@Body document: Document): Call<ResponseBody>

    //Notifications
    @GET("notifications")
    fun getNotifications()

    @GET("notifications/mark-as-read")
    fun markAsReadNotificationById()

    //Logout
    @POST("logout")
    fun logout(): Call<ResponseBody>

    //DeviceInfo
    @POST("employees/fix")
    fun saveDeviceInfo()

}