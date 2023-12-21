package kz.divtech.odyssey.shared.domain.repository

import io.ktor.client.statement.HttpResponse
import kz.divtech.odyssey.shared.common.Resource
import kz.divtech.odyssey.shared.domain.model.trips.refund.create.RefundAppResponse
import kz.divtech.odyssey.shared.domain.model.trips.refund.create.RefundApplication

interface RefundRepository {
    suspend fun sendApplicationToRefund(application: RefundApplication): Resource<RefundAppResponse>
    suspend fun cancelRefund(id: Int): Resource<HttpResponse>
}