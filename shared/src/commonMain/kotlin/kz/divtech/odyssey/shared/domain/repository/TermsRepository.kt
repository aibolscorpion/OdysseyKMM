package kz.divtech.odyssey.shared.domain.repository

import io.ktor.client.statement.HttpResponse
import kotlinx.coroutines.flow.Flow
import kz.divtech.odyssey.shared.common.Resource
import java.io.File

interface TermsRepository {
    suspend fun getTermsOfAgreement(): Resource<String>

    suspend fun updateUAConfirm(): Resource<HttpResponse>

    fun deleteTermsFile(fileToDelete: File)

    fun getUaConfirmedFromDB(): Flow<Long?>
}