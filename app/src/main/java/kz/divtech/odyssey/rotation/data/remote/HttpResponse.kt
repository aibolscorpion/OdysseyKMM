package kz.divtech.odyssey.rotation.data.remote

interface HttpResponse {

    val statusCode: Int

    val statusMessage: String?

    val url: String?
}