package kz.divtech.odyssey.shared.data.repository

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
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
import kz.divtech.odyssey.shared.common.Resource
import kz.divtech.odyssey.shared.data.local.data_store.DataStoreManager
import kz.divtech.odyssey.shared.data.remote.HttpRoutes
import kz.divtech.odyssey.shared.domain.data_source.EmployeeDataSource
import kz.divtech.odyssey.shared.domain.model.DeviceInfo
import kz.divtech.odyssey.shared.domain.model.UpdatePhoneRequest
import kz.divtech.odyssey.shared.domain.model.auth.login.AuthRequest
import kz.divtech.odyssey.shared.domain.model.auth.sendsms.CodeRequest
import kz.divtech.odyssey.shared.domain.model.auth.sendsms.CodeResponse
import kz.divtech.odyssey.shared.domain.model.errors.ValidationErrorResponse
import kz.divtech.odyssey.shared.domain.model.profile.Document
import kz.divtech.odyssey.shared.domain.model.profile.Profile
import kz.divtech.odyssey.shared.domain.model.profile.ProfileResponse
import kz.divtech.odyssey.shared.domain.repository.ProfileRepository

class ProfileRepositoryImpl(private val httpClient: HttpClient,
                            private val dataStoreManager: DataStoreManager,
                            private val dataSource: EmployeeDataSource
): ProfileRepository {
    override suspend fun getProfile(): Resource<ProfileResponse> {
        return try {
            val result: ProfileResponse = httpClient.get{
                url(HttpRoutes(dataStoreManager).profile())
            }.body()
            dataSource.insertProfile(result.data)
            Resource.Success(data = result)
        }catch (e: IOException){
            Resource.Error.IOException(e.message.toString())
        }catch (e: Exception){
            Resource.Error.Exception(e.message.toString())
        }
    }

    override suspend fun insertProfile(profile: Profile) {
        dataSource.insertProfile(profile)
    }

    override suspend fun getProfileFromDb(): Profile? {
        return dataSource.getProfile()
    }

    override suspend fun deleteProfile() {
        dataSource.deleteProfile()
    }

    override suspend fun updateProfile(profile: Profile): Resource<HttpResponse> {
        return try {
            val result = httpClient.post{
                url(HttpRoutes(dataStoreManager).profile())
                contentType(ContentType.Application.Json)
                setBody(profile)
            }
            Resource.Success(data = result)
        }catch (e: IOException){
            Resource.Error.IOException(e.message.toString())
        }catch (e: Exception){
            Resource.Error.Exception(e.message.toString())
        }
    }

    override suspend fun sendDeviceInfo(deviceInfo: DeviceInfo): Resource<HttpResponse> {
        return try {
            val result = httpClient.post{
                url(HttpRoutes(dataStoreManager).sendDeviceInfo())
                contentType(ContentType.Application.Json)
                setBody(deviceInfo)
            }
            Resource.Success(data = result)
        }catch (e: IOException){
            Resource.Error.IOException(e.message.toString())
        }catch (e: Exception){
            Resource.Error.Exception(e.message.toString())
        }
    }

    override suspend fun updateDocument(document: Document): Resource<HttpResponse> {
        return try {
            val result = httpClient.post{
                url(HttpRoutes(dataStoreManager).updateDocument())
                contentType(ContentType.Application.Json)
                setBody(document)
            }
            Resource.Success(data = result)
        }catch (e: IOException){
            Resource.Error.IOException(e.message.toString())
        }catch (e: Exception){
            Resource.Error.Exception(e.message.toString())
        }
    }


    override suspend fun updatePhoneNumberWihoutAuth(phoneRequest: UpdatePhoneRequest): Resource<HttpResponse> {
        return try {
            val result = httpClient.post{
                url(HttpRoutes(dataStoreManager).updatePhoneWithoutAuth())
                contentType(ContentType.Application.Json)
                setBody(phoneRequest)
            }
            when (result.status) {
                HttpStatusCode.OK -> {
                    Resource.Success(data = result)
                }
                HttpStatusCode.UnprocessableEntity -> {
                    val errorResponse: ValidationErrorResponse = Json.decodeFromString(result.bodyAsText())
                    var phoneErrorText: String? = null
                    errorResponse.errors.forEach { (field, errorMessages) ->
                        if (field == "phone") {
                            phoneErrorText = errorMessages.first()
                        }
                    }
                    Resource.Error.HttpException.UnprocessibleEntity(phoneErrorText ?: result.status.description)
                }
                else -> {
                    Resource.Error.HttpException.Exception(result.status.description)
                }
            }
        }catch (e: IOException){
            Resource.Error.IOException(e.message.toString())
        }catch (e: Exception){
            Resource.Error.Exception(e.message.toString())
        }
    }

    override suspend fun updatePhoneNumberWithAuth(phoneNumber: String): Resource<CodeResponse> {
        val codeRequest = CodeRequest(phoneNumber, Config.IS_TEST)
        return try {
            val response: HttpResponse = httpClient.post{
                url(HttpRoutes(dataStoreManager).updatePhoneWithAuth())
                contentType(ContentType.Application.Json)
                setBody(codeRequest)
            }
            val result: CodeResponse = response.body()
            Resource.Success(result)
        }catch (e: IOException){
            Resource.Error.IOException(e.message.toString())
        }catch (e: Exception){
            Resource.Error.Exception(e.message.toString())
        }
    }

    override suspend fun updatePhoneConfirm(authRequest: AuthRequest): Resource<HttpResponse> {
        return try {
            val result: HttpResponse = httpClient.post{
                url(HttpRoutes(dataStoreManager).updatePhoneWithAuthConfirm())
                contentType(ContentType.Application.Json)
                setBody(authRequest)
            }
            Resource.Success(data = result)
        }catch (e: IOException){
            Resource.Error.IOException(e.message.toString())
        }catch (e: Exception){
            Resource.Error.Exception(e.message.toString())
        }
    }


}