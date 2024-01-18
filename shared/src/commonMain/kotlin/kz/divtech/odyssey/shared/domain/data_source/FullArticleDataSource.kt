package kz.divtech.odyssey.shared.domain.data_source

import kotlinx.coroutines.flow.Flow
import kz.divtech.odyssey.shared.domain.model.help.press_service.article.FullArticle

interface FullArticleDataSource {
    fun getArticleById(id: Int) : Flow<FullArticle?>
    fun insertArticle(fullArticle: FullArticle)
    fun deleteFullArticles()
}