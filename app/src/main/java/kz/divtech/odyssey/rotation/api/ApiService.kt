package kz.divtech.odyssey.rotation.api

import kz.divtech.odyssey.rotation.models.authentication.sendsms.CodeResponse
import kz.divtech.odyssey.rotation.models.authentication.login.Login
import kz.divtech.odyssey.rotation.models.authentication.login.LoginResponse
import kz.divtech.odyssey.rotation.models.authentication.sendsms.PhoneNumber
import kz.divtech.odyssey.rotation.models.help.faq.Faq
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    //login
    @POST("employees/send-code-login")
    fun sendSms(@Body phone: PhoneNumber): Call<CodeResponse>

    @POST("employees/login")
    fun login(@Body login : Login): Call<LoginResponse>

    @GET("employees/get-employee-by-iin")
    fun getEmployeeByIIN()

    @GET("employees/get-employee-by-phone")
    fun getEmployeeByPhone()

    @POST("employees/update-phone")
    fun updatePhoneNumber()

    @POST("employees/login")
    fun updatePhoneNumberByIIN()

    //Trips
    @GET("employees/get-applications")
    fun getTrips()

    //FAQ
    @GET("faqs")
    fun getFAQs() : Call<ArrayList<Faq>>

    //Articles
    @GET("articles")
    fun getArticles()

    @GET("articles/id")
    fun getSpecificArticleById()

    @POST("articles/269/mark-as-read")
    fun markAsReadArticleById()

    //EmployeeInfo
    @GET("employees/info")
    fun getEmployeeInfo()

    //Update Data
    @POST("update/data")
    fun updateData()

    //Documents
    @GET("employees/get-documents")
    fun getDocuments()

    @POST("employees/update-document")
    fun updateDocument()

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