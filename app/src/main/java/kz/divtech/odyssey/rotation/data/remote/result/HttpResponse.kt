package kz.divtech.odyssey.rotation.data.remote.result

interface HttpResponse {

    val statusCode: Int

    val statusMessage: String?

    val url: String?
}