package kz.divtech.odyssey.shared.data.repository

import io.ktor.client.call.body
import io.ktor.client.request.get
import kz.divtech.odyssey.shared.data.remote.HttpRoutes
import kz.divtech.odyssey.shared.data.remote.MainApi

class TermsRepository() {

    private val httpClient = MainApi.httpClient

    suspend fun getTermsOfAgreement(): String {
        return httpClient.get(HttpRoutes.GET_TERMS_OF_AGREEMENT).body()
    }



}