package kz.divtech.odyssey.shared.data.local.data_source.faq

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kz.divtech.odssey.database.OdysseyDatabase
import kz.divtech.odyssey.shared.domain.data_source.FaqDataSource
import kz.divtech.odyssey.shared.domain.model.help.faq.Faq

class SqlDelightFaqDataSource(database: OdysseyDatabase): FaqDataSource {
    private val queries = database.faqQueries
    override fun getFaq(): Flow<List<Faq>> {
        return queries.getFaqList(mapper = { id, question, answer, _, _ ->
            Faq(id.toInt(), question, answer)
        }).asFlow().mapToList(Dispatchers.IO)
    }

    override fun searchFaq(searchQuery: String): List<Faq> {
        return queries.searchFaqByText(searchQuery).executeAsList().toFaqList()
    }

    override fun insertFAQ(faqList: List<Faq>) {
        queries.transaction {
            faqList.forEach { faq ->
                queries.insertFaq(
                    id = faq.id?.toLong(),
                    question = faq.question,
                    answer = faq.answer,
                    createdAt = null,
                    updatedAt = null
                )
            }
        }
    }

    override fun refreshFaq(faqList: List<Faq>) {
        queries.transaction {
            queries.deleteFaqList()
            faqList.forEach { faq ->
                queries.insertFaq(
                    id = faq.id?.toLong(),
                    question = faq.question,
                    answer = faq.answer,
                    createdAt = null,
                    updatedAt = null
                )
            }
        }
    }

    override fun deleteFaq() {
        queries.deleteFaqList()
    }

}