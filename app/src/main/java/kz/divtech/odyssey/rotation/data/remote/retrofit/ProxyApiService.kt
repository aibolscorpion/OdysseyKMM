package kz.divtech.odyssey.rotation.data.remote.retrofit

import kz.divtech.odyssey.rotation.domain.model.help.faq.Faq
import okhttp3.ResponseBody
import retrofit2.http.*
import kz.divtech.odyssey.rotation.data.remote.result.Result
import kz.divtech.odyssey.rotation.domain.model.OrgInfo
import kz.divtech.odyssey.rotation.domain.model.login.search_employee.EmployeeResult

interface ProxyApiService {
    @GET("user-agreement")
    suspend fun getUserAgreement(): Result<ResponseBody>

    @GET("get-employee-by-phone")
    suspend fun getEmployeeByPhone(@Query("phone") phone: String): Result<EmployeeResult>

    @GET("get-employee-by-iin")
    suspend fun getEmployeeByIIN(@Query("iin") iin: String): Result<EmployeeResult>

    @GET("faqs")
    suspend fun getFAQs() : Result<List<Faq>>

    @GET("get-app-info")
    suspend fun getOrgInfo(): Result<OrgInfo>

}