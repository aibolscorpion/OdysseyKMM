package kz.divtech.odyssey.shared.data.remote

import kotlinx.coroutines.flow.first
import kz.divtech.odyssey.shared.data.local.data_store.DataStoreManager

class HttpRoutes(private val dataStoreManager: DataStoreManager) {

    private suspend fun getBaseUrl(): String {
        return "${dataStoreManager.getBaseUrl().first()}/api"
    }
    suspend fun requestSmsCode() =  "${getBaseUrl()}/send-sms-code"
    suspend fun login() =  "${getBaseUrl()}/login"
    suspend fun logout() =  "${getBaseUrl()}/logout"
    suspend fun getNews() =  "${getBaseUrl()}/articles"
    suspend fun getArticleById(id: Int) =  "${getBaseUrl()}/articles/$id"
    suspend fun markArticleAsRead(id: Int) =  "${getBaseUrl()}/articles/$id/mark-as-read"
    suspend fun sendApplicationToRefund() =   "${getBaseUrl()}/refund-applications"
    suspend fun cancelRefundById(id: Int) =   "${getBaseUrl()}/refund-applications/$id/cancel"
    suspend fun profile() =  "${getBaseUrl()}/profile"
    suspend fun updateUAConfirm() = "${getBaseUrl()}/profile/update-ua-confirm"
    suspend fun sendDeviceInfo() =  "${getBaseUrl()}/fix-device"
    suspend fun updatePhoneWithoutAuth() =  "${getBaseUrl()}/update-phone-request"
    suspend fun getNotifications() =  "${getBaseUrl()}/notifications"
    suspend fun markNotificationAsRead() =  "${getBaseUrl()}/notifications/mark-as-read"
    suspend fun getTripById(id: Int): String{
        return "${getBaseUrl()}/applications/$id"
    }
    suspend fun getNearestActiveTrip() =  "${getBaseUrl()}/applications/get-nearest-active-app"

    suspend fun updatePhoneWithAuth() =  "${getBaseUrl()}/profile/update-phone"
    suspend fun updatePhoneWithAuthConfirm() =  "${getBaseUrl()}/profile/update-phone-confirm"

    suspend fun updateDocument() =  "${getBaseUrl()}/profile/update-document"

    suspend fun getActiveTrips() =  "${getBaseUrl()}/applications/active"
    suspend fun getArchiveTrips() = "${getBaseUrl()}/applications/archive"

    companion object {
        private const val PROXY_URL  = "https://proxy.odyssey.kz/api"
        const val GET_TERMS_OF_AGREEMENT = "${PROXY_URL}/user-agreement"
        const val GET_EMPLOYEE_BY_PHONE = "${PROXY_URL}/get-employee-by-phone"
        const val GET_EMPLOYEE_BY_IIN = "${PROXY_URL}/get-employee-by-iin"
        const val GET_ORG_INFO = "${PROXY_URL}/get-app-info"
        const val GET_FAQ_LIST = "${PROXY_URL}/faqs"
    }

}