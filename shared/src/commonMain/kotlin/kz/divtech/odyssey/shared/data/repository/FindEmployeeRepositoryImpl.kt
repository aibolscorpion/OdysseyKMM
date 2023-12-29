package kz.divtech.odyssey.shared.data.repository

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.url
import io.ktor.utils.io.errors.IOException
import kz.divtech.odyssey.shared.common.Resource
import kz.divtech.odyssey.shared.data.local.data_store.DataStoreManager
import kz.divtech.odyssey.shared.data.remote.HttpRoutes
import kz.divtech.odyssey.shared.domain.model.auth.search_employee.EmployeeResult
import kz.divtech.odyssey.shared.domain.repository.FindEmployeeRepository

class FindEmployeeRepositoryImpl(private val httpClient: HttpClient,
        private val dataStoreManager: DataStoreManager
): FindEmployeeRepository {
    override suspend fun findByPhoneNumber(phoneNumber: String) : Resource<EmployeeResult> {
        return try {
            val result: EmployeeResult = httpClient.get{
                url(HttpRoutes.GET_EMPLOYEE_BY_PHONE)
                parameter("phone", phoneNumber)
            }.body()
            result.url?.let { dataStoreManager.saveBaseUrl(it) }
            Resource.Success(result)
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

    override suspend fun findByIIN(iin: String): Resource<EmployeeResult> {
        return try{
            val result: EmployeeResult = httpClient.get{
                url(HttpRoutes.GET_EMPLOYEE_BY_IIN)
                parameter("iin", iin)
            }.body()
            result.url?.let { dataStoreManager.saveBaseUrl(it) }
            Resource.Success(result)
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


}