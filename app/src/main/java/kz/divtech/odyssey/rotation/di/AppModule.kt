package kz.divtech.odyssey.rotation.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import app.cash.sqldelight.db.SqlDriver
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import kz.divtech.odssey.database.OdysseyDatabase
import kz.divtech.odyssey.rotation.data.local.AppDatabase
import kz.divtech.odyssey.rotation.data.local.Dao
import kz.divtech.odyssey.rotation.data.remote.retrofit.ApiService
import kz.divtech.odyssey.rotation.data.remote.retrofit.ProxyApiService
import kz.divtech.odyssey.rotation.data.remote.retrofit.RetrofitClient
import kz.divtech.odyssey.rotation.data.repository.ArticleRepository
import kz.divtech.odyssey.rotation.data.repository.ProfileRepository
import kz.divtech.odyssey.rotation.data.repository.FaqRepository
import kz.divtech.odyssey.rotation.data.repository.FindEmployeeRepository
import kz.divtech.odyssey.rotation.data.repository.LoginRepository
import kz.divtech.odyssey.rotation.data.repository.NewsRepository
import kz.divtech.odyssey.rotation.data.repository.NotificationRepository
import kz.divtech.odyssey.rotation.data.repository.OrgInfoRepository
import kz.divtech.odyssey.rotation.data.repository.RefundRepository
import kz.divtech.odyssey.rotation.data.repository.TermsRepository
import kz.divtech.odyssey.rotation.data.repository.TripsRepository
import kz.divtech.odyssey.shared.data.local.data_source.DatabaseDriverFactory
import kz.divtech.odyssey.shared.data.local.data_source.org_info.SqlDelightOrgInfoDataSource
import kz.divtech.odyssey.shared.data.local.data_store.DataStoreManager
import kz.divtech.odyssey.shared.data.local.data_store.LocalDataStore.createDataStore
import kz.divtech.odyssey.shared.data.remote.MainApi
import kz.divtech.odyssey.shared.data.repository.ArticleRepositoryImpl
import kz.divtech.odyssey.shared.data.repository.ProfileRepositoryImpl
import kz.divtech.odyssey.shared.data.repository.FaqRepositoryImpl
import kz.divtech.odyssey.shared.data.repository.LoginRepositoryImpl
import kz.divtech.odyssey.shared.data.repository.NewsRepositoryImpl
import kz.divtech.odyssey.shared.data.repository.NotificationsRepositoryImpl
import kz.divtech.odyssey.shared.data.repository.OrgInfoRepositoryImpl
import kz.divtech.odyssey.shared.data.repository.RefundRepositoryImpl
import kz.divtech.odyssey.shared.data.repository.TermsRepositoryImpl
import kz.divtech.odyssey.shared.data.repository.TripsRepositoryImpl
import kz.divtech.odyssey.shared.domain.repository.NotificationsRepository
import javax.inject.Singleton
import kz.divtech.odyssey.shared.data.local.data_store.LocalDataStore.dataStoreFileName
import kz.divtech.odyssey.shared.domain.data_source.OrgInfoDataSource

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDao(@ApplicationContext context: Context): Dao{
        return AppDatabase.getDatabase(context).dao()
    }
    
    @Provides
    fun provideNewsRepository(dao: Dao, apiService: ApiService): NewsRepository{
        return NewsRepository(dao, apiService)
    }

    @Provides
    fun provideArticleRepository(dao: Dao, apiService: ApiService): ArticleRepository{
        return ArticleRepository(dao, apiService)
    }

    @Provides
    fun provideProfileRepository(dao: Dao, apiService: ApiService): ProfileRepository{
        return ProfileRepository(dao, apiService)
    }

    @Provides
    fun provideLoginRepository(apiService: ApiService): LoginRepository{
        return LoginRepository(apiService)
    }

    @Provides
    fun provideTermsRepository(dao: Dao, apiService: ApiService, proxyService: ProxyApiService): TermsRepository{
        return TermsRepository(dao, apiService, proxyService)
    }

    @Provides
    @Singleton
    fun provideFaqRepository(dao: Dao, apiService: ProxyApiService): FaqRepository{
        return FaqRepository(dao, apiService)
    }

    @Provides
    fun provideFindEmployeeRepository(apiService: ProxyApiService): FindEmployeeRepository{
        return FindEmployeeRepository(apiService)
    }

    @Provides
    @Singleton
    fun provideOrgInfoRepository(dao: Dao, apiService: ProxyApiService): OrgInfoRepository{
        return OrgInfoRepository(dao, apiService)
    }

    @Provides
    fun provideNotificationRepository(dao: Dao, apiService: ApiService): NotificationRepository{
        return NotificationRepository(dao, apiService)
    }

    @Provides
    fun provideTripsRepository(dao: Dao, apiService: ApiService): TripsRepository{
        return TripsRepository(dao, apiService)
    }

    @Provides
    fun provideRefundRepository(apiService: ApiService): RefundRepository{
        return RefundRepository(apiService)
    }

    @Provides
    fun provideApiService() : ApiService {
        return RetrofitClient.getApiService()
    }

    @Provides
    fun provideProxyService(): ProxyApiService{
        return RetrofitClient.getProxyService()
    }

    @Provides
    @Singleton
    fun provideHttpClient(dataStoreManager: DataStoreManager): HttpClient{
        return MainApi(dataStoreManager).httpClient
    }
    @Provides
    @Singleton
    fun provideSharedFindEmployeeRepository(httpClient: HttpClient, dataStoreManager: DataStoreManager): kz.divtech.odyssey.shared.domain.repository.FindEmployeeRepository {
        return kz.divtech.odyssey.shared.data.repository.FindEmployeeRepositoryImpl(httpClient, dataStoreManager)
    }

    @Provides
    @Singleton
    fun provideSharedLoginRepository(httpClient: HttpClient, dataStoreManager: DataStoreManager):
            kz.divtech.odyssey.shared.domain.repository.LoginRepository{
        return LoginRepositoryImpl(httpClient, dataStoreManager)
    }

    @Provides
    @Singleton
    fun provideSharedTermsRepository(httpClient: HttpClient, dataStoreManager: DataStoreManager): kz.divtech.odyssey.shared.domain.repository.TermsRepository{
        return TermsRepositoryImpl(httpClient, dataStoreManager)
    }

    @Provides
    @Singleton
    fun provideSharedOrgInfoRepository(httpClient: HttpClient, dataSource: OrgInfoDataSource): kz.divtech.odyssey.shared.domain.repository.OrgInfoRepository{
        return OrgInfoRepositoryImpl(httpClient, dataSource)
    }

    @Provides
    @Singleton
    fun provideSharedFaqRepository(httpClient: HttpClient): kz.divtech.odyssey.shared.domain.repository.FaqRepository{
        return FaqRepositoryImpl(httpClient)
    }

    @Provides
    @Singleton
    fun provideSharedArticleRepository(httpClient: HttpClient, dataStoreManager: DataStoreManager):
            kz.divtech.odyssey.shared.domain.repository.ArticleRepository{
        return ArticleRepositoryImpl(httpClient, dataStoreManager)
    }

    @Provides
    @Singleton
    fun provideSharedRefundRepository(httpClient: HttpClient, dataStoreManager: DataStoreManager):
            kz.divtech.odyssey.shared.domain.repository.RefundRepository{
        return RefundRepositoryImpl(httpClient, dataStoreManager)
    }

    @Provides
    @Singleton
    fun provideSharedProfileRepository(httpClient: HttpClient, dataStoreManager: DataStoreManager):
            kz.divtech.odyssey.shared.domain.repository.ProfileRepository{
        return ProfileRepositoryImpl(httpClient, dataStoreManager)
    }

    @Provides
    @Singleton
    fun provideSharedNotificationsRepository(httpClient: HttpClient, dataStoreManager: DataStoreManager): NotificationsRepository{
        return NotificationsRepositoryImpl(httpClient, dataStoreManager)
    }

    @Provides
    @Singleton
    fun provideSharedTripsRepository(httpClient: HttpClient, dataStoreManager: DataStoreManager):
            kz.divtech.odyssey.shared.domain.repository.TripsRepository{
        return TripsRepositoryImpl(httpClient, dataStoreManager)
    }

    @Provides
    @Singleton
    fun provideSharedNewsRepository(httpClient: HttpClient, dataStoreManager: DataStoreManager):
            kz.divtech.odyssey.shared.domain.repository.NewsRepository{
        return NewsRepositoryImpl(httpClient, dataStoreManager)
    }

    @Provides
    @Singleton
    fun provideSharedDataStore(@ApplicationContext context: Context): DataStore<Preferences> {
        return createDataStore(
            producePath = { context.filesDir.resolve(dataStoreFileName).absoluteFile }
        )
    }

    @Provides
    @Singleton
    fun provideSharedDataStoreManager(dataStore: DataStore<Preferences>): DataStoreManager {
        return DataStoreManager(dataStore)
    }

    @Provides
    @Singleton
    fun provideSqlDriver(@ApplicationContext context: Context): SqlDriver{
        return DatabaseDriverFactory(context).createDriver()
    }

    @Provides
    @Singleton
    fun provideOrgInfoDataSource(sqlDriver: SqlDriver): OrgInfoDataSource{
        return SqlDelightOrgInfoDataSource(OdysseyDatabase(sqlDriver))
    }

}