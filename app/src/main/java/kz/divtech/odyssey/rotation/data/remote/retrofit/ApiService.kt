package kz.divtech.odyssey.rotation.data.remote.retrofit

import kz.divtech.odyssey.rotation.domain.model.DeviceInfo
import kz.divtech.odyssey.rotation.domain.model.login.sendsms.CodeResponse
import kz.divtech.odyssey.rotation.domain.model.login.login.AuthRequest
import kz.divtech.odyssey.rotation.domain.model.help.faq.Faq
import kz.divtech.odyssey.rotation.domain.model.help.press_service.news.News
import kz.divtech.odyssey.rotation.domain.model.login.update_phone.UpdatePhoneRequest
import kz.divtech.odyssey.rotation.domain.model.profile.notifications.Notifications
import kz.divtech.odyssey.rotation.domain.model.trips.response.TripResponse
import okhttp3.ResponseBody
import retrofit2.http.*
import kz.divtech.odyssey.rotation.data.remote.result.Result
import kz.divtech.odyssey.rotation.domain.model.OrgInfo
import kz.divtech.odyssey.rotation.domain.model.help.press_service.full_article.FullArticleResponse
import kz.divtech.odyssey.rotation.domain.model.login.login.employee_response.Document
import kz.divtech.odyssey.rotation.domain.model.login.login.employee_response.Employee
import kz.divtech.odyssey.rotation.domain.model.login.login.employee_response.LoginResponse
import kz.divtech.odyssey.rotation.domain.model.login.search_employee.EmployeeResult
import kz.divtech.odyssey.rotation.domain.model.login.sendsms.CodeRequest
import kz.divtech.odyssey.rotation.domain.model.profile.employee.SingleEmployee
import kz.divtech.odyssey.rotation.domain.model.trips.refund.create.RefundApplication
import kz.divtech.odyssey.rotation.domain.model.trips.response.trip.SingleTrip

interface ApiService {
    @GET("user-agreement")
    suspend fun getUserAgreement(): Result<ResponseBody>

    @GET("get-employee-by-phone")
    suspend fun getEmployeeByPhone(@Query("phone") phone: String): Result<EmployeeResult>

    @GET("get-employee-by-iin")
    suspend fun getEmployeeByIIN(@Query("iin") iin: String): Result<EmployeeResult>

    //login
    @POST("send-sms-code")
    suspend fun sendSms(@Body codeRequest: CodeRequest): Result<CodeResponse>

    @POST("login")
    suspend fun login(@Body authRequest : AuthRequest): Result<LoginResponse>

    @POST("update-phone-request")
    suspend fun updatePhoneNumberWithoutAuth(@Body request: UpdatePhoneRequest) : Result<ResponseBody>

    //Trips
    @GET("applications/get-nearest-active-app")
    suspend fun getNearestActiveTrip(): Result<SingleTrip>

    @GET("applications/active")
    suspend fun getActiveTrips(@Query("page") pageIndex: Int,
                               @Query("order_by") orderBy: String = "date",
                               @Query("order_dir") orderDir: String): Result<TripResponse>
    @GET("applications/archive")
    suspend fun getArchiveTrips(@Query("page") pageIndex: Int,
                               @Query("order_by") orderBy: String = "date",
                               @Query("order_dir") orderDir: String): Result<TripResponse>

    @GET("applications/{id}")
    suspend fun getTripById(@Path("id") tripId: Int): Result<SingleTrip>

    //Refund
    @POST("refund-applications")
    suspend fun sendApplicationToRefund(@Body refundApplication: RefundApplication) : Result<Map<String, Int>>

    @POST("refund-applications/{id}/cancel")
    suspend fun cancelRefund(@Path("id") refundId: Int) : Result<ResponseBody>

    //FAQ
    @GET("faqs")
    suspend fun getFAQs() : Result<List<Faq>>

    //Articles
    @GET("articles")
    suspend fun getArticles(@Query("page") pageIndex: Int) : Result<News>

    @GET("articles/{id}")
    suspend fun getArticleById(@Path("id") articleId: Int) : Result<FullArticleResponse>

    @POST("articles/{id}/mark-as-read")
    suspend fun markAsReadArticleById(@Path("id") articleId: Int): Result<ResponseBody>

    //Profile
    @GET("profile")
    suspend fun getEmployeeInfo() : Result<SingleEmployee>

    @POST("profile")
    suspend fun updateEmployee(@Body employee: Employee): Result<ResponseBody>

    @POST("profile/update-document")
    suspend fun updateDocument(@Body document: Document): Result<ResponseBody>

    @POST("profile/update-phone")
    suspend fun updatePhoneNumberWithAuth(@Body codeRequest: CodeRequest): Result<CodeResponse>

    @POST("profile/update-phone-confirm")
    suspend fun updatePhoneConfirm(@Body authRequest : AuthRequest): Result<ResponseBody>

    //Notifications
    @GET("notifications")
    suspend fun getNotifications(@Query("page") pageIndex: Int) : Result<Notifications>

    @POST("notifications/mark-as-read")
    suspend fun markAsReadNotificationById(@Body map: Map<String, String>) : Result<ResponseBody>

    //Logout
    @POST("logout")
    suspend fun logout(): Result<ResponseBody>

    //DeviceInfo
    @POST("fix-device")
    suspend fun sendDeviceInfo(@Body deviceInfo: DeviceInfo): Result<ResponseBody>

    @GET("get-app-info")
    suspend fun getOrgInfo(): Result<OrgInfo>

}