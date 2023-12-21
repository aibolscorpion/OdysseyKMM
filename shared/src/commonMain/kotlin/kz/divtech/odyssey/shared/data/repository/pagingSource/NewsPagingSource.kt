package kz.divtech.odyssey.shared.data.repository.pagingSource

import app.cash.paging.PagingState
import app.cash.paging.PagingSource
import app.cash.paging.PagingSourceLoadParams
import app.cash.paging.PagingSourceLoadResultError
import app.cash.paging.PagingSourceLoadResultPage
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.url
import io.ktor.http.isSuccess
import io.ktor.utils.io.errors.IOException
import kz.divtech.odyssey.shared.common.Constants.NEWS_PAGE_SIZE
import kz.divtech.odyssey.shared.data.remote.HttpRoutes
import kz.divtech.odyssey.shared.domain.model.help.press_service.news.Article
import kz.divtech.odyssey.shared.domain.model.help.press_service.news.News

class NewsPagingSource(private val httpClient: HttpClient):
    PagingSource<Int, Article>(){
    override fun getRefreshKey(state: PagingState<Int, Article>): Int?  = null
    override suspend fun load(params: PagingSourceLoadParams<Int>): LoadResult<Int, Article> {
        val page = params.key ?: FIRST_PAGE_INDEX
         try {
            val httpResponse = httpClient.get{
                url(HttpRoutes.GET_NEWS)
                parameter("page", page)
            }
             return when{
                 httpResponse.status.isSuccess() -> {
                     val news: News = httpResponse.body()
                     PagingSourceLoadResultPage(
                         data = news.data,
                         prevKey = (page - 1).takeIf { it >= FIRST_PAGE_INDEX },
                         nextKey = if (news.data.size >= NEWS_PAGE_SIZE) page + 1 else null,
                     )
                 }else -> {
                     PagingSourceLoadResultError(
                         Exception("Received a ${httpResponse.status}."),
                     )
                 }
             }
        }catch (e: ClientRequestException) {
             return PagingSourceLoadResultError(
                Exception(e.response.status.description),
            )
        } catch (e: ServerResponseException) {
             return PagingSourceLoadResultError(
                Exception(e.response.status.description),
            )
        } catch (e: IOException) {
             return PagingSourceLoadResultError(
                Exception("${e.message}"),
            )
        } catch (e: Exception){
             return PagingSourceLoadResultError(
                Exception("${e.message}"),
            )
        }
    }

    companion object {
        const val FIRST_PAGE_INDEX = 1
    }

}