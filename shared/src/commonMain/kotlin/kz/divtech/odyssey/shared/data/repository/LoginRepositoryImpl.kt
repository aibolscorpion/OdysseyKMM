package kz.divtech.odyssey.shared.data.repository

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.request.url
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.utils.io.errors.IOException
import kz.divtech.odyssey.shared.common.Config
import kz.divtech.odyssey.shared.common.Resource
import kz.divtech.odyssey.shared.data.local.DataStoreManager
import kz.divtech.odyssey.shared.data.remote.HttpRoutes
import kz.divtech.odyssey.shared.domain.model.auth.login.AuthRequest
import kz.divtech.odyssey.shared.domain.model.auth.login.employee_response.LoginResponse
import kz.divtech.odyssey.shared.domain.model.auth.sendsms.CodeRequest
import kz.divtech.odyssey.shared.domain.model.auth.sendsms.CodeResponse
import kz.divtech.odyssey.shared.domain.repository.LoginRepository

class LoginRepositoryImpl(private val httpClient: HttpClient,
                          private val dataStoreManager: DataStoreManager): LoginRepository {
    override suspend fun requestSmsCode(phoneNumber: String): Resource<CodeResponse> {
        val codeRequest = CodeRequest(phoneNumber, Config.IS_TEST)
        return try {
            val response: HttpResponse = httpClient.post{
                url(HttpRoutes(dataStoreManager).requestSmsCode())
                contentType(ContentType.Application.Json)
                setBody(codeRequest)
            }
            if(response.status.value == 200){
                val result: CodeResponse = response.body()
                Resource.Success(result)
            }else{
                Resource.Error(message = response.body())
            }
        }catch (e: ClientRequestException){
            Resource.Error(message = e.response.status.description)
        }catch (e: ServerResponseException){
            Resource.Error(message = e.response.status.description)
        }catch (e: IOException){
            Resource.Error(message = "${e.message}")
        }catch (e: Exception){
            Resource.Error(message = "${e.message}")
        }
    }

    override suspend fun login(authRequest: AuthRequest): Resource<LoginResponse> {
        return try {
             val result: LoginResponse = httpClient.post{
                url(HttpRoutes(dataStoreManager).login())
                contentType(ContentType.Application.Json)
                setBody(authRequest)
            }.body()
            dataStoreManager.saveAuthToken(result.token)
            dataStoreManager.saveOrganizationName(result.organization.name)
            Resource.Success(data = result)
        }catch (e: ClientRequestException){
            Resource.Error(message = e.response.status.description)
        }catch (e: ServerResponseException){
            Resource.Error(message = e.response.status.description)
        }catch (e: IOException){
            Resource.Error(message = "${e.message}")
        }catch (e: IOException){
            Resource.Error(message = "${e.message}")
        }
    }

    override suspend fun logout(): Resource<HttpResponse> {
        return try {
            val result = httpClient.post{
                url(HttpRoutes(dataStoreManager).logout())
            }
            Resource.Success(data = result)
        }catch (e: ClientRequestException){
            Resource.Error(message = e.response.status.description)
        }catch (e: ServerResponseException){
            Resource.Error(message = e.response.status.description)
        }catch (e: IOException){
            Resource.Error(message = "${e.message}")
        }catch (e: IOException){
            Resource.Error(message = "${e.message}")
        }
    }
}