package kz.divtech.odyssey.rotation.data.remote.retrofit

import kz.divtech.odyssey.rotation.domain.model.DeviceInfo
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
import kz.divtech.odyssey.rotation.domain.model.profile.notifications.Notifications
import kz.divtech.odyssey.rotation.domain.model.trips.Data
import okhttp3.ResponseBody
import retrofit2.http.*
import kz.divtech.odyssey.rotation.data.remote.result.Result
import kz.divtech.odyssey.rotation.domain.model.OrgInfo
import kz.divtech.odyssey.rotation.domain.model.trips.RefundApplication

interface ApiService {

    @GET("user-agreement")
    suspend fun getUserAgreement(): Result<ResponseBody>

    @GET("employees/get-employee-by-phone")
    suspend fun getEmployeeByPhone(@Query("phone") phone: String): Result<EmployeeData>

    //login
    @POST("employees/send-code-login")
    suspend fun sendSms(@Body phone: Map<String, String>): Result<CodeResponse>

    @POST("employees/login")
    suspend fun login(@Body login : Login): Result<LoginResponse>

    @GET("employees/get-employee-by-iin")
    suspend fun getEmployeeByIIN(@Query("iin") iin: String): Result<EmployeeData>

    @POST("employees/update-phone")
    suspend fun updatePhoneNumber(@Body request: UpdatePhoneRequest) : Result<ResponseBody>

    //Trips
    @GET("employees/get-applications")
    suspend fun getTrips(@Query("page") pageIndex: Int,
                        @Query("order_by") orderBy: String = "date",
                        @Query("order_dir") orderDir: String): Result<Data>

    //Refund
    @GET("employees/refund-applications")
    suspend fun getRefundApplications()

    @POST("employees/refund-applications")
    suspend fun sendApplicationToRefund(@Body refundApplication: RefundApplication) : Result<Map<String, Int>>

    @POST("employees/refund-applications/{id}/cancel")
    suspend fun cancelRefund(@Path("id") refundId: Int) : Result<ResponseBody>

    //FAQ
    @GET("faqs")
    suspend fun getFAQs() : Result<List<Faq>>

    //Articles
    @GET("articles")
    suspend fun getArticles(@Query("page") pageIndex: Int) : Result<News>

    @GET("articles/{id}")
    suspend fun getSpecificArticleById(@Path("id") articleId: Int) : Result<FullArticle>

    @POST("articles/{id}/mark-as-read")
    suspend fun markAsReadArticleById(@Path("id") articleId: Int): Result<ResponseBody>

    //EmployeeInfo
    @GET("employees/info")
    suspend fun getEmployeeInfo() : Result<Employee>

    //Update kz.divtech.odyssey.rotation.domain.model.trips.Data
    @POST("employees/update-data")
    suspend fun updateData(@Body employee: Employee): Result<ResponseBody>

    //Documents
    @GET("employees/get-documents")
    suspend fun getDocuments(): Result<Documents>

    @POST("employees/update-document")
    suspend fun updateDocument(@Body document: Document): Result<ResponseBody>

    //Notifications
    @GET("notifications")
    suspend fun getNotifications(@Query("page") pageIndex: Int) : Result<Notifications>

    @POST("notifications/mark-as-read")
    suspend fun markAsReadNotificationById(@Body map: Map<String, String>) : Result<ResponseBody>

    //Logout
    @POST("logout")
    suspend fun logout(): Result<ResponseBody>

    //DeviceInfo
    @POST("employees/fix")
    suspend fun sendDeviceInfo(@Body deviceInfo: DeviceInfo): Result<ResponseBody>

    @GET("get-app-info")
    suspend fun getOrgInfo(): Result<OrgInfo>

}