package kz.divtech.odyssey.shared.data.local.data_source.faq

import kz.divtech.odssey.database.OdysseyDatabase
import kz.divtech.odyssey.shared.domain.data_source.FaqDataSource
import kz.divtech.odyssey.shared.domain.model.help.faq.Faq

class SqlDelightFaqDataSource(database: OdysseyDatabase): FaqDataSource {
    private val queries = database.faqQueries
    override suspend fun getFaq(): List<Faq> {
        return queries.getFaqList().executeAsList().toFaqList()
    }

    override suspend fun searchFaq(searchQuery: String): List<Faq> {
        return queries.searchFaqByText(searchQuery).executeAsList().toFaqList()
    }

    override suspend fun insertFAQ(faqList: List<Faq>) {
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

    override suspend fun refreshFaq(faqList: List<Faq>) {
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

    override suspend fun deleteFaq() {
        queries.deleteFaqList()
    }

}