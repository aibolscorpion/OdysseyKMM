package kz.divtech.odyssey.shared.domain.repository

import io.ktor.client.statement.HttpResponse
import kz.divtech.odyssey.shared.common.Resource

interface TermsRepository {
    suspend fun getTermsOfAgreement(): Resource<String>

    suspend fun updateUAConfirm(): Resource<HttpResponse>
}