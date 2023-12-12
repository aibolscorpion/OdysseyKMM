package kz.divtech.odyssey.rotation.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kz.divtech.odyssey.rotation.data.local.AppDatabase
import kz.divtech.odyssey.rotation.data.local.Dao
import kz.divtech.odyssey.rotation.data.remote.retrofit.ApiService
import kz.divtech.odyssey.rotation.data.remote.retrofit.RetrofitClient
import kz.divtech.odyssey.rotation.data.repository.ArticleRepository
import kz.divtech.odyssey.rotation.data.repository.EmployeeRepository
import kz.divtech.odyssey.rotation.data.repository.FaqRepository
import kz.divtech.odyssey.rotation.data.repository.FindEmployeeRepository
import kz.divtech.odyssey.rotation.data.repository.LoginRepository
import kz.divtech.odyssey.rotation.data.repository.NewsRepository
import kz.divtech.odyssey.rotation.data.repository.NotificationRepository
import kz.divtech.odyssey.rotation.data.repository.OrgInfoRepository
import kz.divtech.odyssey.rotation.data.repository.RefundRepository
import kz.divtech.odyssey.rotation.data.repository.TermsRepository
import kz.divtech.odyssey.rotation.data.repository.TripsRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDao(@ApplicationContext context: Context): Dao{
        return AppDatabase.getDatabase(context).dao()
    }
    
    @Provides
    fun provideNewsRepository(dao: Dao, @kz.divtech.odyssey.rotation.di.ApiService apiService: ApiService): NewsRepository{
        return NewsRepository(dao, apiService)
    }

    @Provides
    fun provideArticleRepository(dao: Dao, @kz.divtech.odyssey.rotation.di.ApiService apiService: ApiService): ArticleRepository{
        return ArticleRepository(dao, apiService)
    }

    @Provides
    fun provideEmployeeRepository(dao: Dao, @kz.divtech.odyssey.rotation.di.ApiService  apiService: ApiService): EmployeeRepository{
        return EmployeeRepository(dao, apiService)
    }

    @Provides
    fun provideLoginRepository(@kz.divtech.odyssey.rotation.di.ApiService apiService: ApiService): LoginRepository{
        return LoginRepository(apiService)
    }

    @Provides
    fun provideTermsRepository(dao: Dao, @kz.divtech.odyssey.rotation.di.ApiService apiService: ApiService,
                               @ProxyService proxyService: ApiService): TermsRepository{
        return TermsRepository(dao, apiService, proxyService)
    }

    @Provides
    @Singleton
    fun provideFaqRepository(dao: Dao, @ProxyService apiService: ApiService): FaqRepository{
        return FaqRepository(dao, apiService)
    }

    @Provides
    fun provideFindEmployeeRepository(@ProxyService apiService: ApiService): FindEmployeeRepository{
        return FindEmployeeRepository(apiService)
    }

    @Provides
    @Singleton
    fun provideOrgInfoRepository(dao: Dao, @ProxyService apiService: ApiService): OrgInfoRepository{
        return OrgInfoRepository(dao, apiService)
    }

    @Provides
    fun provideNotificationRepository(dao: Dao, @kz.divtech.odyssey.rotation.di.ApiService apiService: ApiService): NotificationRepository{
        return NotificationRepository(dao, apiService)
    }

    @Provides
    fun provideTripsRepository(dao: Dao, @kz.divtech.odyssey.rotation.di.ApiService apiService: ApiService): TripsRepository{
        return TripsRepository(dao, apiService)
    }

    @Provides
    fun provideRefundRepository(@kz.divtech.odyssey.rotation.di.ApiService apiService: ApiService): RefundRepository{
        return RefundRepository(apiService)
    }

    @kz.divtech.odyssey.rotation.di.ApiService
    @Provides
    fun provideApiService(@ApplicationContext context: Context) : ApiService {
        return RetrofitClient.getApiService(context)
    }

    @ProxyService
    @Provides
    fun provideProxyService(): ApiService{
        return RetrofitClient.getProxyService()
    }

}