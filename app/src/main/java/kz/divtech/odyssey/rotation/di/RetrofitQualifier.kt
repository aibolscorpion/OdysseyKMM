package kz.divtech.odyssey.rotation.di

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class ApiService

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class ProxyService