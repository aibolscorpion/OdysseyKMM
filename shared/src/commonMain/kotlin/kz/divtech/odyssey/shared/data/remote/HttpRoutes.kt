package kz.divtech.odyssey.shared.data.remote

object HttpRoutes {
    private const val BASE_URL  = "https://proxy.odyssey.kz/api"
    const val GET_TERMS_OF_AGREEMENT = "$BASE_URL/user-agreement"
    const val GET_EMPLOYEE_BY_PHONE = "$BASE_URL/get-employee-by-phone"
}