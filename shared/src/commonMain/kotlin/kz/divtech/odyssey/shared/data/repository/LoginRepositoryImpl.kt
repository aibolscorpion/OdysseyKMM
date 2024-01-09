package kz.divtech.odyssey.shared.data.repository

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.request.url
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.utils.io.errors.IOException
import kotlinx.serialization.json.Json
import kz.divtech.odyssey.shared.common.Config
import kz.divtech.odyssey.shared.common.Constants
import kz.divtech.odyssey.shared.common.Resource
import kz.divtech.odyssey.shared.data.local.data_store.DataStoreManager
import kz.divtech.odyssey.shared.data.remote.HttpRoutes
import kz.divtech.odyssey.shared.domain.model.errors.BadRequest
import kz.divtech.odyssey.shared.domain.model.auth.login.AuthRequest
import kz.divtech.odyssey.shared.domain.model.auth.login.employee_response.LoginResponse
import kz.divtech.odyssey.shared.domain.model.auth.sendsms.CodeRequest
import kz.divtech.odyssey.shared.domain.model.auth.sendsms.CodeResponse
import kz.divtech.odyssey.shared.domain.repository.LoginRepository
import kz.divtech.odyssey.shared.domain.repository.ProfileRepository

class LoginRepositoryImpl(private val httpClient: HttpClient,
                          private val dataStoreManager: DataStoreManager,
                          private val profileRepository: ProfileRepository
): LoginRepository {
    override suspend fun requestSmsCode(phoneNumber: String): Resource<CodeResponse> {
        val codeRequest = CodeRequest(phoneNumber, Config.IS_TEST)
        return try {
            val response: HttpResponse = httpClient.post{
                url(HttpRoutes(dataStoreManager).requestSmsCode())
                contentType(ContentType.Application.Json)
                setBody(codeRequest)
            }
            when (response.status) {
                HttpStatusCode.OK -> {
                    val result: CodeResponse = response.body()
                    Resource.Success(result)
                }
                HttpStatusCode.BadRequest -> {
                    val badRequest: BadRequest = Json.decodeFromString(response.bodyAsText())
                    Resource.Error.HttpException.BadRequest(badRequest.message)
                }
                HttpStatusCode.TooManyRequests -> {
                    val badRequest: BadRequest = Json.decodeFromString(response.bodyAsText())
                    val seconds = response.headers[Constants.RETRY_AFTER]!!.toInt()
                    Resource.Error.HttpException.TooManyRequest(seconds, badRequest.message)
                }
                else -> {
                    Resource.Error.HttpException.Exception(response.status.description)
                }
            }
        }catch (e: IOException){
            Resource.Error.IOException(e.message.toString())
        }catch (e: Exception){
            Resource.Error.Exception(e.message.toString())
        }
    }

    override suspend fun login(authRequest: AuthRequest): Resource<LoginResponse> {
        return try {
             val response: HttpResponse = httpClient.post{
                     url(HttpRoutes(dataStoreManager).login())
                     contentType(ContentType.Application.Json)
                     setBody(authRequest)
             }

            when(response.status){
                HttpStatusCode.OK -> {
                    val result: LoginResponse = response.body()
                    dataStoreManager.saveAuthToken(result.token)
                    dataStoreManager.saveOrganizationName(result.organization.name)
                    profileRepository.insertProfile(result.profile)
                    Resource.Success(data = result)
                }
                HttpStatusCode.BadRequest -> {
                    val badRequest: BadRequest = Json.decodeFromString(response.bodyAsText())
                    Resource.Error.HttpException.BadRequest(badRequest.message)
                }
                HttpStatusCode.TooManyRequests -> {
                    val badRequest: BadRequest = Json.decodeFromString(response.bodyAsText())
                    val seconds = response.headers[Constants.RETRY_AFTER]!!.toInt()
                    Resource.Error.HttpException.TooManyRequest(seconds, badRequest.message)
                }
                else -> {
                    Resource.Error.HttpException.Exception(response.status.description)
                }
            }
        }catch (e: IOException){
            Resource.Error.IOException(e.message.toString())
        }catch (e: Exception){
            Resource.Error.Exception(e.message.toString())
        }
    }

    override suspend fun logout(): Resource<HttpResponse> {
        return try {
            val result = httpClient.post{
                url(HttpRoutes(dataStoreManager).logout())
            }
            Resource.Success(data = result)
        }catch (e: IOException){
            Resource.Error.IOException(e.message.toString())
        }catch (e: Exception){
            Resource.Error.Exception(e.message.toString())
        }
    }
}