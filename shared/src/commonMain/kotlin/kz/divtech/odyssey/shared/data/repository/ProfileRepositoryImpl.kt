package kz.divtech.odyssey.shared.data.repository

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.request.url
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.utils.io.errors.IOException
import kz.divtech.odyssey.shared.common.Config
import kz.divtech.odyssey.shared.common.Resource
import kz.divtech.odyssey.shared.data.remote.HttpRoutes
import kz.divtech.odyssey.shared.domain.model.DeviceInfo
import kz.divtech.odyssey.shared.domain.model.UpdatePhoneRequest
import kz.divtech.odyssey.shared.domain.model.auth.login.AuthRequest
import kz.divtech.odyssey.shared.domain.model.auth.sendsms.CodeRequest
import kz.divtech.odyssey.shared.domain.model.auth.sendsms.CodeResponse
import kz.divtech.odyssey.shared.domain.model.profile.Document
import kz.divtech.odyssey.shared.domain.model.profile.Profile
import kz.divtech.odyssey.shared.domain.model.profile.ProfileResponse
import kz.divtech.odyssey.shared.domain.repository.ProfileRepository

class ProfileRepositoryImpl(private val httpClient: HttpClient): ProfileRepository {
    override suspend fun getProfile(): Resource<ProfileResponse> {
        return try {
            val result: ProfileResponse = httpClient.get{
                url(HttpRoutes.PROFILE)
            }.body()
            Resource.Success(data = result)
        }catch (e: ClientRequestException) {
            Resource.Error(message = e.response.status.description)
        } catch (e: ServerResponseException) {
            Resource.Error(message = e.response.status.description)
        } catch (e: IOException) {
            Resource.Error(message = "${e.message}")
        } catch (e: Exception){
            Resource.Error(message = "${e.message}")
        }
    }

    override suspend fun updateProfile(profile: Profile): Resource<HttpResponse> {
        return try {
            val result = httpClient.post{
                url(HttpRoutes.PROFILE)
                contentType(ContentType.Application.Json)
                setBody(profile)
            }
            Resource.Success(data = result)
        }catch (e: ClientRequestException) {
            Resource.Error(message = e.response.status.description)
        } catch (e: ServerResponseException) {
            Resource.Error(message = e.response.status.description)
        } catch (e: IOException) {
            Resource.Error(message = "${e.message}")
        } catch (e: Exception){
            Resource.Error(message = "${e.message}")
        }
    }

    override suspend fun sendDeviceInfo(deviceInfo: DeviceInfo): Resource<HttpResponse> {
        return try {
            val result = httpClient.post{
                url(HttpRoutes.SEND_DEVICE_INFO)
                contentType(ContentType.Application.Json)
                setBody(deviceInfo)
            }
            Resource.Success(data = result)
        }catch (e: ClientRequestException) {
            Resource.Error(message = e.response.status.description)
        } catch (e: ServerResponseException) {
            Resource.Error(message = e.response.status.description)
        } catch (e: IOException) {
            Resource.Error(message = "${e.message}")
        } catch (e: Exception){
            Resource.Error(message = "${e.message}")
        }
    }

    override suspend fun updateDocument(document: Document): Resource<HttpResponse> {
        return try {
            val result = httpClient.post{
                url(HttpRoutes.UPDATE_DOCUMENT)
                contentType(ContentType.Application.Json)
                setBody(document)
            }
            Resource.Success(data = result)
        }catch (e: ClientRequestException) {
            Resource.Error(message = e.response.status.description)
        } catch (e: ServerResponseException) {
            Resource.Error(message = e.response.status.description)
        } catch (e: IOException) {
            Resource.Error(message = "${e.message}")
        } catch (e: Exception){
            Resource.Error(message = "${e.message}")
        }
    }


    override suspend fun updatePhoneNumberWihoutAuth(phoneRequest: UpdatePhoneRequest): Resource<HttpResponse> {
        return try {
            val result = httpClient.post{
                url(HttpRoutes.UPDATE_PHONE_WITHOUT_AUTH)
                contentType(ContentType.Application.Json)
                setBody(phoneRequest)
            }
            Resource.Success(data = result)
        }catch (e: ClientRequestException) {
            Resource.Error(message = e.response.status.description)
        } catch (e: ServerResponseException) {
            Resource.Error(message = e.response.status.description)
        } catch (e: IOException) {
            Resource.Error(message = "${e.message}")
        } catch (e: Exception){
            Resource.Error(message = "${e.message}")
        }
    }

    override suspend fun updatePhoneNumberWithAuth(phoneNumber: String): Resource<CodeResponse> {
        val codeRequest = CodeRequest(phoneNumber, Config.IS_TEST)
        return try {
            val response: HttpResponse = httpClient.post{
                url(HttpRoutes.UPDATE_PHONE_WITH_AUTH)
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

    override suspend fun updatePhoneConfirm(authRequest: AuthRequest): Resource<HttpResponse> {
        return try {
            val result: HttpResponse = httpClient.post{
                url(HttpRoutes.UPDATE_PHONE_WITH_AUTH_CONFIRM)
                contentType(ContentType.Application.Json)
                setBody(authRequest)
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