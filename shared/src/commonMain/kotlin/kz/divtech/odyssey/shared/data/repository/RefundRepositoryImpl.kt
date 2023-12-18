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
import kz.divtech.odyssey.shared.common.Resource
import kz.divtech.odyssey.shared.data.remote.HttpRoutes
import kz.divtech.odyssey.shared.domain.model.refund.RefundAppResponse
import kz.divtech.odyssey.shared.domain.model.refund.RefundApplication
import kz.divtech.odyssey.shared.domain.repository.RefundRepository

class RefundRepositoryImpl(private val httpClient: HttpClient): RefundRepository {
    override suspend fun sendApplicationToRefund(application: RefundApplication): Resource<RefundAppResponse> {
        return try {
           val result: RefundAppResponse =  httpClient.post {
                contentType(ContentType.Application.Json)
                url(HttpRoutes.SEND_APPLICATION_TO_REFUND)
                setBody(application)
            }.body()
            Resource.Success(result)
        } catch (e: ClientRequestException) {
            Resource.Error(message = e.response.status.description)
        } catch (e: ServerResponseException) {
            Resource.Error(message = e.response.status.description)
        } catch (e: IOException) {
            Resource.Error(message = "${e.message}")
        } catch (e: Exception) {
            Resource.Error(message = "${e.message}")
        }
    }

    override suspend fun cancelRefund(id: Int): Resource<HttpResponse> {
        return try {
            val result = httpClient.post {
                url(HttpRoutes.cancelRefundById(id))
            }
            Resource.Success(result)
        } catch (e: ClientRequestException) {
            Resource.Error(message = e.response.status.description)
        } catch (e: ServerResponseException) {
            Resource.Error(message = e.response.status.description)
        } catch (e: IOException) {
            Resource.Error(message = "${e.message}")
        } catch (e: Exception) {
            Resource.Error(message = "${e.message}")
        }
    }
}