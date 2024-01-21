package kz.divtech.odyssey.rotation.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import kz.divtech.odssey.database.OdysseyDatabase
import kz.divtech.odyssey.shared.data.local.data_source.DatabaseDriverFactory
import kz.divtech.odyssey.shared.data.local.data_source.active_trips.SqlDelightActiveTripsDataSource
import kz.divtech.odyssey.shared.data.local.data_source.archive_trips.SqlDelightArchiveTripsTripsDataSource
import kz.divtech.odyssey.shared.data.local.data_source.employee.SqlDelightEmployeeDataSource
import kz.divtech.odyssey.shared.data.local.data_source.faq.SqlDelightFaqDataSource
import kz.divtech.odyssey.shared.data.local.data_source.full_article.SqlDelightFullArticleDataSource
import kz.divtech.odyssey.shared.data.local.data_source.nearest_trip.SqlDelightNearestTripDataSource
import kz.divtech.odyssey.shared.data.local.data_source.news.SqlDelightNewsDataSource
import kz.divtech.odyssey.shared.data.local.data_source.notification.SqlDelightNotifcationDataSource
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
import kz.divtech.odyssey.shared.domain.data_source.ActiveTripDataSource
import kz.divtech.odyssey.shared.domain.data_source.ArchiveTripsDataSource
import kz.divtech.odyssey.shared.domain.data_source.EmployeeDataSource
import kz.divtech.odyssey.shared.domain.data_source.FaqDataSource
import kz.divtech.odyssey.shared.domain.data_source.FullArticleDataSource
import kz.divtech.odyssey.shared.domain.data_source.NearestTripDataSource
import kz.divtech.odyssey.shared.domain.data_source.NewsDataSource
import kz.divtech.odyssey.shared.domain.data_source.NotificationDataSource
import kz.divtech.odyssey.shared.domain.data_source.OrgInfoDataSource
import org.greenrobot.eventbus.EventBus

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideHttpClient(dataStoreManager: DataStoreManager, eventBus: EventBus): HttpClient{
        return MainApi(dataStoreManager, eventBus).httpClient
    }

    @Provides
    @Singleton
    fun provideEventBus(): EventBus {
        return EventBus.getDefault()
    }

    @Provides
    @Singleton
    fun provideSharedFindEmployeeRepository(httpClient: HttpClient, dataStoreManager: DataStoreManager): kz.divtech.odyssey.shared.domain.repository.FindEmployeeRepository {
        return kz.divtech.odyssey.shared.data.repository.FindEmployeeRepositoryImpl(httpClient, dataStoreManager)
    }

    @Provides
    @Singleton
    fun provideSharedLoginRepository(httpClient: HttpClient, dataStoreManager: DataStoreManager,
                                     profileRepository: kz.divtech.odyssey.shared.domain.repository.ProfileRepository):
            kz.divtech.odyssey.shared.domain.repository.LoginRepository{
        return LoginRepositoryImpl(httpClient, dataStoreManager, profileRepository)
    }

    @Provides
    @Singleton
    fun provideSharedTermsRepository(httpClient: HttpClient,
                                     dataStoreManager: DataStoreManager,
                                     employeeDataSource: EmployeeDataSource
                                     ): kz.divtech.odyssey.shared.domain.repository.TermsRepository{
        return TermsRepositoryImpl(httpClient, dataStoreManager, employeeDataSource)
    }

    @Provides
    @Singleton
    fun provideSharedOrgInfoRepository(httpClient: HttpClient,
                                       dataSource: OrgInfoDataSource): kz.divtech.odyssey.shared.domain.repository.OrgInfoRepository{
        return OrgInfoRepositoryImpl(httpClient, dataSource)
    }

    @Provides
    @Singleton
    fun provideSharedFaqRepository(httpClient: HttpClient,
                                   dataSource: FaqDataSource): kz.divtech.odyssey.shared.domain.repository.FaqRepository{
        return FaqRepositoryImpl(httpClient, dataSource)
    }

    @Provides
    @Singleton
    fun provideSharedArticleRepository(httpClient: HttpClient,
                                       dataStoreManager: DataStoreManager,
                                       dataSource: FullArticleDataSource):
            kz.divtech.odyssey.shared.domain.repository.ArticleRepository{
        return ArticleRepositoryImpl(httpClient, dataStoreManager, dataSource)
    }

    @Provides
    @Singleton
    fun provideSharedRefundRepository(httpClient: HttpClient,
                                      dataStoreManager: DataStoreManager):
            kz.divtech.odyssey.shared.domain.repository.RefundRepository{
        return RefundRepositoryImpl(httpClient, dataStoreManager)
    }

    @Provides
    @Singleton
    fun provideSharedProfileRepository(httpClient: HttpClient,
                                       dataStoreManager: DataStoreManager,
                                       dataSource: EmployeeDataSource):
            kz.divtech.odyssey.shared.domain.repository.ProfileRepository{
        return ProfileRepositoryImpl(httpClient, dataStoreManager, dataSource)
    }

    @Provides
    @Singleton
    fun provideSharedNotificationsRepository(httpClient: HttpClient,
                                             dataStoreManager: DataStoreManager,
                                             dataSource: NotificationDataSource): NotificationsRepository{
        return NotificationsRepositoryImpl(httpClient, dataStoreManager, dataSource)
    }

    @Provides
    @Singleton
    fun provideSharedTripsRepository(httpClient: HttpClient,
                                     dataStoreManager: DataStoreManager,
                                     activeTripDataSource: ActiveTripDataSource,
                                     archiveTripsDataSource: ArchiveTripsDataSource,
                                     nearestTripDataSource: NearestTripDataSource):
            kz.divtech.odyssey.shared.domain.repository.TripsRepository{
        return TripsRepositoryImpl(httpClient, dataStoreManager, activeTripDataSource,
            archiveTripsDataSource, nearestTripDataSource)
    }

    @Provides
    @Singleton
    fun provideSharedNewsRepository(httpClient: HttpClient,
                                    dataStoreManager: DataStoreManager,
                                    newsDataSource: NewsDataSource):
            kz.divtech.odyssey.shared.domain.repository.NewsRepository{
        return NewsRepositoryImpl(httpClient, dataStoreManager, newsDataSource)
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
    fun provideSharedDatabase(@ApplicationContext context: Context): OdysseyDatabase{
        val sqlDriver = DatabaseDriverFactory(context).createDriver()
        return OdysseyDatabase(sqlDriver)
    }

    @Provides
    @Singleton
    fun provideOrgInfoDataSource(database: OdysseyDatabase): OrgInfoDataSource{
        return SqlDelightOrgInfoDataSource(database)
    }

    @Provides
    @Singleton
    fun provideFullArticleDataSource(database: OdysseyDatabase): FullArticleDataSource{
        return SqlDelightFullArticleDataSource(database)
    }

    @Provides
    @Singleton
    fun provideFaqDataSource(database: OdysseyDatabase): FaqDataSource{
        return SqlDelightFaqDataSource(database)
    }

    @Provides
    @Singleton
    fun provideEmployeeDataSource(database: OdysseyDatabase): EmployeeDataSource{
        return SqlDelightEmployeeDataSource(database)
    }

    @Provides
    @Singleton
    fun provideActiveTripsDataSource(database: OdysseyDatabase): ActiveTripDataSource{
        return SqlDelightActiveTripsDataSource(database)
    }

    @Provides
    @Singleton
    fun provideArchiveTripsDataSource(database: OdysseyDatabase): ArchiveTripsDataSource{
        return SqlDelightArchiveTripsTripsDataSource(database)
    }

    @Provides
    @Singleton
    fun provideNearestActiveTripDataSource(database: OdysseyDatabase): NearestTripDataSource{
        return SqlDelightNearestTripDataSource(database)
    }

    @Provides
    @Singleton
    fun provideNewsDataSource(database: OdysseyDatabase): NewsDataSource{
        return SqlDelightNewsDataSource(database)
    }

    @Provides
    @Singleton
    fun provideNotificationDataSource(database: OdysseyDatabase): NotificationDataSource{
        return SqlDelightNotifcationDataSource(database)
    }

}