package kz.divtech.odyssey.shared

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform