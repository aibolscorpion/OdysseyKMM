package kz.divtech.odyssey.shared.domain.repository

import io.ktor.client.statement.HttpResponse
import kz.divtech.odyssey.shared.common.Resource
import kz.divtech.odyssey.shared.domain.model.auth.login.AuthRequest
import kz.divtech.odyssey.shared.domain.model.auth.login.employee_response.LoginResponse
import kz.divtech.odyssey.shared.domain.model.auth.sendsms.CodeResponse

interface LoginRepository {
    suspend fun requestSmsCode(phoneNumber: String): Resource<CodeResponse>
    suspend fun login(authRequest: AuthRequest): Resource<LoginResponse>
    suspend fun logout(): Resource<HttpResponse>
}