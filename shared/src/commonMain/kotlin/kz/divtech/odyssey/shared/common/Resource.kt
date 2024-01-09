package kz.divtech.odyssey.shared.common

sealed class Resource<T>(val data: T? = null, val message: String? = null){
    class Success<T>(data: T) : Resource<T>(data)
    sealed class Error<T>(message: String): Resource<T>(message = message){
        sealed class HttpException<T>(message: String)
            : Error<T>(message = message){
            class BadRequest<T>(message: String): HttpException<T>(message)
            class TooManyRequest<T>(val seconds: Int, message: String): HttpException<T>(message)
            class UnprocessibleEntity<T>(message: String): HttpException<T>(message)
            class Exception<T>(message: String): HttpException<T>(message)
            }
        class IOException<T>(message: String): Error<T>(message = message)
        class Exception<T>(message: String): Error<T>(message = message)
    }

}
