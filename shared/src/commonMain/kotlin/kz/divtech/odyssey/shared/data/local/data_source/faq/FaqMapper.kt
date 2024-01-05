package kz.divtech.odyssey.shared.data.local.data_source.faq

import database.Faq

fun List<Faq>.toFaqList(): List<kz.divtech.odyssey.shared.domain.model.help.faq.Faq>{
    return this.map {
        kz.divtech.odyssey.shared.domain.model.help.faq.Faq(
            id = it.id.toInt(),
            answer = it.answer,
            question =  it.question
        )
    }
}