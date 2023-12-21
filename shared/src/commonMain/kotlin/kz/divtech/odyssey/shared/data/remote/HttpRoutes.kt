package kz.divtech.odyssey.shared.data.remote

object HttpRoutes {
    private const val PROXY_URL  = "https://proxy.odyssey.kz/api"
    const val GET_TERMS_OF_AGREEMENT = "$PROXY_URL/user-agreement"
    const val GET_EMPLOYEE_BY_PHONE = "$PROXY_URL/get-employee-by-phone"
    const val GET_EMPLOYEE_BY_IIN = "$PROXY_URL/get-employee-by-iin"
    const val GET_ORG_INFO = "$PROXY_URL/get-app-info"
    const val GET_FAQ_LIST = "$PROXY_URL/faqs"

    const val TMP_PTRAVELS_URL = "https://tmp.ptravels.kz/api"
    const val REQUEST_SMS_CODE = "$TMP_PTRAVELS_URL/send-sms-code"
    const val LOGIN = "$TMP_PTRAVELS_URL/login"
    const val LOGOUT = "$TMP_PTRAVELS_URL/logout"
    const val GET_NEWS = "$TMP_PTRAVELS_URL/articles"
    fun getArticleById(id: Int) = "$TMP_PTRAVELS_URL/articles/$id"
    fun markArticleAsRead(id: Int) = "$TMP_PTRAVELS_URL/articles/$id/mark-as-read"
    const val SEND_APPLICATION_TO_REFUND = "$TMP_PTRAVELS_URL/refund-applications"
    fun cancelRefundById(id: Int) = "$TMP_PTRAVELS_URL/refund-applications/$id/cancel"
    const val PROFILE = "$TMP_PTRAVELS_URL/profile"
    const val UPDATE_UA_CONFIRM = "$TMP_PTRAVELS_URL/profile/update-ua-confirm"
    const val SEND_DEVICE_INFO = "$TMP_PTRAVELS_URL/fix-device"
    const val UPDATE_PHONE_WITHOUT_AUTH = "$TMP_PTRAVELS_URL/update-phone-request"
    const val GET_NOTIFICATIONS = "$TMP_PTRAVELS_URL/notifications"
    const val MARK_NOTIFICATION_AS_READ = "$TMP_PTRAVELS_URL/notifications/mark-as-read"
    fun getTripById(id: Int) = "$TMP_PTRAVELS_URL/applications/$id"
    const val GET_NEAREST_ACTIVE_TRIP = "$TMP_PTRAVELS_URL/applications/get-nearest-active-app"

    const val UPDATE_PHONE_WITH_AUTH = "$TMP_PTRAVELS_URL/profile/update-phone"
    const val UPDATE_PHONE_WITH_AUTH_CONFIRM = "$TMP_PTRAVELS_URL/profile/update-phone-confirm"

    const val UPDATE_DOCUMENT = "$TMP_PTRAVELS_URL/profile/update-document"

    const val GET_ACTIVE_TRIPS = "$TMP_PTRAVELS_URL/applications/active"
    const val GET_ARCHIVE_TRIPS = "$TMP_PTRAVELS_URL/applications/archive"

}